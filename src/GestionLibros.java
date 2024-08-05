import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GestionLibros extends JFrame {
    private JPanel gestLibrosPanel;
    private JButton volverButton;
    private JButton crearLibroButton;
    private JButton editarLibroButton;
    private JButton eliminarLibroButton;
    private JButton buscarLibroButton;
    private JLabel usActual;

    // Constructor de la clase GestionLibros
    public GestionLibros() {
        setTitle("Gestion Libros");
        setContentPane(gestLibrosPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setMinimumSize(new Dimension(600, 400));
        pack();
        setLocationRelativeTo(null);
        usActual.setText("Usuario Conectado: " + UsuarioActual.getNombreUsuario());
        setVisible(true);

        // Acción del botón "Crear Libro"
        crearLibroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CrearLibro();
                setVisible(false);
            }
        });

        // Acción del botón "Buscar Libro"
        buscarLibroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BuscarLibro();
                setVisible(false);
            }
        });

        // Acción del botón "Editar Libro"
        editarLibroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditarLibro();
                setVisible(false);
            }
        });

        // Acción del botón "Eliminar Libro"
        eliminarLibroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EliminarLibro();
                setVisible(false);
            }
        });

        // Acción del botón "Volver"
        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MenuAdmin();
                setVisible(false);
            }
        });
    }
}
