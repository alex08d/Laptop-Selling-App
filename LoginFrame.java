package gui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Objects;
import java.util.Vector;

public class LoginFrame extends JFrame {
    public static Connection connection = null;
    private JTextField userameTextField;
    private JPasswordField passwordTextField;
    private JPanel mainPanel;
    private JButton loginButton;
    private JTextArea text;
    private JButton addButton;
    private JTextField addTextField;
    private JButton afiseazaButton;
    private JButton stergeButton;
    private JButton detaliiBtn;
    public static boolean LoginCheck;
    private static int count = 1;

    public LoginFrame() {
        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        loginButton.addActionListener(e -> Login());
        addButton.addActionListener(e -> add());
        afiseazaButton.addActionListener(e -> showLaptops());
        stergeButton.addActionListener(e -> deleteLaptop());
        detaliiBtn.addActionListener(e -> detaliiProduse());
    }

    public void Login() {
        String url = "jdbc:sqlserver://DESKTOP-4IRQL7J\\sqlexpress;database=Aplicatie_Laptopuri;";
        String admin = "Alex";
        String password = "1234";

        if (userameTextField.getText().equals(admin) && passwordTextField.getText().equals(password)) {
            try {
                connection = DriverManager.getConnection(url, admin, password);
                System.out.println("Connection established!");
            } catch (SQLException e) {
                System.out.println("Connection error! Please try again!");
                JOptionPane.showMessageDialog(null, "Connection error! Login failed.");
                e.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Login successfully!");
            userameTextField.setText("");
            passwordTextField.setText("");
            LoginCheck = true;
        } else {
            JOptionPane.showMessageDialog(null, "Username or password is incorrect. Login failed!");
            userameTextField.setText("");
            passwordTextField.setText("");
        }


    }

    public void add() {
        if (LoginCheck) {
            try {
                String sql2 = "SELECT MAX(ID_Marca)FROM Marci_de_Laptop";
                Statement st = connection.createStatement();
                ResultSet resultSet = st.executeQuery(sql2);
                resultSet.next();
                int count = resultSet.getInt(1);
                String counter = String.valueOf(count + 1);
                String Marca;
                if (addTextField.getText() != null) {
                    Marca = addTextField.getText();
                    String sql1 = "INSERT INTO Marci_de_laptop(ID_marca,Nume_marca_laptop)" + " VALUES (?,?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql1);
                    preparedStatement.setString(1, counter);
                    preparedStatement.setString(2, Marca);
                    preparedStatement.execute();
                    addTextField.setText("");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "You must connect to database first.");
        }
    }

    public void showLaptops() {
        if (text.getText() != null) {
            text.setText("");
        }
        if (LoginCheck) {
            String sql = "SELECT * FROM Marci_de_laptop";
            try {
                Statement rs = connection.createStatement();
                ResultSet resultSet = rs.executeQuery(sql);
                while (resultSet.next()) {
                    text.append(resultSet.getString("Nume_marca_laptop"));
                    text.append("\n");

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "You must connect to database first.");
        }
    }

    public void deleteLaptop() {
        String Marca;
        if (LoginCheck) {

            try {
                if (addTextField.getText() != null) {
                    String sql3 = "DELETE FROM Marci_de_laptop WHERE Nume_marca_laptop = ?";
                    Marca = addTextField.getText();
                    PreparedStatement preparedStatement = connection.prepareStatement(sql3);
                    preparedStatement.setString(1, Marca);
                    preparedStatement.execute();
                    addTextField.setText("");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "You must connect to database first.");
        }
    }

    public void detaliiProduse() {
        if (LoginCheck) {
           new DetaliiProduse();
        } else {
            JOptionPane.showMessageDialog(null,"You must connect to database first.");
        }

    }
}