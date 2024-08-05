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

public class Login extends JFrame {
    private JPanel mainPanel;
    private JButton loginButton;
    private JTextField usuarioTxt;
    private JTextField contraTxt;
    private JButton ingresarComoAdminButton;
    private JButton RegistrarBtn;

    // Constructor de la clase Login
    public Login() {
        setTitle("Login");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setMinimumSize(new Dimension(600, 400));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Acción del botón Ingresar como Admin
        ingresarComoAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginAdmin();
                setVisible(false);
            }
        });

        // Acción del botón "Login"
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Conexión a la base de datos de Mongo
                try (MongoClient mongoClient = MongoClients.create("mongodb+srv://ithancamacho:ithancamacho@biblioteca.psx9hpj.mongodb.net/?retryWrites=true&w=majority&appName=Biblioteca")) {
                    MongoDatabase database = mongoClient.getDatabase("BibliotecaDigital");
                    MongoCollection<Document> collection = database.getCollection("Usuarios");

                    // Búsqueda de documentos en la colección Usuarios
                    FindIterable<Document> documentos = collection.find();

                    boolean acceso = false;

                    // Verificación de credenciales
                    for (Document documento : documentos) {
                        String usuario = documento.getString("usuario");
                        String contrasena = documento.getString("contraseña");

                        // Si las credenciales coinciden, se concede el acceso
                        if (usuarioTxt.getText().equals(usuario) && Encriptacion.generateHash(contraTxt.getText()).equals(contrasena)) {
                            JOptionPane.showMessageDialog(null, "Acceso Exitoso");

                            // Establece el usuario actual
                            UsuarioActual.setNombreUsuario(usuario);

                            // Abre la ventana del buscador si las credenciales coinciden
                            new Buscador();
                            setVisible(false);
                            acceso = true;
                            break;
                        }
                    }

                    // Si las credenciales no coinciden, muestra un mensaje de error
                    if (!acceso) {
                        JOptionPane.showMessageDialog(null, "Las Credenciales no coinciden o no existen");
                    }
                }
            }
        });

        // Acción del botón "Registrar"
        RegistrarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Registro();
                setVisible(false);
            }
        });
    }
}
