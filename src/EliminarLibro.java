import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EliminarLibro extends JFrame {
    private JPanel eliminarPanel;
    private JButton eliminarButton;
    private JTextField tituloTxt;
    private JButton volverBtn;

    public EliminarLibro() {

        setTitle("Eliminar Usuarios");
        setContentPane(eliminarPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setMinimumSize(new Dimension(400, 300));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (tituloTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese un titulo de algun Lirbo");
                    return;
                }

                int confirmar = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este libro?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                if (confirmar == JOptionPane.YES_OPTION) {
                    try (MongoClient mongoClient = MongoClients.create("mongodb+srv://ithancamacho:ithancamacho@biblioteca.psx9hpj.mongodb.net/?retryWrites=true&w=majority&appName=Biblioteca")) {
                        MongoDatabase database = mongoClient.getDatabase("BibliotecaDigital");
                        MongoCollection<Document> collection = database.getCollection("Libros");

                        Document filtro = new Document("titulo", tituloTxt.getText());
                        DeleteResult resultado = collection.deleteOne(filtro);

                        if (resultado.getDeletedCount() > 0) {
                            JOptionPane.showMessageDialog(null, "Libro eliminado correctamente");
                        } else {
                            JOptionPane.showMessageDialog(null, "Libro no encontrado");
                        }
                    }
                }

            }
        });
        volverBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionLibros();
                setVisible(false);
            }
        });
    }
}
