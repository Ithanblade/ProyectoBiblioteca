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
        // Configuración de la ventana principal
        setTitle("Buscar Libro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 650);
        setMinimumSize(new Dimension(1400, 650));
        setLocationRelativeTo(null);

        // Inicialización del panel principal
        buscarPanel = new JPanel(new BorderLayout());
        buscarPanel.setBackground(new Color(0xF2E8D5));
        setContentPane(buscarPanel);

        // Configuración de la tabla y su modelo
        librosTable = new JTable();
        scrollPane = new JScrollPane(librosTable);
        buscarPanel.add(scrollPane, BorderLayout.CENTER);

        String[] columnNames = {"Título", "Autor", "Género", "Número de Páginas", "ISBN", "Portada", "Descargar"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 5) {
                    return ImageIcon.class; // Columna de la portada como ImageIcon
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;  // Solo la columna de Descargar es editable
            }
        };

        librosTable.setModel(tableModel);
        librosTable.setRowHeight(200); // Ajusta la altura de las filas

        // Configuración de las columnas de la tabla
        TableColumnModel columnModel = librosTable.getColumnModel();
        int[] columnWidths = {250, 200, 150, 150, 150, 250, 100};
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(columnWidths[i]);
        }
        librosTable.getColumn("Portada").setCellRenderer(new ImageRenderer());
        librosTable.getColumn("Descargar").setCellRenderer(new ButtonRenderer());
        librosTable.getColumn("Descargar").setCellEditor(new ButtonEditor(new JCheckBox()));

        // Panel superior para encabezados y filtros
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

        // Panel de controles de búsqueda
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        controlsPanel.setBackground(new Color(0xF2E8D5));
        topPanel.add(controlsPanel, BorderLayout.SOUTH);

        // Panel para el título
        JPanel tituloPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tituloPanel.setBackground(new Color(0xF2E8D5));
        JLabel tituloLabel = new JLabel("Título:");
        tituloTxt = new JTextField(30);
        tituloPanel.add(tituloLabel);
        tituloPanel.add(tituloTxt);
        controlsPanel.add(tituloPanel);

        // Panel para el autor
        JPanel autorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        autorPanel.setBackground(new Color(0xF2E8D5));
        JLabel autorLabel = new JLabel("Autor:");
        autorTxt = new JTextField(30);
        autorPanel.add(autorLabel);
        autorPanel.add(autorTxt);
        controlsPanel.add(autorPanel);

        // Panel para el género
        JPanel generoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        generoPanel.setBackground(new Color(0xF2E8D5));
        JLabel generoLabel = new JLabel("Género:");
        generoComboBox = new JComboBox<>(new String[]{"Todos", "Novela", "Ciencia", "Ficción", "Fantasía", "Misterio", "Romance", "Terror", "Aventura"});
        generoPanel.add(generoLabel);
        generoPanel.add(generoComboBox);
        controlsPanel.add(generoPanel);

        // Panel para los botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0xF2E8D5));
        buttonPanel.setLayout(new FlowLayout());
        buscarButton = new JButton("Buscar");
        buscarButton.setFont(new Font("Open Sans", Font.BOLD, 14));
        buscarButton.setForeground(new Color(0x333333));
        buscarButton.setBackground(new Color(0x42743F));
        volverBtn = new JButton("Salir");
        volverBtn.setFont(new Font("Open Sans", Font.BOLD, 14));
        volverBtn.setForeground(new Color(0x333333));
        volverBtn.setBackground(new Color(0x42743F));
        buttonPanel.add(buscarButton);
        buttonPanel.add(volverBtn);
        controlsPanel.add(buttonPanel);

        // Acción del botón de búsqueda
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titulo = tituloTxt.getText();
                String autor = autorTxt.getText();
                String genero = (String) generoComboBox.getSelectedItem();
                cargarLibros(titulo, autor, genero); // Cargar libros según filtros
            }
        });

        // Acción del botón de salir
        volverBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new Login(); // Abre la ventana de inicio de sesión
            }
        });

        // Carga inicial de libros
        cargarLibros("", "", "Todos");

        // Hace visible la ventana
        setVisible(true);
    }

    // Método para cargar los libros según los filtros
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

            // Consulta a la base de datos
            FindIterable<Document> resultados = filtroDoc.isEmpty() ? collection.find() : collection.find(filtroDoc);

            for (Document libro : resultados) {
                // Obtener los datos del libro
                String titulo = libro.getString("titulo");
                String autor = libro.getString("autor");
                String genero = libro.getString("genero");
                int numPaginas = libro.getInteger("numPaginas");
                String isbn = libro.getString("ISBN");

                // Decodificar la imagen de la portada
                String portadaBase64 = libro.getString("portada");
                byte[] portadaData = Base64.getDecoder().decode(portadaBase64);
                ByteArrayInputStream bais = new ByteArrayInputStream(portadaData);
                BufferedImage portadaImage = ImageIO.read(bais);

                ImageIcon icon = new ImageIcon(portadaImage.getScaledInstance(150, 200, Image.SCALE_SMOOTH));

                String downloadLink = libro.getString("linkDescarga");

                // Agregar los datos del libro a la tabla
                tableModel.addRow(new Object[]{titulo, autor, genero, numPaginas, isbn, icon, downloadLink});
            }
        } catch (IOException ioException) {
            ioException.printStackTrace(); // Manejo de errores de IO
        }
    }

    // Renderiza las imágenes en la columna de portada
    private static class ImageRenderer extends JLabel implements TableCellRenderer {
        public ImageRenderer() {
            setOpaque(true);
            setHorizontalAlignment(JLabel.CENTER); // Centra la imagen en la celda
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

    // Renderiza el botón de descarga en la tabla
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

    // Maneja la edición de la celda del botón de descarga
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
                    fireEditingStopped(); // Detiene la edición cuando se hace clic en el botón
                    int row = librosTable.getSelectedRow();
                    String linkDescarga = (String) tableModel.getValueAt(row, 6);
                    String libro = (String) tableModel.getValueAt(row, 0);
                    String usuario = UsuarioActual.getNombreUsuario();

                    if (linkDescarga != null && !linkDescarga.isEmpty()) {
                        try {
                            URL url = new URL(linkDescarga);
                            if (Desktop.isDesktopSupported()) {
                                Desktop.getDesktop().browse(url.toURI()); // Abre el enlace en el navegador

                                // Registrar la descarga del libro
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
}
