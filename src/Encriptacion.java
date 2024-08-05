import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// Clase para manejar la encriptación usando SHA-256
public class Encriptacion {

    // Método para generar un hash SHA-256 a partir de una cadena de entrada
    public static String generateHash(String input) {
        try {
            // Obtiene una instancia del algoritmo de encriptación SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Genera el hash de la entrada
            byte[] encodedhash = digest.digest(input.getBytes());
            // Convierte el hash a una representación hexadecimal
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            // Lanza una excepción en caso de que el algoritmo no esté disponible
            throw new RuntimeException(e);
        }
    }

    // Método privado para convertir un array de bytes a una cadena hexadecimal
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            // Convierte cada byte a una cadena hexadecimal
            String hex = Integer.toHexString(0xff & b);
            // Asegura que cada byte se represente con dos dígitos hexadecimales
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
