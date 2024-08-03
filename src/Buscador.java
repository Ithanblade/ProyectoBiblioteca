import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

public class Buscador extends JFrame {
    private JPanel buscarPanel;
    private JTextField tituloTxt;
    private JTextField autorTxt;
    private JComboBox<String> generoComboBox;
    private JButton volverBtn;
    private JTable librosTable;
    private JScrollPane scrollPane;
    private JButton buscarButton;
    private DefaultTableModel tableModel;
    private JPanel buscadorPanel;

    public Buscador() {
        setTitle("Buscar Libro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setMinimumSize(new Dimension(1400, 800));
        setLocationRelativeTo(null);

        buscarPanel = new JPanel(new BorderLayout());
        setContentPane(buscarPanel);

        librosTable = new JTable();
        scrollPane = new JScrollPane(librosTable);
        buscarPanel.add(scrollPane, BorderLayout.CENTER);

        String[] columnNames = {"Título", "Autor", "Género", "Número de Páginas", "ISBN", "Portada", "Descargar"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 5) {
                    return ImageIcon.class;
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;  // Solo la columna de descarga es editable
            }
        };

        librosTable.setModel(tableModel);
        librosTable.setRowHeight(200);

        TableColumnModel columnModel = librosTable.getColumnModel();
        int[] columnWidths = {250, 200, 150, 150, 150, 250, 100};
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(columnWidths[i]);
        }
        librosTable.getColumn("Portada").setCellRenderer(new ImageRenderer());
        librosTable.getColumn("Descargar").setCellRenderer(new ButtonRenderer());
        librosTable.getColumn("Descargar").setCellEditor(new ButtonEditor(new JCheckBox()));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        buscarPanel.add(topPanel, BorderLayout.NORTH);

        JLabel titleLabel = new JLabel("BIENVENIDO A LibroConnect", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        JLabel mensajeLabel = new JLabel("BUSCAR LIBROS", JLabel.CENTER);
        mensajeLabel.setFont(new Font("Arial", Font.BOLD, 17));
        topPanel.add(mensajeLabel, BorderLayout.CENTER);

        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        topPanel.add(controlsPanel, BorderLayout.SOUTH);

        JPanel tituloPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel tituloLabel = new JLabel("Título:");
        tituloTxt = new JTextField(30);
        tituloPanel.add(tituloLabel);
        tituloPanel.add(tituloTxt);
        controlsPanel.add(tituloPanel);

        JPanel autorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel autorLabel = new JLabel("Autor:");
        autorTxt = new JTextField(30);
        autorPanel.add(autorLabel);
        autorPanel.add(autorTxt);
        controlsPanel.add(autorPanel);

        JPanel generoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel generoLabel = new JLabel("Género:");
        generoComboBox = new JComboBox<>(new String[]{"Todos", "Novela", "Ciencia", "Ficción", "Fantasía", "Misterio", "Romance", "Terror", "Aventura"});
        generoPanel.add(generoLabel);
        generoPanel.add(generoComboBox);
        controlsPanel.add(generoPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buscarButton = new JButton("Buscar");
        volverBtn = new JButton("Salir");
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
                new Login();
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

                String downloadLink = libro.getString("linkDescarga");

                tableModel.addRow(new Object[]{titulo, autor, genero, numPaginas, isbn, icon, downloadLink});
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

    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Descargar");
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private String label;
        private JButton button;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    int row = librosTable.getSelectedRow();
                    String linkDescarga = (String) tableModel.getValueAt(row, 6);
                    String libro = (String) tableModel.getValueAt(row, 0);
                    String usuario = "nombre_del_usuario";  // Aquí puedes obtener el nombre del usuario actual

                    if (linkDescarga != null && !linkDescarga.isEmpty()) {
                        try {
                            URL url = new URL(linkDescarga);
                            if (Desktop.isDesktopSupported()) {
                                Desktop.getDesktop().browse(url.toURI());


                                Descargas descarga = new Descargas(libro, UsuarioActual.getNombreUsuario(), "");
                                descarga.guardar();
                            } else {
                                System.err.println("Desktop no soportado");
                            }
                        } catch (Exception ex) {
                            System.err.println("Error al intentar abrir el enlace: " + ex.getMessage());
                        }
                    } else {
                        System.err.println("El enlace de descarga no es válido.");
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText("Descargar");
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Buscador());
    }
}
