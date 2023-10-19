import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CrearCuentaForm {
    private JFrame frame;
    private JButton crearCuentaButton;
    private int idCliente; // El ID del cliente que ha iniciado sesión

    public CrearCuentaForm(int idCliente) {
        this.idCliente = idCliente;

        frame = new JFrame("Crear Cuenta");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 150);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.setBackground(new Color(93, 193, 185));

        JLabel nombreLabel = new JLabel("Nombre:");
        JLabel saldoLabel = new JLabel("Saldo Inicial:");
        JLabel nombreClienteLabel = new JLabel();
        JLabel saldoInicialLabel = new JLabel("0.0");

        crearCuentaButton = new JButton("Crear Cuenta");

        panel.add(nombreLabel);
        panel.add(nombreClienteLabel);
        panel.add(saldoLabel);
        panel.add(saldoInicialLabel);
        panel.add(crearCuentaButton);

        obtenerDatosCliente(idCliente, nombreClienteLabel, saldoInicialLabel);

        frame.add(panel);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        frame.setLocation(x, y);

        crearCuentaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (crearCuenta()) {
                    JOptionPane.showMessageDialog(null, "Cuenta creada exitosamente.\nNúmero de Cuenta: " + obtenerUltimoNumeroCuenta(), "Cuenta Creada", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al crear la cuenta.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setVisible(true);
    }

    private void obtenerDatosCliente(int idCliente, JLabel nombreClienteLabel, JLabel saldoInicialLabel) {
        String url = "jdbc:mysql://localhost:3306/taller2dse";
        String user = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String query = "SELECT nombre_titular FROM clientes WHERE id_cliente = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idCliente);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String nombreCliente = resultSet.getString("nombre_titular");
                nombreClienteLabel.setText(nombreCliente);
            }

            // Obtener el saldo inicial (debes implementar esta parte)
            // Puedes hacer una consulta a la base de datos para obtener el saldo inicial del cliente.
            // Luego, establece el valor en saldoInicialLabel.
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean crearCuenta() {
        String url = "jdbc:mysql://localhost:3306/taller2dse";
        String user = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String insertQuery = "INSERT INTO cuentas (saldo, id_cliente) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setDouble(1, 0); // Establecer el saldo inicial en 0
            preparedStatement.setInt(2, idCliente);
            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private int obtenerUltimoNumeroCuenta() {
        String url = "jdbc:mysql://localhost:3306/taller2dse";
        String user = "root";
        String password = "";
        int numeroCuenta = -1;

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String query = "SELECT MAX(numero_cuenta) FROM cuentas";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                numeroCuenta = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return numeroCuenta;
    }

    public void setVisible(boolean b) {
        frame.setVisible(b);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Debes obtener el ID del cliente desde la autenticación o el inicio de sesión
                int idCliente = obtenerIdClienteDesdeInicioDeSesion();
                new CrearCuentaForm(idCliente);
            }
        });
    }

    // Método de ejemplo para obtener el ID del cliente desde el inicio de sesión
    private static int obtenerIdClienteDesdeInicioDeSesion() {
        return -1;
    }
}
