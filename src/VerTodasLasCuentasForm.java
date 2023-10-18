import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class VerTodasLasCuentasForm {
    private JFrame frame;
    private JTextArea cuentasTextArea;
    private int idCliente; // El ID del cliente que ha iniciado sesión

    public VerTodasLasCuentasForm(int idCliente) {
        this.idCliente = idCliente;

        frame = new JFrame("Ver Datos de Todas las Cuentas");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        cuentasTextArea = new JTextArea();
        cuentasTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(cuentasTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        frame.add(panel);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        frame.setLocation(x, y);

        obtenerYMostrarDatosCuentas();

        frame.setVisible(true);
    }

    private void obtenerYMostrarDatosCuentas() {
        String url = "jdbc:mysql://localhost:3306/taller2dse";
        String user = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String query = "SELECT cuentas.numero_cuenta, clientes.nombre_titular, cuentas.saldo " +
                    "FROM cuentas " +
                    "JOIN clientes ON cuentas.id_cliente = clientes.id_cliente " +
                    "WHERE cuentas.id_cliente = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, idCliente);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int numeroCuenta = resultSet.getInt("numero_cuenta");
                String nombreTitular = resultSet.getString("nombre_titular");
                double saldo = resultSet.getDouble("saldo");

                String cuentaInfo = "Número de Cuenta: " + numeroCuenta + "\n" +
                        "Nombre del Titular: " + nombreTitular + "\n" +
                        "Saldo Disponible: $" + saldo + "\n\n";

                cuentasTextArea.append(cuentaInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setVisible(boolean b) {
        frame.setVisible(b);
    }
}

