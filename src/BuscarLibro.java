import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.mongodb.client.FindIterable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class BuscarLibro extends JFrame {
    private JPanel BuscarPanel;
    private JTextField tituloTxt;
    private JButton buscarButton;
    private JButton volverBtn;
    private JLabel autorLabel;
    private JLabel generoLabel;
    private JLabel paginasLabel;
    private JLabel isbnLabel;
    private JLabel tituloPortadaLabel;
    private JLabel portadaLabel;

    public BuscarLibro() {

        setTitle("Buscar Libro");
        setContentPane(BuscarPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setMinimumSize(new Dimension(800, 600));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        autorLabel.setVisible(false);
        generoLabel.setVisible(false);
        paginasLabel.setVisible(false);
        isbnLabel.setVisible(false);
        tituloPortadaLabel.setVisible(false);
        portadaLabel.setVisible(false);

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titulo = tituloTxt.getText();

                if (titulo.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese un título");
                    return;
                }

                try (MongoClient mongoClient = MongoClients.create("mongodb+srv://ithancamacho:ithancamacho@biblioteca.psx9hpj.mongodb.net/?retryWrites=true&w=majority&appName=Biblioteca")) {
                    MongoDatabase database = mongoClient.getDatabase("BibliotecaDigital");
                    MongoCollection<Document> collection = database.getCollection("Libros");

                    Document filtro = new Document("titulo", titulo);
                    FindIterable<Document> resultados = collection.find(filtro);

                    if (resultados.first() != null) {
                        Document libroEncontrado = resultados.first();

                        autorLabel.setText("Autor: " + libroEncontrado.getString("autor"));
                        generoLabel.setText("Género: " + libroEncontrado.getString("genero"));
                        paginasLabel.setText("Número de Páginas: " + libroEncontrado.getInteger("numPaginas"));
                        isbnLabel.setText("ISBN: " + libroEncontrado.getString("ISBN"));

                        String portadaBase64 = libroEncontrado.getString("portada");
                        byte[] portadaData = Base64.getDecoder().decode(portadaBase64);
                        ByteArrayInputStream bais = new ByteArrayInputStream(portadaData);
                        BufferedImage portadaImage = ImageIO.read(bais);

                        ImageIcon icon = new ImageIcon(portadaImage.getScaledInstance(100, 150, Image.SCALE_SMOOTH));
                        tituloPortadaLabel.setText("Portada:");
                        portadaLabel.setIcon(icon);

                        autorLabel.setVisible(true);
                        generoLabel.setVisible(true);
                        paginasLabel.setVisible(true);
                        isbnLabel.setVisible(true);
                        tituloPortadaLabel.setVisible(true);
                        portadaLabel.setVisible(true);

                    } else {
                        JOptionPane.showMessageDialog(null, "Libro no encontrado");
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        volverBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new GestionLibros();

            }
        });
    }

    public static void main(String[] args) {
        new BuscarLibro();
    }
}
