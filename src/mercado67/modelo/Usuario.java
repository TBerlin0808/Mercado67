
package mercado67.modelo;


public class Usuario {
    private final String nombre;
    private final String correo;
    private final String contraseña;
    private final boolean admin;
    private boolean activo;

    public Usuario(String nombre, String correo, String contraseña, boolean admin) {
        this(nombre, correo, contraseña, admin, true);
    }

    public Usuario(String nombre, String correo, String contraseña, boolean admin, boolean activo) {
        this.nombre = nombre;
        this.correo = correo;
        this.contraseña = contraseña;
        this.admin = admin;
        this.activo = activo;
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

    public String getContraseña() {
        return contraseña;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean validar (String correo, String contraseña){
        
        return this.correo.equalsIgnoreCase(correo) && this.contraseña.equals(contraseña);
        
    }
    }
