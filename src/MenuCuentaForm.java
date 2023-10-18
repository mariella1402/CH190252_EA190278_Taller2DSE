import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuCuentaForm {
    private JFrame frame;
    private JButton crearCuentaButton;
    private JButton retirarCuentaButton;
    private JButton abonarCuentaButton;
    private JButton verDatosCuentaButton;
    private JButton verTodosDatosButton;
    private JButton verTransaccionesButton;
    private JButton salirButton;
    private int idCliente;

    public MenuCuentaForm(int idCliente) {
        this.idCliente = idCliente;
        frame = new JFrame("Menú de Cuenta");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1));

        crearCuentaButton = new JButton("Crear Cuenta");
        retirarCuentaButton = new JButton("Retirar de una Cuenta");
        abonarCuentaButton = new JButton("Abonar a una Cuenta");
        verDatosCuentaButton = new JButton("Ver Datos de una Cuenta Específica");
        verTodosDatosButton = new JButton("Ver Datos de Todas las Cuentas");
        verTransaccionesButton = new JButton("Ver Transacciones de una Cuenta Específica");
        salirButton = new JButton("Salir");

        panel.add(crearCuentaButton);
        panel.add(retirarCuentaButton);
        panel.add(abonarCuentaButton);
        panel.add(verDatosCuentaButton);
        panel.add(verTodosDatosButton);
        panel.add(verTransaccionesButton);
        panel.add(salirButton);

        frame.add(panel);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        frame.setLocation(x, y);

        // Agregar ActionListener para cada botón

        crearCuentaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CrearCuentaForm crearCuentaForm = new CrearCuentaForm(idCliente);
                crearCuentaForm.setVisible(true);
              }
        });

        retirarCuentaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new RetirarCuentaForm(idCliente);
                 }
        });

        abonarCuentaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AbonarCuentaForm(idCliente);
            }
        });

        verDatosCuentaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new VerCuentaForm(idCliente);
            }
        });

        verTodosDatosButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new VerTodasLasCuentasForm(idCliente);
            }
        });

        verTransaccionesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new VerTransaccionesCuentaForm(idCliente);
            }
        });

        salirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        frame.setVisible(true);
    }
}
