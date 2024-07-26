import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.UpdateResult;

public class LoginAdmin extends JFrame {
    private JButton loginButton;
    private JTextField usuarioTxt;
    private JTextField contraTxt;
    private JButton ingresarComoUsuarioButton;
    private JPanel loginAdmin;

    public LoginAdmin() {

        setTitle("Login Administrador");
        setContentPane(loginAdmin);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setMinimumSize(new Dimension(600, 400));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        ingresarComoUsuarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new Login();
                setVisible(false);
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                try (MongoClient mongoClient = MongoClients.create("mongodb+srv://ithancamacho:ithancamacho@biblioteca.psx9hpj.mongodb.net/?retryWrites=true&w=majority&appName=Biblioteca")) {

                    MongoDatabase database = mongoClient.getDatabase("BibliotecaDigital");
                    MongoCollection<Document> collection = database.getCollection("Administrador");

                    FindIterable<Document> documentos = collection.find();

                    boolean acceso = false;

                    for (Document documento : documentos) {
                        String usuario = documento.getString("usuario");
                        String contrasena = documento.getString("contraseña");

                        if (usuarioTxt.getText().equals(usuario) && Encriptacion.generateHash(contraTxt.getText()).equals(contrasena)) {
                            JOptionPane.showMessageDialog(null, "Acceso Exitoso");
                            new MenuAdmin();
                            setVisible(false);
                            acceso = true;
                            break;
                        }
                    }

                    if (!acceso) {
                        JOptionPane.showMessageDialog(null, "Las Credenciales no coinciden o no existen");
                    }

                }

            }
        });
    }
}
