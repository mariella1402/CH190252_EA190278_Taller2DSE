import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class VerCuentaForm {
    private JFrame frame;
    private JComboBox<String> cuentasComboBox;
    private JLabel nombreLabel;
    private JLabel numeroCuentaLabel;
    private JLabel saldoLabel;
    private int idCliente; // El ID del cliente que ha iniciado sesión

    public VerCuentaForm(int idCliente) {
        this.idCliente = idCliente;

        frame = new JFrame("Ver Datos de una Cuenta");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));
        panel.setBackground(new Color(93, 193, 185));

        cuentasComboBox = new JComboBox<>();
        JLabel cuentaLabel = new JLabel("Seleccionar Cuenta:");
        panel.add(cuentaLabel);
        panel.add(cuentasComboBox);

        nombreLabel = new JLabel("Nombre del Titular: ");
        panel.add(nombreLabel);

        numeroCuentaLabel = new JLabel("Número de Cuenta: ");
        panel.add(numeroCuentaLabel);

        saldoLabel = new JLabel("Saldo Disponible: ");
        panel.add(saldoLabel);

        frame.add(panel);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        frame.setLocation(x, y);

        // Llenar el ComboBox con las cuentas del cliente
        obtenerCuentasCliente(idCliente);

        cuentasComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarDatosCuentaSeleccionada();
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

    private void mostrarDatosCuentaSeleccionada() {
        String cuentaSeleccionada = (String) cuentasComboBox.getSelectedItem();

        String url = "jdbc:mysql://localhost:3306/taller2dse";
        String user = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String query = "SELECT clientes.nombre_titular, cuentas.numero_cuenta, cuentas.saldo " +
                    "FROM cuentas " +
                    "JOIN clientes ON cuentas.id_cliente = clientes.id_cliente " +
                    "WHERE cuentas.numero_cuenta = ? AND cuentas.id_cliente = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(cuentaSeleccionada));
            preparedStatement.setInt(2, idCliente);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                nombreLabel.setText("Nombre del Titular: " + resultSet.getString("nombre_titular")); // Cambio aquí
                numeroCuentaLabel.setText("Número de Cuenta: " + cuentaSeleccionada);
                saldoLabel.setText("Saldo Disponible: $" + resultSet.getDouble("saldo"));
            } else {
                JOptionPane.showMessageDialog(null, "La cuenta no pertenece al cliente seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
                nombreLabel.setText("Nombre del Titular: ");
                numeroCuentaLabel.setText("Número de Cuenta: ");
                saldoLabel.setText("Saldo Disponible: ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void setVisible(boolean b) {
        frame.setVisible(b);
    }
}

