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

        buscarPanel = new JPanel(new BorderLayout());
        buscarPanel.setBackground(new Color(0xF2E8D5));
        setContentPane(buscarPanel);

        librosTable = new JTable();
        scrollPane = new JScrollPane(librosTable);
        buscarPanel.add(scrollPane, BorderLayout.CENTER);

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

        librosTable.setModel(tableModel);
        librosTable.setRowHeight(200);

        TableColumnModel columnModel = librosTable.getColumnModel();
        int[] columnWidths = {250, 200, 150, 150, 150, 250};
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(columnWidths[i]);
        }
        librosTable.getColumn("Portada").setCellRenderer(new ImageRenderer());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(new Color(0xF2E8D5));
        buscarPanel.add(topPanel, BorderLayout.NORTH);

        JLabel titleLabel = new JLabel("BIENVENIDO A LibroConnect", JLabel.CENTER);
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0x004D00));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        JLabel mensajeLabel = new JLabel("BUSCAR LIBROS", JLabel.CENTER);
        mensajeLabel.setFont(new Font("Montserrat", Font.BOLD, 17));
        mensajeLabel.setForeground(new Color(0x004D00));
        topPanel.add(mensajeLabel, BorderLayout.CENTER);

        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        controlsPanel.setBackground(new Color(0xF2E8D5));
        topPanel.add(controlsPanel, BorderLayout.SOUTH);

        JPanel tituloPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tituloPanel.setBackground(new Color(0xF2E8D5));
        JLabel tituloLabel = new JLabel("Título:");
        tituloLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        tituloLabel.setForeground(new Color(0x004D00));
        tituloTxt = new JTextField(30);
        tituloTxt.setFont(new Font("Roboto", Font.PLAIN, 14));
        tituloTxt.setForeground(new Color(0x333333));
        tituloPanel.add(tituloLabel);
        tituloPanel.add(tituloTxt);
        controlsPanel.add(tituloPanel);

        JPanel autorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        autorPanel.setBackground(new Color(0xF2E8D5));
        JLabel autorLabel = new JLabel("Autor:");
        autorLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        autorLabel.setForeground(new Color(0x004D00));
        autorTxt = new JTextField(30);
        autorTxt.setFont(new Font("Roboto", Font.PLAIN, 14));
        autorTxt.setForeground(new Color(0x333333));
        autorPanel.add(autorLabel);
        autorPanel.add(autorTxt);
        controlsPanel.add(autorPanel);

        JPanel generoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        generoPanel.setBackground(new Color(0xF2E8D5));
        JLabel generoLabel = new JLabel("Género:");
        generoLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        generoLabel.setForeground(new Color(0x004D00));
        generoComboBox = new JComboBox<>(new String[]{"Todos", "Novela", "Ciencia Ficción", "Fantasía", "Misterio", "Romance", "Terror", "Aventura"});
        generoComboBox.setFont(new Font("Roboto", Font.PLAIN, 14));
        generoComboBox.setForeground(new Color(0x333333));
        generoPanel.add(generoLabel);
        generoPanel.add(generoComboBox);
        controlsPanel.add(generoPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0xF2E8D5));
        buttonPanel.setLayout(new FlowLayout());
        buscarButton = new JButton("Buscar");
        buscarButton.setFont(new Font("Open Sans", Font.BOLD, 14));
        buscarButton.setForeground(new Color(0x333333));
        buscarButton.setBackground(new Color(0x42743F));
        volverBtn = new JButton("Volver");
        volverBtn.setFont(new Font("Open Sans", Font.BOLD, 14));
        volverBtn.setForeground(new Color(0x333333));
        volverBtn.setBackground(new Color(0x42743F));
        buttonPanel.add(buscarButton);
        buttonPanel.add(volverBtn);
        controlsPanel.add(buttonPanel);

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titulo = tituloTxt.getText();
                String autor = autorTxt.getText();
                String genero = (String) generoComboBox.getSelectedItem();
                cargarLibros(titulo, autor, genero);
            }
        });

        volverBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new GestionLibros();
            }
        });

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
