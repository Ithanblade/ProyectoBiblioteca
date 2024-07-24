import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginAdmin extends JFrame {
    private JButton loginButton;
    private JTextField usuarioTxt;
    private JTextField contraTxt;
    private JButton ingresarComoUsuarioButton;
    private JPanel loginAdmin;

    public LoginAdmin() {

        setTitle("Login Administrador");
        setContentPane(loginAdmin);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setMinimumSize(new Dimension(600, 400));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        ingresarComoUsuarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new Login();
                setVisible(false);
            }
        });
    }
}
