import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.mongodb.client.FindIterable;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BuscarUsuario extends JFrame {
    private JTextField nombreTxt;
    private JTextField correoTxt;
    private JComboBox<String> preferenciaCombo;
    private JButton buscarButton;
    private JButton volverBtn;
    private JPanel Buscar;
    private JTable usuarioTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    public BuscarUsuario() {
        setTitle("Buscar Usuarios");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 350);
        setMinimumSize(new Dimension(600, 350));
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Panel superior para la búsqueda
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(new Color(0xF2E8D5));

        // Título
        JLabel tituloLabel = new JLabel("BUSCAR USUARIO", JLabel.CENTER);
        tituloLabel.setFont(new Font("Montserrat", Font.BOLD, 20));
        tituloLabel.setForeground(new Color(0x004D00));
        panelSuperior.add(tituloLabel);

        // Panel de búsqueda con campos y botones
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setLayout(new GridLayout(4, 2, 5, 5));
        panelBusqueda.setBackground(new Color(0xF2E8D5));

        panelBusqueda.add(new JLabel("Nombre:", JLabel.LEFT));
        nombreTxt = new JTextField(20);
        nombreTxt.setFont(new Font("Roboto", Font.PLAIN, 14));
        nombreTxt.setForeground(new Color(0x333333));
        panelBusqueda.add(nombreTxt);

        panelBusqueda.add(new JLabel("Correo:", JLabel.LEFT));
        correoTxt = new JTextField(20);
        correoTxt.setFont(new Font("Roboto", Font.PLAIN, 14));
        correoTxt.setForeground(new Color(0x333333));
        panelBusqueda.add(correoTxt);

        panelBusqueda.add(new JLabel("Preferencia:", JLabel.LEFT));
        preferenciaCombo = new JComboBox<>(new String[]{"Todos", "Novela", "Ciencia Ficción", "Fantasía", "Misterio", "Romance", "Terror", "Aventura"});
        preferenciaCombo.setFont(new Font("Roboto", Font.PLAIN, 14));
        preferenciaCombo.setForeground(new Color(0x333333));
        panelBusqueda.add(preferenciaCombo);

        buscarButton = new JButton("Buscar");
        buscarButton.setFont(new Font("Open Sans", Font.BOLD, 14));
        buscarButton.setForeground(new Color(0x333333));
        buscarButton.setBackground(new Color(0x42743F));
        panelBusqueda.add(buscarButton);

        volverBtn = new JButton("Volver");
        volverBtn.setFont(new Font("Open Sans", Font.BOLD, 14));
        volverBtn.setForeground(new Color(0x333333));
        volverBtn.setBackground(new Color(0x42743F));
        panelBusqueda.add(volverBtn);

        panelSuperior.add(panelBusqueda);

        add(panelSuperior, BorderLayout.NORTH);

        // Tabla para mostrar resultados
        String[] columnNames = {"Nombre", "Correo", "Preferencia"};
        tableModel = new DefaultTableModel(columnNames, 0);
        usuarioTable = new JTable(tableModel);
        scrollPane = new JScrollPane(usuarioTable);
        add(scrollPane, BorderLayout.CENTER);

        // Acción del botón de búsqueda
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarUsuarios();
            }
        });

        // Acción del botón de volver
        volverBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionUsuarios();
                setVisible(false);
            }
        });

        // Cargar todos los usuarios al iniciar
        buscarUsuarios();

        setVisible(true);
    }

    private void buscarUsuarios() {
        tableModel.setRowCount(0); // Limpiar la tabla

        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://ithancamacho:ithancamacho@biblioteca.psx9hpj.mongodb.net/?retryWrites=true&w=majority&appName=Biblioteca")) {
            MongoDatabase database = mongoClient.getDatabase("BibliotecaDigital");
            MongoCollection<Document> collection = database.getCollection("Usuarios");

            // Obtener los filtros para la busqueda
            String nombreFiltro = nombreTxt.getText().trim();
            String correoFiltro = correoTxt.getText().trim();
            String preferenciaFiltro = preferenciaCombo.getSelectedItem().toString();

            Document filtro = new Document();

            // Aplicar filtros si se ingresaron
            if (!nombreFiltro.isEmpty()) {
                filtro.append("usuario", new Document("$regex", nombreFiltro).append("$options", "i"));
            }
            if (!correoFiltro.isEmpty()) {
                filtro.append("correo", new Document("$regex", correoFiltro).append("$options", "i"));
            }
            if (!preferenciaFiltro.equals("Todos")) {
                filtro.append("preferencia", preferenciaFiltro);
            }

            // Buscar en la colección de usuarios
            FindIterable<Document> resultados = collection.find(filtro);

            for (Document usuario : resultados) {
                String usuarioNombre = usuario.getString("usuario");
                String correo = usuario.getString("correo");
                String preferencia = usuario.getString("preferencia");

                // Agregar cada usuario encontrado a la tabla
                tableModel.addRow(new Object[]{usuarioNombre, correo, preferencia});
            }

            // Mensaje si no se encuentran resultados
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Usuario no encontrado");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectarse a la base de datos");
        }
    }
}
