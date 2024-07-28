import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.mongodb.client.result.DeleteResult;


public class EliminarUsuario extends JFrame {
    private JTextField usuarioTxt;
    private JButton eliminarButton;
    private JPanel Eliminar;

    public EliminarUsuario() {

        setTitle("Eliminar Usuarios");
        setContentPane(Eliminar);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setMinimumSize(new Dimension(400, 300));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if (usuarioTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese un nombre de usuario");
                    return;
                }

                int confirmar = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este usuario?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                if (confirmar == JOptionPane.YES_OPTION) {
                    try (MongoClient mongoClient = MongoClients.create("mongodb+srv://ithancamacho:ithancamacho@biblioteca.psx9hpj.mongodb.net/?retryWrites=true&w=majority&appName=Biblioteca")) {
                        MongoDatabase database = mongoClient.getDatabase("BibliotecaDigital");
                        MongoCollection<Document> collection = database.getCollection("Usuarios");

                        Document filtro = new Document("usuario", usuarioTxt.getText());
                        DeleteResult resultado = collection.deleteOne(filtro);

                        if (resultado.getDeletedCount() > 0) {
                            JOptionPane.showMessageDialog(null, "Usuario eliminado correctamente");
                        } else {
                            JOptionPane.showMessageDialog(null, "Usuario no encontrado");
                        }
                    }
                }
            }
        });

    }
}
