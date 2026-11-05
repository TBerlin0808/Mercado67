package mercado67.modelo;

import java.util.ArrayList;
import java.util.List;

public class ServicioUsuarios {

    private final List<Usuario> usuarios = new ArrayList<>();

    public ServicioUsuarios() {
        usuarios.add(new Usuario("Administrador", "admin@mercado67.com", "admin123", true));
        usuarios.add(new Usuario("Cliente de prueba", "cliente@mercado67.com", "1234", false));
    }

    public Usuario iniciarSesion(String correo, String contrasena) {
        for (Usuario u : usuarios) {
            if (u.validar(correo, contrasena)) {
                return u;
            }
        }
        return null;
    }

    public boolean existeCorreo(String correo) {
        for (Usuario u : usuarios) {
            if (u.getCorreo().equalsIgnoreCase(correo)) {
                return true;
            }
        }
        return false;
    }

    public boolean registrar(String nombre, String correo, String contrasena) {
        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            return false;
        }
        if (existeCorreo(correo)) {
            return false;
        }
        usuarios.add(new Usuario(nombre, correo, contrasena, false));
        return true;
    }

    public int cantidadUsuarios() {
        return usuarios.size();
    }
}

