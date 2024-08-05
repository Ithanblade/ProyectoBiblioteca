import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuAdmin extends JFrame {
    private JPanel MenuPanel;
    private JButton usuariosButton;
    private JButton librosButton;
    private JButton seguimientoADescargasButton;
    private JLabel usActual;
    private JButton salir;

    // Constructor de la clase MenuAdmin
    public MenuAdmin() {
        setTitle("Menu Admin");
        setContentPane(MenuPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setMinimumSize(new Dimension(700, 500));
        pack();
        setLocationRelativeTo(null);

        //Mostrar el nombre del usuario conectado
        usActual.setText("Usuario Conectado: " + UsuarioActual.getNombreUsuario()); // Se llama a la clase UsuarioActual que almacena el usuario del Login

        setVisible(true);

        // Acción del botón Usuarios
        usuariosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionUsuarios();
                setVisible(false);
            }
        });

        // Acción del botón Libros
        librosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionLibros();
                setVisible(false);
            }
        });

        // Acción del botón Seguimiento a Descargas
        seguimientoADescargasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SeguimientoDescargas();
                setVisible(false);
            }
        });

        // Acción del botón Salir
        salir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mostrar un cuadro de confirmacion
                int response = JOptionPane.showConfirmDialog(
                        null, "¿Seguro que quiere cerrar sesión?", "Confirmar Cierre de Sesión", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                // Si el usuario confirma, cerrar sesión
                if (response == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "Sesión Cerrada con éxito");
                    new Login();
                    setVisible(false);
                }
            }
        });
    }
}
