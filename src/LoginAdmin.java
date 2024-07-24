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
    }
}
