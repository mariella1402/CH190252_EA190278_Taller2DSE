import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Random;

public class CrearClienteForm {
    private JFrame frame;
    private JTextField nombreField;
    private JTextField duiField;
    private JButton crearClienteButton;

    public CrearClienteForm() {
        frame = new JFrame("Crear Cliente");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 150);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel nombreLabel = new JLabel("Nombre:");
        nombreField = new JTextField(20);

        JLabel duiLabel = new JLabel("DUI:");
        duiField = new JTextField(10);

        crearClienteButton = new JButton("Crear Cliente");

        panel.add(nombreLabel);
        panel.add(nombreField);
        panel.add(duiLabel);
        panel.add(duiField);
        panel.add(crearClienteButton);

        frame.add(panel);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        int x = (screenSize.width - frameSize.width) / 2;
        int y = (screenSize.height - frameSize.height) / 2;
        frame.setLocation(x, y);

        crearClienteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreField.getText();
                String dui = duiField.getText();

                // Genera un PIN aleatorio de 4 dígitos
                String pin = generarPIN();


                // Inserta el cliente en la base de datos
                if (insertarCliente(nombre, dui, pin)) {
                    String mensaje = "Cliente creado exitosamente.\nPIN: " + pin;
                    JOptionPane.showMessageDialog(null, mensaje);
                        frame.dispose();
                    // Cierra la ventana actual
                } else {
                    JOptionPane.showMessageDialog(null, "Error al crear el cliente.");
                }
            }
        });

        frame.setVisible(true);
    }

    private String generarPIN() {
        Random random = new Random();
        int pin = 1000 + random.nextInt(9000);
        return String.valueOf(pin);
    }

    private boolean insertarCliente(String nombre, String dui, String pin) {
        String url = "jdbc:mysql://localhost:3306/taller2dse";
        String user = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String insertQuery = "INSERT INTO clientes (nombre_titular, dui, pin) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, dui);
            preparedStatement.setInt(3, Integer.parseInt(pin));
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idCliente = generatedKeys.getInt(1);
                }
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CrearClienteForm();
            }
        });
    }

    public void setVisible(boolean visible) {

    }
}
