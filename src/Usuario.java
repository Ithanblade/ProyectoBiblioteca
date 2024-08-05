public class Usuario {

    String usuario;
    String correo;
    String preferencia;
    String contrasena;

    // Constructor por defecto
    public Usuario() {
    }

    // Constructor con parámetros
    public Usuario(String usuario, String correo, String preferencia, String contrasena) {
        this.usuario = usuario;
        this.correo = correo;
        this.preferencia = preferencia;
        this.contrasena = contrasena;
    }

    // Getters y Setters para los atributos

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPreferencia() {
        return preferencia;
    }

    public void setPreferencia(String preferencia) {
        this.preferencia = preferencia;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
