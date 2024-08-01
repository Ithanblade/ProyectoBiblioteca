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
        private JComboBox comboBox1;
        private JTextField linkTxt;
        private BufferedImage portadaImage;

        public CrearLibro() {
            setTitle("Crear Nuevo Libro");
            setContentPane(crearPanel);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(800, 600);
            setMinimumSize(new Dimension(800, 600));
            pack();
            setLocationRelativeTo(null);
            setVisible(true);

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

            crearButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (tituloTxt.getText().isEmpty() || autorTxt.getText().isEmpty() || comboBox1.getSelectedItem().equals("Seleccione un genero...") || paginasTxt.getText().isEmpty() || portadaImage == null || portadaImage.getWidth() == 0 || portadaImage.getHeight() == 0 ||linkTxt.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Por favor, llene todos los campos y seleccione una imagen");
                        return;
                    }

                    try {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(portadaImage, "jpg", baos);
                        String portadaBase64 = Base64.getEncoder().encodeToString(baos.toByteArray());

                        // Crear objeto Libro
                        Libro libro = new Libro(tituloTxt.getText(),autorTxt.getText(),comboBox1.getSelectedItem().toString(),Integer.parseInt(paginasTxt.getText()),"",portadaBase64,linkTxt.getText());

                        // Insertar libro en la base de datos
                        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://ithancamacho:ithancamacho@biblioteca.psx9hpj.mongodb.net/?retryWrites=true&w=majority&appName=Biblioteca")) {
                            MongoDatabase database = mongoClient.getDatabase("BibliotecaDigital");
                            MongoCollection<Document> collection = database.getCollection("Libros");

                            Document libroDoc = new Document("titulo", libro.titulo)
                                    .append("autor", libro.autor)
                                    .append("genero", libro.genero)
                                    .append("numPaginas", libro.numPaginas)
                                    .append("ISBN", libro.ISBN)
                                    .append("portada", libro.portada)
                                    .append("linkDescarga", libro.link);

                            collection.insertOne(libroDoc);
                        }

                        JOptionPane.showMessageDialog(null, "Libro creado correctamente");

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });

            volverButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                    new GestionLibros();
                }
            });
        }
    }


