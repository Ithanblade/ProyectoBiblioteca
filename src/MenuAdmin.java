import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuAdmin extends JFrame{
    private JPanel MenuPanel;
    private JButton usuariosButton;
    private JButton librosButton;
    private JButton seguimientoADescargasButton;
    private JLabel usActual;
    private JButton salir;

    public MenuAdmin() {

        setTitle("Menu Admin");
        setContentPane(MenuPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setMinimumSize(new Dimension(700, 500));
        pack();
        setLocationRelativeTo(null);
        usActual.setText("Usuario Conectado: "+UsuarioActual.getNombreUsuario());

        setVisible(true);

        usuariosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionUsuarios();
                setVisible(false);


            }
        });

        librosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GestionLibros();
                setVisible(false);
            }
        });
        seguimientoADescargasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SeguimientoDescargas();
                setVisible(false);
            }
        });
        salir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(
                        null,"¿Seguro que quiere cerrar sesión?","Confirmar Cierre de Sesión",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);

                if (response == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "Sesión Cerrada con éxito");
                    new Login();
                    setVisible(false);
                }
            }
        });

    }
}
