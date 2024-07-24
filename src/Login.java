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

public class Login extends JFrame{
    private JPanel mainPanel;
    private JButton loginButton;
    private JTextField usuarioTxt;
    private JTextField contraTxt;
    private JButton ingresarComoAdminButton;
    private JButton RegistrarBtn;


    public Login() {

        setTitle("Login");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setMinimumSize(new Dimension(600, 400));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        ingresarComoAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginAdmin();
                setVisible(false);
            }
        });


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try (MongoClient mongoClient = MongoClients.create("")) {

                    MongoDatabase database = mongoClient.getDatabase("BibliotecaDigital");
                    MongoCollection<Document> collection = database.getCollection("Usuarios");

                    FindIterable<Document> documentos = collection.find();

                    for (Document documento : documentos) {
                        String usuario = documento.getString("usuario");
                        String contrasena = documento.getString("correo");


                    }
                }

            }
        });

        RegistrarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Registro();
                setVisible(false);
            }
        });
    }
}
