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

    public GestionLibros() {
        setTitle("Gestion Usuarios");
        setContentPane(gestLibrosPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setMinimumSize(new Dimension(600, 400));
        pack();
        setLocationRelativeTo(null);
        usActual.setText("Usuario Conectado: "+UsuarioActual.getNombreUsuario());
        setVisible(true);

        crearLibroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new CrearLibro();
                setVisible(false);

            }
        });

        buscarLibroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BuscarLibro();
                setVisible(false);

            }
        });
        editarLibroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditarLibro();
                setVisible(false);
            }
        });
        eliminarLibroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EliminarLibro();
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
