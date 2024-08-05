import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class CrearLibro extends JFrame {
    private JPanel crearPanel;
    private JButton crearButton;
    private JTextField tituloTxt;
    private JTextField paginasTxt;
    private JTextField autorTxt;
    private JButton seleccionarImagenButton;
    private JLabel portadaLabel;
    private JButton volverButton;
    private JComboBox<String> comboBox1;
    private JTextField linkTxt;
    private BufferedImage portadaImage;

    public CrearLibro() {
        // Configura la ventana principal
        setTitle("Crear Nuevo Libro");
        setContentPane(crearPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setMinimumSize(new Dimension(800, 600));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Acción del botón para seleccionar una imagen
        seleccionarImagenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(CrearLibro.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File portadaFile = fileChooser.getSelectedFile();
                    try {
                        portadaImage = ImageIO.read(portadaFile);
                        ImageIcon icon = new ImageIcon(portadaImage.getScaledInstance(150, 200, Image.SCALE_SMOOTH));
                        portadaLabel.setIcon(icon);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });

        // Acción del botón Crear
        crearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Verifica que todos los campos estén llenos y que se haya seleccionado una imagen
                if (tituloTxt.getText().isEmpty() || autorTxt.getText().isEmpty() ||
                        comboBox1.getSelectedItem().equals("Seleccione un genero...") || paginasTxt.getText().isEmpty() ||
                        portadaImage == null || portadaImage.getWidth() == 0 || portadaImage.getHeight() == 0 ||
                        linkTxt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, llene todos los campos y seleccione una imagen");
                    return;
                }

                try {
                    // Convierte la imagen a Base64
                    ByteArrayOutputStream baos = new ByteArrayOutputStream(); // Mediante esta clase se puede escribir datos en un array de bytes
                    ImageIO.write(portadaImage, "jpg", baos); // Transforma la imagen en bytes en formato JPEG y lo ingresa en baos
                    String portadaBase64 = Base64.getEncoder().encodeToString(baos.toByteArray()); //Transforma el array de bytes en una cadena de texto codificada en Base64 para su mejor almacenamiento

                    // Crea un objeto Libro y lo convierte a un documento
                    Libro libro = new Libro(tituloTxt.getText(), autorTxt.getText(),comboBox1.getSelectedItem().toString(), Integer.parseInt(paginasTxt.getText()),"", portadaBase64, linkTxt.getText());

                    Document libroDoc = new Document("titulo", libro.getTitulo())
                            .append("autor", libro.getAutor())
                            .append("genero", libro.getGenero())
                            .append("numPaginas", libro.getNumPaginas())
                            .append("ISBN", libro.getISBN())
                            .append("portada", libro.getPortada())
                            .append("linkDescarga", libro.getLink());

                    // Inserta el documento en la colección de libros
                    try (MongoClient mongoClient = MongoClients.create("mongodb+srv://ithancamacho:ithancamacho@biblioteca.psx9hpj.mongodb.net/?retryWrites=true&w=majority&appName=Biblioteca")) {
                        MongoDatabase database = mongoClient.getDatabase("BibliotecaDigital");
                        MongoCollection<Document> collection = database.getCollection("Libros");
                        collection.insertOne(libroDoc);
                    }

                    JOptionPane.showMessageDialog(null, "Libro creado correctamente");
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        // Acción del botón "Volver"
        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new GestionLibros();
            }
        });
    }
}
