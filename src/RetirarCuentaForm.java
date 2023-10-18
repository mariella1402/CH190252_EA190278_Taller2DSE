import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RetirarCuentaForm {
    private JFrame frame;
    private JComboBox<String> cuentasComboBox;
    private JTextField montoRetiroField;
    private JButton retirarButton;
    private int idCliente; // El ID del cliente que ha iniciado sesión
    private JLabel saldoLabel;
    public RetirarCuentaForm(int idCliente) {
        this.idCliente = idCliente;

        frame = new JFrame("Retirar de una Cuenta");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 150);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        cuentasComboBox = new JComboBox<>();
        JLabel cuentaLabel = new JLabel("Seleccionar Cuenta:");
        panel.add(cuentaLabel);
        panel.add(cuentasComboBox);

        JLabel montoLabel = new JLabel("Monto a Retirar:");
        montoRetiroField = new JTextField(10);
        panel.add(montoLabel);
        panel.add(montoRetiroField);

        saldoLabel = new JLabel("Saldo Disponible: ");
        panel.add(saldoLabel);

        retirarButton = new JButton("Retirar");
        panel.add(retirarButton);

        frame.add(panel);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        frame.setLocation(x, y);

        // Llenar el ComboBox con las cuentas del cliente
        obtenerCuentasCliente(idCliente);
        actualizarSaldoSeleccionado();

        cuentasComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarSaldoSeleccionado();
            }
        });

        retirarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cuentaSeleccionada = (String) cuentasComboBox.getSelectedItem();
                double montoRetiro = Double.parseDouble(montoRetiroField.getText());

                if (saldoSuficiente(cuentaSeleccionada, montoRetiro)) {
                    if (realizarRetiro(cuentaSeleccionada, montoRetiro)) {
                        JOptionPane.showMessageDialog(null, "Retiro realizado con éxito.", "Retiro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al realizar el retiro.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Saldo insuficiente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setVisible(true);
    }

    private void obtenerCuentasCliente(int idCliente) {
        String url = "jdbc:mysql://localhost:3306/taller2dse";
        String user = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String query = "SELECT numero_cuenta FROM cuentas WHERE id_cliente = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idCliente);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int numeroCuenta = resultSet.getInt("numero_cuenta");
                cuentasComboBox.addItem(String.valueOf(numeroCuenta));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void actualizarSaldoSeleccionado() {
        String cuentaSeleccionada = (String) cuentasComboBox.getSelectedItem();
        double saldo = obtenerSaldoCuenta(cuentaSeleccionada);
        saldoLabel.setText("Saldo Disponible: $" + saldo);
    }
    private double obtenerSaldoCuenta(String cuentaSeleccionada) {
        String url = "jdbc:mysql://localhost:3306/taller2dse";
        String user = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String query = "SELECT saldo FROM cuentas WHERE numero_cuenta = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(cuentaSeleccionada));

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("saldo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // En caso de error o si la cuenta no se encuentra, devolvemos -1 para indicar un error.
        return -1;
    }

    private boolean saldoSuficiente(String cuentaSeleccionada, double montoRetiro) {
        String url = "jdbc:mysql://localhost:3306/taller2dse";
        String user = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String query = "SELECT saldo FROM cuentas WHERE numero_cuenta = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(cuentaSeleccionada));

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double saldo = resultSet.getDouble("saldo");
                return saldo >= montoRetiro;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    private boolean realizarRetiro(String cuentaSeleccionada, double montoRetiro) {
        String url = "jdbc:mysql://localhost:3306/taller2dse";
        String user = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);

            // Iniciar una transacción
            connection.setAutoCommit(false);

            // Actualizar el saldo de la cuenta
            String updateQuery = "UPDATE cuentas SET saldo = saldo - ? WHERE numero_cuenta = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setDouble(1, montoRetiro);
            updateStatement.setInt(2, Integer.parseInt(cuentaSeleccionada));
            int rowsAffected = updateStatement.executeUpdate();

            // Registrar la transacción como un retiro
            String insertTransaccionQuery = "INSERT INTO transacciones (id_cuenta, tipo_transaccion, monto) VALUES (?, 'Retiro', ?)";
            PreparedStatement transaccionStatement = connection.prepareStatement(insertTransaccionQuery);
            transaccionStatement.setInt(1, Integer.parseInt(cuentaSeleccionada));
            transaccionStatement.setDouble(2, montoRetiro);
            transaccionStatement.executeUpdate();

            // Confirmar la transacción
            connection.commit();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setVisible(boolean b) {
    }
}
