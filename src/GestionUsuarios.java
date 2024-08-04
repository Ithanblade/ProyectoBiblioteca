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

    public GestionUsuarios() {

        setTitle("Gestion Usuarios");
        setContentPane(gestUsuarioPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setMinimumSize(new Dimension(600, 400));
        pack();
        setLocationRelativeTo(null);
        usActual.setText("Usuario Conectado: "+UsuarioActual.getNombreUsuario());
        setVisible(true);

        crearUsuarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new CrearUsuario();
                setVisible(false);

            }
        });

        editarUsuarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new EdicionUsuario();
                setVisible(false);

            }

        });
        eliminarUsuarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new EliminarUsuario();
                setVisible(false);

            }
        });
        buscarUsuarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new BuscarUsuario();
                setVisible(false);
            }
        });

        volverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new MenuAdmin();
                setVisible(false);

            }
        });
    }
}
