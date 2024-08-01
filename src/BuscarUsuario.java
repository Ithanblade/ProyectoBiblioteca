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
        setSize(600, 500);
        setMinimumSize(new Dimension(600, 500));
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Panel superior para búsqueda
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));

        JLabel tituloLabel = new JLabel("BUSCAR USUARIO", JLabel.CENTER);
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panelSuperior.add(tituloLabel);

        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setLayout(new GridLayout(4, 2, 5, 5));

        panelBusqueda.add(new JLabel("Nombre:"));
        nombreTxt = new JTextField(20);
        panelBusqueda.add(nombreTxt);

        panelBusqueda.add(new JLabel("Correo:"));
        correoTxt = new JTextField(20);
        panelBusqueda.add(correoTxt);

        panelBusqueda.add(new JLabel("Preferencia:"));
        preferenciaCombo = new JComboBox<>(new String[]{"Todos", "Novela", "Ciencia", "Ficción", "Fantasía", "Misterio", "Romance", "Terror", "Aventura"});
        panelBusqueda.add(preferenciaCombo);

        buscarButton = new JButton("Buscar");
        panelBusqueda.add(buscarButton);
        volverBtn = new JButton("Volver");
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
        buscarUsuarios(); // Cargar todos los usuarios

        setVisible(true);
    }

    private void buscarUsuarios() {
        tableModel.setRowCount(0); // Limpiar la tabla

        try (MongoClient mongoClient = MongoClients.create("mongodb+srv://ithancamacho:ithancamacho@biblioteca.psx9hpj.mongodb.net/?retryWrites=true&w=majority&appName=Biblioteca")) {
            MongoDatabase database = mongoClient.getDatabase("BibliotecaDigital");
            MongoCollection<Document> collection = database.getCollection("Usuarios");

            String nombreFiltro = nombreTxt.getText().trim();
            String correoFiltro = correoTxt.getText().trim();
            String preferenciaFiltro = preferenciaCombo.getSelectedItem().toString();

            Document filtro = new Document();

            if (!nombreFiltro.isEmpty()) {
                filtro.append("usuario", new Document("$regex", nombreFiltro).append("$options", "i"));
            }
            if (!correoFiltro.isEmpty()) {
                filtro.append("correo", new Document("$regex", correoFiltro).append("$options", "i"));
            }
            if (!preferenciaFiltro.equals("Todos")) {
                filtro.append("preferencia", preferenciaFiltro);
            }

            FindIterable<Document> resultados = collection.find(filtro);

            for (Document usuario : resultados) {
                String usuarioNombre = usuario.getString("usuario");
                String correo = usuario.getString("correo");
                String preferencia = usuario.getString("preferencia");

                tableModel.addRow(new Object[]{usuarioNombre, correo, preferencia});
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Usuario no encontrado");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al conectarse a la base de datos");
        }
    }

}
