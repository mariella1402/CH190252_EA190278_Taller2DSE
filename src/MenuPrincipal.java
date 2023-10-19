import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipal {
    private JFrame frame;
    private JButton crearClienteButton;
    private JButton accederCuentaButton;

    public MenuPrincipal() {
        frame = new JFrame("Cajero Automático MaMi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(93, 193, 185));

        crearClienteButton = new JButton("Crear Cliente");
        accederCuentaButton = new JButton("Acceder a la Cuenta");

        panel.add(crearClienteButton);
        panel.add(accederCuentaButton);

        frame.add(panel);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        frame.setLocation(x, y);


        crearClienteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new CrearClienteForm().setVisible(true);
                    }
                });
            }
        });


        accederCuentaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new InicioSesionForm().setVisible(true);
                    }
                });
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MenuPrincipal();
            }
        });
    }
}
