import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.mongodb.client.FindIterable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Registro extends JFrame {
    private JPanel RegistroPanel;
    private JButton registrarmeButton;
    private JTextField usuarioTxt;
    private JTextField contraTxt;
    private JTextField correoTxt;
    private JComboBox<String> comboBox1;
    private JTextField confirmarTxt;
    private JButton volverButton;

    // Constructor de la clase Registro
    public Registro() {
        setTitle("Registro");
        setContentPane(RegistroPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setMinimumSize(new Dimension(600, 400));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Acción del botón Registrarme
        registrarmeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Cadena para las terminaciones del correo validas
                String[] terminaciones = {".com", ".ec", ".gob", ".org", ".net"};

                // Validación de campos vacíos
                if (usuarioTxt.getText().isEmpty() || contraTxt.getText().isEmpty() || correoTxt.getText().isEmpty() || comboBox1.getSelectedItem().toString().equals("Seleccione una preferencia...")) {
                    JOptionPane.showMessageDialog(null, "Por favor, llene todos los campos");
                    return;
                }

                // Validación de coincidencia de contraseñas
                if (!contraTxt.getText().equals(confirmarTxt.getText())) {
                    JOptionPane.showMessageDialog(null, "Las contraseñas deben ser iguales");
                    return;
                }

                // Validación de correo electrónico
                if (!correoTxt.getText().contains("@")) {
                    JOptionPane.showMessageDialog(null, "Ingrese un correo electrónico válido");
                    return;
                }

                // Validación de terminación de correo electrónico
                boolean correoValido = false;
                for (String extension : terminaciones) {
                    if (correoTxt.getText().endsWith(extension)) {
                        correoValido = true;
                        break;
                    }
                }

                if (!correoValido) {
                    JOptionPane.showMessageDialog(null, "Ingrese un correo válido");
                    return;
                }

                // Conexión a la base de datos
                try (MongoClient mongoClient = MongoClients.create("mongodb+srv://ithancamacho:ithancamacho@biblioteca.psx9hpj.mongodb.net/?retryWrites=true&w=majority&appName=Biblioteca")) {
                    MongoDatabase database = mongoClient.getDatabase("BibliotecaDigital");
                    MongoCollection<Document> collection = database.getCollection("Usuarios");

                    // Verificación del usuario y contraseña, para ver si ya existen
                    FindIterable<Document> documentos = collection.find();
                    for (Document documento : documentos) {
                        String usuario = documento.getString("usuario");
                        String correo = documento.getString("correo");

                        if (usuarioTxt.getText().equals(usuario)) {
                            JOptionPane.showMessageDialog(null, "El usuario ya existe");
                            return;
                        }

                        if (correoTxt.getText().equals(correo)) {
                            JOptionPane.showMessageDialog(null, "El correo ya existe");
                            return;
                        }
                    }

                    // Encriptación de la contraseña y creación de un nuevo usuario
                    String hashedPassword = Encriptacion.generateHash(contraTxt.getText());
                    Usuario NuevoUsuario = new Usuario(usuarioTxt.getText(), correoTxt.getText(), comboBox1.getSelectedItem().toString(), hashedPassword);

                    // Creación del documento a insertar en la colección de MongoDB
                    Document documento = new Document("usuario", NuevoUsuario.getUsuario())
                            .append("correo", NuevoUsuario.getCorreo())
                            .append("preferencia", NuevoUsuario.getPreferencia())
                            .append("contraseña", NuevoUsuario.getContrasena());

                    // Inserción del documento en la colección de MongoDB
                    collection.insertOne(documento);
                    JOptionPane.showMessageDialog(null, "Registro creado correctamente");

                    //Si pasa todas las validaciones abre el Login
                    new Login();
                    setVisible(false);
                }
            }
        });

        // Acción del botón "Volver"
        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login(); // Ir a la ventana de Login
                setVisible(false); // Cerrar la ventana actual
            }
        });
    }
}
