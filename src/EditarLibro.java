import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

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

public class EditarLibro extends JFrame {
    private JButton editarButton;
    private JTextField generoTxt;
    private JTextField tituloTxt;
    private JTextField buscarTxt;
    private JButton seleccionarImagenButton;
    private JLabel portadaLabel;
    private JTextField autorTxt;
    private JTextField paginasTxt;
    private JPanel editarPanel;
    private JButton volverBtn;
    private JTextField linkTxt;
    private JComboBox<String> comboBox1; // Debe especificar el tipo de datos para el JComboBox
    private BufferedImage portadaImage;

    public EditarLibro() {
        setTitle("Editar Libro");
        setContentPane(editarPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setMinimumSize(new Dimension(800, 600));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Acción del botón Seleccionar Imagen
        seleccionarImagenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(EditarLibro.this);
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

        // Acción del botón "Editar"
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tituloBuscar = buscarTxt.getText();
                if (tituloBuscar.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese el título del libro a modificar");
                    return;
                }

                try (MongoClient mongoClient = MongoClients.create("mongodb+srv://ithancamacho:ithancamacho@biblioteca.psx9hpj.mongodb.net/?retryWrites=true&w=majority&appName=Biblioteca")) {
                    MongoDatabase database = mongoClient.getDatabase("BibliotecaDigital");
                    MongoCollection<Document> collection = database.getCollection("Libros");

                    Document filtro = new Document("titulo", tituloBuscar);
                    Document libroEncontrado = collection.find(filtro).first();

                    if (libroEncontrado != null) {
                        Document updates = new Document();

                        // Agregar campos actualizados
                        if (!tituloTxt.getText().isEmpty()) {
                            updates.append("titulo", tituloTxt.getText());
                        }
                        if (!autorTxt.getText().isEmpty()) {
                            updates.append("autor", autorTxt.getText());
                        }
                        if (comboBox1.getSelectedIndex() != 0) {
                            updates.append("genero", comboBox1.getSelectedItem().toString());
                        }
                        if (!paginasTxt.getText().isEmpty()) {
                            updates.append("numPaginas", Integer.parseInt(paginasTxt.getText()));
                        }
                        if (!linkTxt.getText().isEmpty()) {
                            updates.append("linkDescarga", linkTxt.getText());
                        }

                        // Manejo de la imagen de portada
                        if (portadaImage != null) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ImageIO.write(portadaImage, "jpg", baos);
                            String portadaBase64 = Base64.getEncoder().encodeToString(baos.toByteArray());
                            updates.append("portada", portadaBase64);
                        }

                        // Actualización en la base de datos
                        if (!updates.isEmpty()) {
                            collection.updateOne(filtro, new Document("$set", updates));
                            JOptionPane.showMessageDialog(null, "Libro actualizado correctamente");
                        } else {
                            JOptionPane.showMessageDialog(null, "No se ha realizado ningún cambio");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Libro no encontrado");
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        // Acción del botón "Volver"
        volverBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionLibros();
                setVisible(false);
            }
        });
    }
}
