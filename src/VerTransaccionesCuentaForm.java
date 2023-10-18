import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class VerTransaccionesCuentaForm {
    private JFrame frame;
    private JComboBox<String> cuentasComboBox;
    private JTextArea transaccionesTextArea;
    private int idCliente; // El ID del cliente que ha iniciado sesión

    public VerTransaccionesCuentaForm(int idCliente) {
        this.idCliente = idCliente;

        frame = new JFrame("Ver Transacciones de una Cuenta");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        cuentasComboBox = new JComboBox<>();
        JLabel cuentaLabel = new JLabel("Seleccionar Cuenta:");
        panel.add(cuentaLabel);
        panel.add(cuentasComboBox);

        transaccionesTextArea = new JTextArea();
        transaccionesTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(transaccionesTextArea);
        panel.add(scrollPane);

        frame.add(panel);

        obtenerCuentasCliente(idCliente);
        cuentasComboBox.addActionListener(e -> mostrarTransaccionesCuentaSeleccionada());

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

    private void mostrarTransaccionesCuentaSeleccionada() {
        String cuentaSeleccionada = (String) cuentasComboBox.getSelectedItem();

        String url = "jdbc:mysql://localhost:3306/taller2dse";
        String user = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String query = "SELECT tipo_transaccion, monto FROM transacciones WHERE id_cuenta = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(cuentaSeleccionada));

            ResultSet resultSet = preparedStatement.executeQuery();

            transaccionesTextArea.setText(""); // Limpiar el área de texto

            boolean tieneTransacciones = false;

            while (resultSet.next()) {
                tieneTransacciones = true;
                String tipoTransaccion = resultSet.getString("tipo_transaccion");
                double monto = resultSet.getDouble("monto");

                String transaccionInfo = "Tipo de Transacción: " + tipoTransaccion + "\n" +
                        "Monto: $" + monto + "\n\n";

                transaccionesTextArea.append(transaccionInfo);
            }

            if (!tieneTransacciones) {
                transaccionesTextArea.setText("No tiene transacciones.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setVisible(boolean b) {
        frame.setVisible(b);
    }
}
