import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SeguimientoDescargas extends JFrame {
    private JPanel buscarPanel;
    private JTextField usuarioTxt;
    private JTextField libroTxt;
    private JTextField fechaTxt;
    private JButton buscarButton;
    private JButton volverBtn;
    private JTable descargasTable;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;
    private JPanel buscadorPanel;

    // Constructor de la clase SeguimientoDescargas
    public SeguimientoDescargas() {
        setTitle("Seguimiento de Descargas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);
        setMinimumSize(new Dimension(1200, 600));
        setLocationRelativeTo(null);

        // Configuración del panel principal
        buscarPanel = new JPanel(new BorderLayout());
        buscarPanel.setBackground(new Color(0xF2E8D5)); // Color de fondo
        setContentPane(buscarPanel);

        // Configuración de la tabla de descargas
        descargasTable = new JTable();
        scrollPane = new JScrollPane(descargasTable);
        buscarPanel.add(scrollPane, BorderLayout.CENTER);

        // Nombres de las columnas de la tabla
        String[] columnNames = {"Usuario", "Libro", "Fecha de Descarga"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No se pueden editar las celdas
            }
        };

        // Asignar el modelo a la tabla y ajustar el alto de las filas en la tabla
        descargasTable.setModel(tableModel);
        descargasTable.setRowHeight(30);

        // Ajustar el ancho de las columnas
        TableColumnModel columnModel = descargasTable.getColumnModel();
        int[] columnWidths = {200, 400, 200};
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        // Panel superior donde va el titulo y los botones para la busqueda
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(new Color(0xF2E8D5));
        buscarPanel.add(topPanel, BorderLayout.NORTH);

        // Título para el panel
        JLabel titleLabel = new JLabel("Seguimiento de Descargas", JLabel.CENTER);
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0x004D00)); // Color del texto
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // Botones de busqueda
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        controlsPanel.setBackground(new Color(0xF2E8D5)); // Color de fondo
        topPanel.add(controlsPanel, BorderLayout.CENTER);

        // Campo de texto para el usuario
        JPanel usuarioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        usuarioPanel.setBackground(new Color(0xF2E8D5)); // Color de fondo
        JLabel usuarioLabel = new JLabel("Usuario:");
        usuarioLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        usuarioTxt = new JTextField(30);
        usuarioTxt.setFont(new Font("Roboto", Font.PLAIN, 14));
        usuarioTxt.setForeground(new Color(0x333333)); // Color del texto
        usuarioPanel.add(usuarioLabel);
        usuarioPanel.add(usuarioTxt);
        controlsPanel.add(usuarioPanel);

        // Campo de texto para el libro
        JPanel libroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        libroPanel.setBackground(new Color(0xF2E8D5)); // Color de fondo
        JLabel libroLabel = new JLabel("Libro:");
        libroLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        libroTxt = new JTextField(30);
        libroTxt.setFont(new Font("Roboto", Font.PLAIN, 14));
        libroTxt.setForeground(new Color(0x333333)); // Color del texto
        libroPanel.add(libroLabel);
        libroPanel.add(libroTxt);
        controlsPanel.add(libroPanel);

        // Campo de texto para la fecha
        JPanel fechaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fechaPanel.setBackground(new Color(0xF2E8D5)); // Color de fondo
        JLabel fechaLabel = new JLabel("Fecha (YYYY-MM-DD):");
        fechaLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        fechaTxt = new JTextField(30);
        fechaTxt.setFont(new Font("Roboto", Font.PLAIN, 14));
        fechaTxt.setForeground(new Color(0x333333)); // Color del texto
        fechaPanel.add(fechaLabel);
        fechaPanel.add(fechaTxt);
        controlsPanel.add(fechaPanel);

        // Botones de búsqueda y volver
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0xF2E8D5)); // Color de fondo
        buttonPanel.setLayout(new FlowLayout());
        buscarButton = new JButton("Buscar");
        buscarButton.setFont(new Font("Open Sans", Font.BOLD, 14));
        buscarButton.setForeground(new Color(0x333333)); // Color del texto
        buscarButton.setBackground(new Color(0x42743F)); // Color del fondo
        volverBtn = new JButton("Volver");
        volverBtn.setFont(new Font("Open Sans", Font.BOLD, 14));
        volverBtn.setForeground(new Color(0x333333)); // Color del texto
        volverBtn.setBackground(new Color(0x42743F)); // Color del fondo
        buttonPanel.add(buscarButton);
        buttonPanel.add(volverBtn);
        controlsPanel.add(buttonPanel);

        // Acción del botón de búsqueda
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = usuarioTxt.getText();
                String libro = libroTxt.getText();
                String fecha = fechaTxt.getText();
                // Se pasa los datos al metodo para que busque exactamente eso
                cargarDescargas(usuario, libro, fecha);
            }
        });

        // Acción del botón de volver
        volverBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new MenuAdmin();
            }
        });

        // Llenar toda el historial de descargas al inicio
        cargarDescargas("","","");
        setVisible(true);
    }

    // Método para cargar las descargas en la tabla
    private void cargarDescargas(String usuarioFiltro, String libroFiltro, String fechaFiltro) {
        tableModel.setRowCount(0); // Limpiar la tabla

        boolean encontrado = false;

        // Conexión a la base de datos de MongoDB
        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://ithancamacho:ithancamacho@biblioteca.psx9hpj.mongodb.net/?retryWrites=true&w=majority&appName=Biblioteca")) {
            MongoDatabase database = mongoClient.getDatabase("BibliotecaDigital");
            MongoCollection<Document> collection = database.getCollection("Descargas");

            // Creación del filtro para la consulta
            Document filtroDoc = new Document();
            if (!usuarioFiltro.isEmpty()) {
                filtroDoc.append("usuario", new Document("$regex", usuarioFiltro).append("$options", "i"));
            }
            if (!libroFiltro.isEmpty()) {
                filtroDoc.append("libro", new Document("$regex", libroFiltro).append("$options", "i"));
            }
            if (!fechaFiltro.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date fechaInicio = sdf.parse(fechaFiltro);
                Date fechaFin = new Date(fechaInicio.getTime() + 24 * 60 * 60 * 1000 - 1);
                filtroDoc.append("fechaDescarga", new Document("$gte", fechaInicio).append("$lte", fechaFin));
            }

            // Consulta a la colección de descargas
            FindIterable<Document> resultados = filtroDoc.isEmpty() ? collection.find() : collection.find(filtroDoc);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Document descarga : resultados) {
                String usuario = descarga.getString("usuario");
                String libro = descarga.getString("libro");
                Date fechaDescarga = descarga.getDate("fechaDescarga");
                String fechaDescargaStr = sdf.format(fechaDescarga);

                // Agregar fila a la tabla
                tableModel.addRow(new Object[]{usuario, libro, fechaDescargaStr});
                encontrado = true;
            }

            // Mostrar mensajes si no se encontraron resultados
            if (!encontrado) {
                if (!usuarioFiltro.isEmpty() && !libroFiltro.isEmpty() && !fechaFiltro.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No existen registros de descargas para el usuario, libro y fecha proporcionados.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
                } else if (!usuarioFiltro.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No existen registros de descargas para el usuario proporcionado.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
                } else if (!libroFiltro.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No existen registros de descargas para el libro proporcionado.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
                } else if (!fechaFiltro.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No existen registros de descargas para la fecha proporcionada.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
