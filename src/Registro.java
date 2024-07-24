import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

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
    private JComboBox comboBox1;
    private JTextField confirmarTxt;

    public Registro() {

        setTitle("Login");
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

                if (usuarioTxt.getText().equals("") || contraTxt.getText().equals("") || correoTxt.getText().equals("")|| comboBox1.getSelectedItem().toString().equals("Seleccione una preferencia...")) {

                    JOptionPane.showMessageDialog(null, "Por favor, llene todos los campos");
                    return;
                }

                if (!contraTxt.getText().equals(confirmarTxt.getText())) {
                    JOptionPane.showMessageDialog(null, "Las contraseñas deben ser iguales");
                    return;
                }


                Usuario NuevoUsuario = new Usuario(usuarioTxt.getText(), correoTxt.getText(), comboBox1.getSelectedItem().toString(), contraTxt.getText());


                try (MongoClient mongoClient = MongoClients.create("mongodb+srv://ithancamacho:epn123@biblioteca.psx9hpj.mongodb.net/?retryWrites=true&w=majority&appName=Biblioteca")) {
                    MongoDatabase database = mongoClient.getDatabase("BibliotecaDigital");
                    MongoCollection<Document> collection = database.getCollection("Usuarios");
                    Document documento = new Document("usuario", NuevoUsuario.getUsuario())
                            .append("correo", NuevoUsuario.getCorreo())
                            .append("preferencia", NuevoUsuario.getPreferencia())
                            .append("contraseña", NuevoUsuario.getContrasena());

                    collection.insertOne(documento);
                    JOptionPane.showMessageDialog(null, "Registro creado correctamente");
                    new Login();
                    setVisible(false);
                }

            }
        });
    }
}
