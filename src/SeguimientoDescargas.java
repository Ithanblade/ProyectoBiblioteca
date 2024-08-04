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

    public SeguimientoDescargas() {
        setTitle("Seguimiento de Descargas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);
        setMinimumSize(new Dimension(1200, 600));
        setLocationRelativeTo(null);

        buscarPanel = new JPanel(new BorderLayout());
        buscarPanel.setBackground(new Color(0xF2E8D5));
        setContentPane(buscarPanel);

        descargasTable = new JTable();
        scrollPane = new JScrollPane(descargasTable);
        buscarPanel.add(scrollPane, BorderLayout.CENTER);

        String[] columnNames = {"Usuario", "Libro", "Fecha de Descarga"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No se pueden editar las celdas
            }
        };

        descargasTable.setModel(tableModel);
        descargasTable.setRowHeight(30);

        TableColumnModel columnModel = descargasTable.getColumnModel();
        int[] columnWidths = {200, 400, 200};
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(columnWidths[i]);
        }

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(new Color(0xF2E8D5));
        buscarPanel.add(topPanel, BorderLayout.NORTH);

        JLabel titleLabel = new JLabel("Seguimiento de Descargas", JLabel.CENTER);
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0x004D00));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        controlsPanel.setBackground(new Color(0xF2E8D5));
        topPanel.add(controlsPanel, BorderLayout.CENTER);

        JPanel usuarioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        usuarioPanel.setBackground(new Color(0xF2E8D5));
        JLabel usuarioLabel = new JLabel("Usuario:");
        usuarioLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        usuarioTxt = new JTextField(30);
        usuarioTxt.setFont(new Font("Roboto", Font.PLAIN, 14));
        usuarioTxt.setForeground(new Color(0x333333));
        usuarioPanel.add(usuarioLabel);
        usuarioPanel.add(usuarioTxt);
        controlsPanel.add(usuarioPanel);

        JPanel libroPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        libroPanel.setBackground(new Color(0xF2E8D5));
        JLabel libroLabel = new JLabel("Libro:");
        libroLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        libroTxt = new JTextField(30);
        libroTxt.setFont(new Font("Roboto", Font.PLAIN, 14));
        libroTxt.setForeground(new Color(0x333333));
        libroPanel.add(libroLabel);
        libroPanel.add(libroTxt);
        controlsPanel.add(libroPanel);

        JPanel fechaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fechaPanel.setBackground(new Color(0xF2E8D5));
        JLabel fechaLabel = new JLabel("Fecha (YYYY-MM-DD):");
        fechaLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        fechaTxt = new JTextField(30);
        fechaTxt.setFont(new Font("Roboto", Font.PLAIN, 14));
        fechaTxt.setForeground(new Color(0x333333));
        fechaPanel.add(fechaLabel);
        fechaPanel.add(fechaTxt);
        controlsPanel.add(fechaPanel);

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
                String usuario = usuarioTxt.getText();
                String libro = libroTxt.getText();
                String fecha = fechaTxt.getText();
                cargarDescargas(usuario, libro, fecha);
            }
        });

        volverBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new Buscador();
            }
        });

        cargarDescargas("","","");
        setVisible(true);
    }

    private void cargarDescargas(String usuarioFiltro, String libroFiltro, String fechaFiltro) {
        tableModel.setRowCount(0); // Limpiar la tabla

        boolean encontrado = false;

        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://ithancamacho:ithancamacho@biblioteca.psx9hpj.mongodb.net/?retryWrites=true&w=majority&appName=Biblioteca")) {
            MongoDatabase database = mongoClient.getDatabase("BibliotecaDigital");
            MongoCollection<Document> collection = database.getCollection("Descargas");

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

            FindIterable<Document> resultados = filtroDoc.isEmpty() ? collection.find() : collection.find(filtroDoc);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Document descarga : resultados) {
                String usuario = descarga.getString("usuario");
                String libro = descarga.getString("libro");
                Date fechaDescarga = descarga.getDate("fechaDescarga");
                String fechaDescargaStr = sdf.format(fechaDescarga);

                tableModel.addRow(new Object[]{usuario, libro, fechaDescargaStr});
                encontrado = true;
            }

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
