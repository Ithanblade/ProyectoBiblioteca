import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class EdicionUsuario extends JFrame {
    private JPanel Edicion;
    private JButton editarButton;
    private JTextField contraTxt;
    private JTextField correoTxt;
    private JComboBox<String> comboBox1; // Especificar tipo de datos para JComboBox
    private JTextField usuarioTxt;
    private JButton volverButton;

    public EdicionUsuario() {
        setTitle("Edición Usuario");
        setContentPane(Edicion);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setMinimumSize(new Dimension(600, 400));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Acción del botón Editar
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Verifica si el campo de usuario no está vacío
                String usuario = usuarioTxt.getText();
                if (usuario.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese un nombre de usuario");
                    return;
                }

                try (MongoClient mongoClient = MongoClients.create("mongodb+srv://ithancamacho:ithancamacho@biblioteca.psx9hpj.mongodb.net/?retryWrites=true&w=majority&appName=Biblioteca")) {
                    MongoDatabase database = mongoClient.getDatabase("BibliotecaDigital");
                    MongoCollection<Document> collection = database.getCollection("Usuarios");

                    // Crea un filtro para encontrar el usuario
                    Document filtro = new Document("usuario", usuario);
                    Document actualizacion = new Document();

                    // Agrega campos a actualizar si el usuario ingresa algo
                    if (!correoTxt.getText().isEmpty()) {
                        actualizacion.append("correo", correoTxt.getText());
                    }

                    if (comboBox1.getSelectedIndex() != 0) { // Si no selecciona ninguna categoria
                        actualizacion.append("preferencia", comboBox1.getSelectedItem().toString());
                    }

                    if (!contraTxt.getText().isEmpty()) {
                        String hashedContrasena = Encriptacion.generateHash(contraTxt.getText());
                        actualizacion.append("contraseña", hashedContrasena);
                    }

                    // Realiza la actualización si hay datos para modificar
                    if (!actualizacion.isEmpty()) {
                        Document setQuery = new Document("$set", actualizacion);
                        collection.updateOne(filtro, setQuery);
                        JOptionPane.showMessageDialog(null, "Datos actualizados correctamente");
                    } else {
                        JOptionPane.showMessageDialog(null, "No se han realizado cambios");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al actualizar datos");
                }
            }
        });

        // Acción del botón Volver
        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionUsuarios();
                setVisible(false);
            }
        });
    }
}
