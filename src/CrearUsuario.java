import com.mongodb.client.*;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CrearUsuario extends JFrame {
    private JButton crearButton;
    private JTextField usuarioTxt;
    private JTextField contraTxt;
    private JTextField correoTxt;
    private JComboBox<String> comboBox1;
    private JTextField confirmarTxt;
    private JPanel crearPanel;
    private JButton volverBtn;

    public CrearUsuario() {
        setTitle("Crear Usuario");
        setContentPane(crearPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setMinimumSize(new Dimension(600, 400));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Acción del botón Crear
        crearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Verifica que todos los campos estén llenos
                if (usuarioTxt.getText().isEmpty() || contraTxt.getText().isEmpty() ||
                        correoTxt.getText().isEmpty() || comboBox1.getSelectedItem().toString().equals("Seleccione una preferencia...")) {
                    JOptionPane.showMessageDialog(null, "Por favor, llene todos los campos");
                    return;
                }

                // Verifica que las contraseñas coincidan
                if (!contraTxt.getText().equals(confirmarTxt.getText())) {
                    JOptionPane.showMessageDialog(null, "Las contraseñas deben ser iguales");
                    return;
                }

                // Verifica que el correo electrónico tenga un formato válido
                if (!correoTxt.getText().contains("@")) {
                    JOptionPane.showMessageDialog(null, "Ingrese un correo electrónico válido");
                    return;
                }

                // Verifica que el correo tenga una de las terminaciones válidas
                String[] terminaciones = {".com", ".ec", ".gob", ".org", ".net"};
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

                try (MongoClient mongoClient = MongoClients.create("mongodb+srv://ithancamacho:ithancamacho@biblioteca.psx9hpj.mongodb.net/?retryWrites=true&w=majority&appName=Biblioteca")) {
                    MongoDatabase database = mongoClient.getDatabase("BibliotecaDigital");
                    MongoCollection<Document> collection = database.getCollection("Usuarios");

                    // Verifica si el usuario o correo ya existen
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

                    // Crea un nuevo usuario y lo guarda en la base de datos
                    String hashedPassword = Encriptacion.generateHash(contraTxt.getText());
                    Usuario nuevoUsuario = new Usuario(usuarioTxt.getText(), correoTxt.getText(), comboBox1.getSelectedItem().toString(), hashedPassword);

                    Document documento = new Document("usuario", nuevoUsuario.getUsuario())
                            .append("correo", nuevoUsuario.getCorreo())
                            .append("preferencia", nuevoUsuario.getPreferencia())
                            .append("contraseña", nuevoUsuario.getContrasena());

                    collection.insertOne(documento);
                    JOptionPane.showMessageDialog(null, "Usuario creado correctamente");
                    System.out.println("Registro creado correctamente");
                    new GestionUsuarios(); // Muestra la ventana de gestión de usuarios
                    setVisible(false);
                }
            }
        });

        // Acción del botón Volver
        volverBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionUsuarios();
                setVisible(false);
            }
        });
    }
}
