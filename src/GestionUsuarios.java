import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GestionUsuarios extends JFrame {
    private JPanel gestUsuarioPanel;
    private JButton crearUsuarioButton;
    private JButton editarUsuarioButton;
    private JButton eliminarUsuarioButton;
    private JButton buscarUsuarioButton;
    private JButton volverButton;
    private JLabel usActual;

    // Constructor de la clase GestionUsuarios
    public GestionUsuarios() {
        setTitle("Gestion Usuarios");
        setContentPane(gestUsuarioPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setMinimumSize(new Dimension(600, 400));
        pack();
        setLocationRelativeTo(null);
        usActual.setText("Usuario Conectado: " + UsuarioActual.getNombreUsuario());
        setVisible(true);

        // Acción del botón "Crear Usuario"
        crearUsuarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CrearUsuario();
                setVisible(false);
            }
        });

        // Acción del botón "Editar Usuario"
        editarUsuarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EdicionUsuario();
                setVisible(false);
            }
        });

        // Acción del botón "Eliminar Usuario"
        eliminarUsuarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EliminarUsuario();
                setVisible(false);
            }
        });

        // Acción del botón "Buscar Usuario"
        buscarUsuarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BuscarUsuario();
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
