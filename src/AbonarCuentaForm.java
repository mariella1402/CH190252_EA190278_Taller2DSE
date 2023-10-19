import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AbonarCuentaForm {
    private JFrame frame;
    private JComboBox<String> cuentasComboBox;
    private JTextField montoAbonoField;
    private JButton abonarButton;
    private int idCliente; // El ID del cliente que ha iniciado sesión

    public AbonarCuentaForm(int idCliente) {
        this.idCliente = idCliente;

        frame = new JFrame("Abonar a una Cuenta");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 150);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.setBackground(new Color(93, 193, 185));

        cuentasComboBox = new JComboBox<>();
        JLabel cuentaLabel = new JLabel("Seleccionar Cuenta:");
        panel.add(cuentaLabel);
        panel.add(cuentasComboBox);

        JLabel montoLabel = new JLabel("Monto a Abonar:");
        montoAbonoField = new JTextField(10);
        panel.add(montoLabel);
        panel.add(montoAbonoField);

        abonarButton = new JButton("Abonar");
        panel.add(abonarButton);

        frame.add(panel);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        frame.setLocation(x, y);

        // Llenar el ComboBox con las cuentas del cliente
        obtenerCuentasCliente(idCliente);

        abonarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String cuentaSeleccionada = (String) cuentasComboBox.getSelectedItem();
                double montoAbono = 0;
                try {
                    montoAbono = Double.parseDouble(montoAbonoField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "El monto debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (montoAbono > 0) {
                    if (realizarAbono(cuentaSeleccionada, montoAbono)) {
                        JOptionPane.showMessageDialog(null, "Abono realizado con éxito.", "Abono Exitoso", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Error al realizar el abono.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "El monto del abono debe ser mayor que 0.", "Error", JOptionPane.ERROR_MESSAGE);
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

    private boolean realizarAbono(String cuentaSeleccionada, double montoAbono) {
        String url = "jdbc:mysql://localhost:3306/taller2dse";
        String user = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);

            // Iniciar una transacción
            connection.setAutoCommit(false);

            // Actualizar el saldo de la cuenta
            String updateQuery = "UPDATE cuentas SET saldo = saldo + ? WHERE numero_cuenta = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            updateStatement.setDouble(1, montoAbono);
            updateStatement.setInt(2, Integer.parseInt(cuentaSeleccionada));
            int rowsAffected = updateStatement.executeUpdate();

            // Registrar la transacción como un depósito en lugar de abono
            String insertTransaccionQuery = "INSERT INTO transacciones (id_cuenta, tipo_transaccion, monto) VALUES (?, 'Depósito', ?)";
            PreparedStatement transaccionStatement = connection.prepareStatement(insertTransaccionQuery);
            transaccionStatement.setInt(1, Integer.parseInt(cuentaSeleccionada));
            transaccionStatement.setDouble(2, montoAbono);
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
        frame.setVisible(b);
    }
}
