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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Registro extends JFrame {
    private JPanel RegistroPanel;
    private JButton registrarmeButton;
    private JTextField usuarioTxt;
    private JTextField contraTxt;
    private JTextField correoTxt;
    private JComboBox<String> comboBox1;
    private JTextField confirmarTxt;

    public Registro() {
        setTitle("Registro");
        setContentPane(RegistroPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setMinimumSize(new Dimension(600, 400));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        registrarmeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] terminaciones = {".com", ".ec", ".gob", ".org", ".net"};

                if (usuarioTxt.getText().isEmpty() || contraTxt.getText().isEmpty() || correoTxt.getText().isEmpty() || comboBox1.getSelectedItem().toString().equals("Seleccione una preferencia...")) {
                    JOptionPane.showMessageDialog(null, "Por favor, llene todos los campos");
                    return;
                }

                if (!contraTxt.getText().equals(confirmarTxt.getText())) {
                    JOptionPane.showMessageDialog(null, "Las contraseñas deben ser iguales");
                    return;
                }

                if (!correoTxt.getText().contains("@")) {
                    JOptionPane.showMessageDialog(null, "Ingrese un correo electrónico válido");
                    return;
                }

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

                    String hashedPassword = Encriptacion.generateHash(contraTxt.getText());
                    Usuario NuevoUsuario = new Usuario(usuarioTxt.getText(), correoTxt.getText(), comboBox1.getSelectedItem().toString(), hashedPassword);

                    Document documento = new Document("usuario", NuevoUsuario.getUsuario())
                            .append("correo", NuevoUsuario.getCorreo())
                            .append("preferencia", NuevoUsuario.getPreferencia())
                            .append("contraseña", NuevoUsuario.getContrasena());

                    collection.insertOne(documento);
                    JOptionPane.showMessageDialog(null, "Registro creado correctamente");
                    System.out.println("Registro creado correctamente");
                    new Login();
                    setVisible(false);
                }
            }
        });
    }

}
