import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InicioSesionForm {
    private JFrame frame;
    private JTextField duiField;
    private JPasswordField pinField;
    private JButton iniciarSesionButton;

    public InicioSesionForm() {
        frame = new JFrame("Inicio de Sesión");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel duiLabel = new JLabel("DUI:");
        duiField = new JTextField(10);

        JLabel pinLabel = new JLabel("PIN:");
        pinField = new JPasswordField(4);

        iniciarSesionButton = new JButton("Iniciar Sesión");

        panel.add(duiLabel);
        panel.add(duiField);
        panel.add(pinLabel);
        panel.add(pinField);
        panel.add(iniciarSesionButton);

        frame.add(panel);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        frame.setLocation(x, y);

        iniciarSesionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String dui = duiField.getText();
                String pin = new String(pinField.getPassword());

                int idCliente = obtenerIdClienteDesdeInicioDeSesion(dui, pin);

                if (idCliente != -1) {
                    frame.dispose();
                    new MenuCuentaForm(idCliente);
                } else {
                    JOptionPane.showMessageDialog(null, "Inicio de sesión incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setVisible(true);
    }

    private int obtenerIdClienteDesdeInicioDeSesion(String dui, String pin) {
        String url = "jdbc:mysql://localhost:3306/taller2dse";
        String user = "root";
        String password = "";
        int idCliente = -1;

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String query = "SELECT id_cliente FROM clientes WHERE dui = ? AND pin = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, dui);
            preparedStatement.setString(2, pin);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                idCliente = resultSet.getInt("id_cliente");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idCliente;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                new InicioSesionForm();
            }
        });
    }

    public void setVisible(boolean b) {
    }
}
