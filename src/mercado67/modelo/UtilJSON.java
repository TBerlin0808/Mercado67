package mercado67.modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Utilidad ligera para leer y escribir archivos JSON sin dependencias externas.
 * Soporta objetos, listas, strings, numeros, booleanos y nulos.
 */
public final class UtilJSON {

    private UtilJSON() {
    }

    /**
     * Lee todo el contenido de un archivo como String (UTF-8).
     */
    public static String leerArchivo(String rutaArchivo) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new FileReader(archivo, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea).append("\n");
            }
        } catch (IOException e) {
            return null;
        }
        return sb.toString();
    }

    /**
     * Escribe un String a un archivo (UTF-8), creando directorios si es necesario.
     */
    public static boolean escribirArchivo(String rutaArchivo, String contenido) {
        try {
            File archivo = new File(rutaArchivo);
            File dir = archivo.getParentFile();
            if (dir != null && !dir.exists()) {
                dir.mkdirs();
            }
            try (BufferedWriter bw = new BufferedWriter(
                    new FileWriter(archivo, StandardCharsets.UTF_8))) {
                bw.write(contenido);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }



    public static String escaparJSON(String texto) {
        if (texto == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            switch (c) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    public static String mapaAJSON(Map<String, Object> datos) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean primero = true;
        for (Map.Entry<String, Object> e : datos.entrySet()) {
            if (!primero) {
                sb.append(",");
            }
            primero = false;
            sb.append("\"").append(escaparJSON(e.getKey())).append("\":");
            sb.append(valorAJSON(e.getValue()));
        }
        sb.append("}");
        return sb.toString();
    }

    private static String valorAJSON(Object valor) {
        if (valor == null) {
            return "null";
        }
        if (valor instanceof String) {
            return "\"" + escaparJSON((String) valor) + "\"";
        }
        if (valor instanceof Boolean) {
            return valor.toString();
        }
        if (valor instanceof Number) {
            return valor.toString();
        }
        if (valor instanceof List) {
            return listaAJSON((List<?>) valor);
        }
        if (valor instanceof Map) {
            return mapaAJSON((Map<String, Object>) valor);
        }
        return "\"" + escaparJSON(valor.toString()) + "\"";
    }

    public static String listaAJSON(List<?> lista) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean primero = true;
        for (Object item : lista) {
            if (!primero) {
                sb.append(",");
            }
            primero = false;
            sb.append(valorAJSON(item));
        }
        sb.append("]");
        return sb.toString();
    }

 

    private static String json;
    private static int pos;

    @SuppressWarnings("unchecked")
    public static Object parsearJSON(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }
        json = texto;
        pos = 0;
        Object resultado = parsearValor();
        return resultado;
    }

    private static Object parsearValor() {
        saltarEspacios();
        if (pos >= json.length()) {
            return null;
        }
        char c = json.charAt(pos);
        if (c == '{') {
            return parsearObjeto();
        }
        if (c == '[') {
            return parsearLista();
        }
        if (c == '"') {
            return parsearCadena();
        }
        if (c == 't' || c == 'f') {
            return parsearBooleano();
        }
        if (c == 'n') {
            return parsearNulo();
        }
        return parsearNumero();
    }

    private static Map<String, Object> parsearObjeto() {
        Map<String, Object> mapa = new LinkedHashMap<>();
        pos++; // saltar {
        saltarEspacios();
        if (pos < json.length() && json.charAt(pos) == '}') {
            pos++;
            return mapa;
        }
        while (pos < json.length()) {
            saltarEspacios();
            if (pos >= json.length() || json.charAt(pos) != '"') {
                break;
            }
            String clave = (String) parsearCadena();
            saltarEspacios();
            if (pos < json.length() && json.charAt(pos) == ':') {
                pos++;
            }
            Object valor = parsearValor();
            mapa.put(clave, valor);
            saltarEspacios();
            if (pos < json.length() && json.charAt(pos) == ',') {
                pos++;
                continue;
            }
            if (pos < json.length() && json.charAt(pos) == '}') {
                pos++;
                break;
            }
            break;
        }
        return mapa;
    }

    private static List<Object> parsearLista() {
        List<Object> lista = new ArrayList<>();
        pos++; // saltar [
        saltarEspacios();
        if (pos < json.length() && json.charAt(pos) == ']') {
            pos++;
            return lista;
        }
        while (pos < json.length()) {
            Object valor = parsearValor();
            lista.add(valor);
            saltarEspacios();
            if (pos < json.length() && json.charAt(pos) == ',') {
                pos++;
                continue;
            }
            if (pos < json.length() && json.charAt(pos) == ']') {
                pos++;
                break;
            }
            break;
        }
        return lista;
    }

    private static String parsearCadena() {
        StringBuilder sb = new StringBuilder();
        pos++; // saltar comilla de apertura
        while (pos < json.length()) {
            char c = json.charAt(pos);
            if (c == '"') {
                pos++; // consumir comilla de cierre
                return sb.toString();
            }
            if (c == '\\') {
                pos++;
                if (pos < json.length()) {
                    char siguiente = json.charAt(pos);
                    switch (siguiente) {
                        case '"':  sb.append('"');  break;
                        case '\\': sb.append('\\'); break;
                        case '/':  sb.append('/');  break;
                        case 'b':  sb.append('\b'); break;
                        case 'f':  sb.append('\f'); break;
                        case 'n':  sb.append('\n'); break;
                        case 'r':  sb.append('\r'); break;
                        case 't':  sb.append('\t'); break;
                        case 'u':
                            if (pos + 4 < json.length()) {
                                String hex = json.substring(pos + 1, pos + 5);
                                try {
                                    sb.append((char) Integer.parseInt(hex, 16));
                                } catch (NumberFormatException e) {
                                    sb.append('?');
                                }
                                pos += 4;
                            }
                            break;
                        default:
                            sb.append(siguiente);
                    }
                }
            } else {
                sb.append(c);
            }
            pos++;
        }
        return sb.toString();
    }

    private static Boolean parsearBooleano() {
        if (pos + 4 <= json.length() && json.substring(pos, pos + 4).equals("true")) {
            pos += 4;
            return true;
        }
        if (pos + 5 <= json.length() && json.substring(pos, pos + 5).equals("false")) {
            pos += 5;
            return false;
        }
        return null;
    }

    private static Object parsearNulo() {
        if (pos + 4 <= json.length() && json.substring(pos, pos + 4).equals("null")) {
            pos += 4;
            return null;
        }
        return null;
    }

    private static Object parsearNumero() {
        int inicio = pos;
        boolean tienePunto = false;
        boolean tieneExponente = false;
        while (pos < json.length()) {
            char c = json.charAt(pos);
            if (Character.isDigit(c) || c == '-' || c == '+') {
                pos++;
            } else if (c == '.') {
                tienePunto = true;
                pos++;
            } else if (c == 'e' || c == 'E') {
                tieneExponente = true;
                pos++;
                if (pos < json.length() && (json.charAt(pos) == '+' || json.charAt(pos) == '-')) {
                    pos++;
                }
            } else {
                break;
            }
        }
        String numStr = json.substring(inicio, pos);
        try {
            if (tienePunto || tieneExponente) {
                return Double.parseDouble(numStr);
            }
            return Integer.parseInt(numStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static void saltarEspacios() {
        while (pos < json.length()) {
            char c = json.charAt(pos);
            if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                pos++;
            } else {
                break;
            }
        }
    }
}
