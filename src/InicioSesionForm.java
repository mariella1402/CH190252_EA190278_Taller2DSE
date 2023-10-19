import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
        panel.setBackground(new Color(93, 193, 185));

        JLabel duiLabel = new JLabel("DUI:");
        duiField = new JTextField(10);

        JLabel pinLabel = new JLabel("PIN:");
        pinField = new JPasswordField(4);

        iniciarSesionButton = new JButton("Iniciar Sesión");

        iniciarSesionButton.setBackground(new Color(213, 255, 255));
        iniciarSesionButton.setForeground(Color.BLACK);
        iniciarSesionButton.setFont(new Font("Arial", Font.BOLD, 14));
        iniciarSesionButton.setSize(200,300);

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

        //VALIDACIONES
        duiField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || duiField.getText().length()>=9){
                    e.consume();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                String duiFieldText = duiField.getText();

                if(duiFieldText.length()>=9){
                    duiField.setText(duiFieldText.substring(0 , 9));
                }
            }
        });

        pinField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || pinField.getText().length()>=4){
                    e.consume();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String pinFieldText = pinField.getText();

                if(pinFieldText.length()>=4){
                    pinField.setText(pinFieldText.substring(0,9));
                }
            }
        });

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
