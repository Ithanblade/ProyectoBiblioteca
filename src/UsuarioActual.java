public class UsuarioActual {
    // Atributo estático para almacenar el nombre de usuario actual
    private static String nombreUsuario;

    // Método estático para obtener el nombre de usuario actual
    public static String getNombreUsuario() {
        return nombreUsuario;
    }

    // Método estático para establecer el nombre de usuario actual
    public static void setNombreUsuario(String nombre) {
        nombreUsuario = nombre;
    }
}
