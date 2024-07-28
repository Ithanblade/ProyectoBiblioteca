import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import com.mongodb.client.FindIterable;

public class EdicionUsuario extends JFrame{
    private JPanel Edicion;
    private JButton editarButton;
    private JTextField contraTxt;
    private JTextField correoTxt;
    private JComboBox comboBox1;
    private JTextField usuarioTxt;

    public EdicionUsuario() {

        setTitle("Edicion Usuario");
        setContentPane(Edicion);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setMinimumSize(new Dimension(600, 400));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try (MongoClient mongoClient = MongoClients.create("mongodb+srv://ithancamacho:ithancamacho@biblioteca.psx9hpj.mongodb.net/?retryWrites=true&w=majority&appName=Biblioteca")) {
                    MongoDatabase database = mongoClient.getDatabase("BibliotecaDigital");
                    MongoCollection<Document> collection = database.getCollection("Usuarios");

                    Document filtro = new Document("usuario", usuarioTxt.getText());
                    Document actualizacion = new Document();

                    if (!correoTxt.getText().isEmpty()) {
                        actualizacion.append("correo", correoTxt.getText());
                    }

                    if (!comboBox1.getSelectedItem().equals("Seleccione una preferencia...")) {
                        actualizacion.append("preferencia", comboBox1.getSelectedItem().toString());
                    }

                    if (!contraTxt.getText().isEmpty()) {
                        String hashedContrasena = Encriptacion.generateHash(contraTxt.getText());
                        actualizacion.append("contraseña", hashedContrasena);
                    }

                    if (!actualizacion.isEmpty()) {
                        Document setQuery = new Document("$set", actualizacion);
                        collection.updateOne(filtro, setQuery);
                        JOptionPane.showMessageDialog(null, "Datos actualizados correctamente");
                    } else {
                        JOptionPane.showMessageDialog(null, "No se han realizado cambios");
                    }
                }

            }
        });
    }
}
