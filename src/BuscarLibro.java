import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class BuscarLibro extends JFrame {
    private JPanel buscarPanel;
    private JTextField tituloTxt;
    private JTextField autorTxt;
    private JComboBox<String> generoComboBox;
    private JButton volverBtn;
    private JTable librosTable;
    private JScrollPane scrollPane;
    private JButton buscarButton;
    private DefaultTableModel tableModel;

    public BuscarLibro() {
        setTitle("Buscar Libro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);
        setMinimumSize(new Dimension(1200, 600));
        setLocationRelativeTo(null);

        // Inicializar el panel principal
        buscarPanel = new JPanel(new BorderLayout());
        setContentPane(buscarPanel);

        // Inicializar la tabla y el scroll pane
        librosTable = new JTable();
        scrollPane = new JScrollPane(librosTable);
        buscarPanel.add(scrollPane, BorderLayout.CENTER);

        // Inicializar el modelo de la tabla
        String[] columnNames = {"Título", "Autor", "Género", "Número de Páginas", "ISBN", "Portada"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 5) {
                    return ImageIcon.class;
                }
                return String.class;
            }
        };

        // Configurar la tabla
        librosTable.setModel(tableModel);
        librosTable.setRowHeight(200); // Establecer la altura de las filas

        // Ajustar el ancho de las columnas
        TableColumnModel columnModel = librosTable.getColumnModel();
        int[] columnWidths = {250, 200, 150, 150, 150, 250}; // Ancho de columnas
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(columnWidths[i]);
        }
        librosTable.getColumn("Portada").setCellRenderer(new ImageRenderer());

        // Panel superior para búsqueda y botones
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        buscarPanel.add(topPanel, BorderLayout.NORTH);

        // Título
        JLabel titleLabel = new JLabel("BUSCAR LIBROS", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel de controles
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS)); // Vertical layout
        topPanel.add(controlsPanel, BorderLayout.CENTER);

        // Campo de texto para título
        JPanel tituloPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel tituloLabel = new JLabel("Título:");
        tituloTxt = new JTextField(30); // Ajusta el tamaño del campo de texto
        tituloPanel.add(tituloLabel);
        tituloPanel.add(tituloTxt);
        controlsPanel.add(tituloPanel);

        // Campo de texto para autor
        JPanel autorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel autorLabel = new JLabel("Autor:");
        autorTxt = new JTextField(30); // Ajusta el tamaño del campo de texto
        autorPanel.add(autorLabel);
        autorPanel.add(autorTxt);
        controlsPanel.add(autorPanel);

        // ComboBox para género
        JPanel generoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel generoLabel = new JLabel("Género:");
        generoComboBox = new JComboBox<>(new String[]{"Todos", "Novela","Ciencia","Ficción","Fantasía","Misterio","Romance","Terror","Aventura"});
        generoPanel.add(generoLabel);
        generoPanel.add(generoComboBox);
        controlsPanel.add(generoPanel);

        // Botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buscarButton = new JButton("Buscar");
        volverBtn = new JButton("Volver");
        buttonPanel.add(buscarButton);
        buttonPanel.add(volverBtn);
        controlsPanel.add(buttonPanel);

        // Evento del botón "Buscar"
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titulo = tituloTxt.getText();
                String autor = autorTxt.getText();
                String genero = (String) generoComboBox.getSelectedItem();
                cargarLibros(titulo, autor, genero);
            }
        });

        // Evento del botón "Volver"
        volverBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new GestionLibros();
            }
        });

        // Cargar todos los libros al iniciar
        cargarLibros("", "", "Todos");

        setVisible(true);
    }

    private void cargarLibros(String tituloFiltro, String autorFiltro, String generoFiltro) {
        tableModel.setRowCount(0); // Limpiar la tabla

        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://ithancamacho:ithancamacho@biblioteca.psx9hpj.mongodb.net/?retryWrites=true&w=majority&appName=Biblioteca")) {
            MongoDatabase database = mongoClient.getDatabase("BibliotecaDigital");
            MongoCollection<Document> collection = database.getCollection("Libros");

            Document filtroDoc = new Document();
            if (!tituloFiltro.isEmpty()) {
                filtroDoc.append("titulo", new Document("$regex", tituloFiltro).append("$options", "i"));
            }
            if (!autorFiltro.isEmpty()) {
                filtroDoc.append("autor", new Document("$regex", autorFiltro).append("$options", "i"));
            }
            if (!generoFiltro.equals("Todos")) {
                filtroDoc.append("genero", generoFiltro);
            }

            FindIterable<Document> resultados = filtroDoc.isEmpty() ? collection.find() : collection.find(filtroDoc);

            for (Document libro : resultados) {
                String titulo = libro.getString("titulo");
                String autor = libro.getString("autor");
                String genero = libro.getString("genero");
                int numPaginas = libro.getInteger("numPaginas");
                String isbn = libro.getString("ISBN");

                String portadaBase64 = libro.getString("portada");
                byte[] portadaData = Base64.getDecoder().decode(portadaBase64);
                ByteArrayInputStream bais = new ByteArrayInputStream(portadaData);
                BufferedImage portadaImage = ImageIO.read(bais);

                ImageIcon icon = new ImageIcon(portadaImage.getScaledInstance(150, 200, Image.SCALE_SMOOTH));

                tableModel.addRow(new Object[]{titulo, autor, genero, numPaginas, isbn, icon});
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BuscarLibro());
    }

    // Renderer para mostrar imágenes en la tabla
    private static class ImageRenderer extends JLabel implements TableCellRenderer {
        public ImageRenderer() {
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof ImageIcon) {
                setIcon((ImageIcon) value);
            } else {
                setText((value != null) ? value.toString() : "");
                setIcon(null);
            }
            return this;
        }
    }
}
