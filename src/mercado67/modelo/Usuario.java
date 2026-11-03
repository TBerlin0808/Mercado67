
package mercado67.modelo;


public class Usuario {
    private final String nombre;
    private final String correo;
    private final String contraseña;
    private final boolean admin;

    public Usuario(String nombre, String correo, String contraseña, boolean admin) {
        this.nombre = nombre;
        this.correo = correo;
        this.contraseña = contraseña;
        this.admin = admin;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public boolean isAdmin() {
        return admin;
    }
    
    public boolean validar (String correo, String contraseña){
        
        return this.correo.equalsIgnoreCase(correo) && this.contraseña.equals(contraseña);
        
    }
    }

