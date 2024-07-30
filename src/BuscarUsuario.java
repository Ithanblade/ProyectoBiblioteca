import javax.swing.*;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.mongodb.client.FindIterable;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BuscarUsuario extends JFrame{
    private JTextField usuarioTxt;
    private JButton buscarButton;
    private JPanel Buscar;
    private JButton volverBtn;

    public BuscarUsuario() {

        setTitle("Gestion Usuarios");
        setContentPane(Buscar);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setMinimumSize(new Dimension(400, 300));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (usuarioTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese un nombre de usuario");
                    return;
                }

                try (MongoClient mongoClient = MongoClients.create("mongodb+srv://ithancamacho:ithancamacho@biblioteca.psx9hpj.mongodb.net/?retryWrites=true&w=majority&appName=Biblioteca")) {
                    MongoDatabase database = mongoClient.getDatabase("BibliotecaDigital");
                    MongoCollection<Document> collection = database.getCollection("Usuarios");

                    Document filtro = new Document("usuario", usuarioTxt.getText());
                    FindIterable<Document> resultados = collection.find(filtro);

                    if (resultados.first() != null) {
                        Document usuarioEncontrado = resultados.first();
                        String Datos = "Usuario: " + usuarioEncontrado.getString("usuario") + "\n"
                                + "Correo: " + usuarioEncontrado.getString("correo") + "\n"
                                + "Preferencia: " + usuarioEncontrado.getString("preferencia");

                        JOptionPane.showMessageDialog(null, Datos, "Datos del Usuario", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Usuario no encontrado");
                    }
                }
            }
        });

        volverBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionUsuarios();
                setVisible(false);
            }
        });
    }
}
