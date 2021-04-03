package gui;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DetaliiProduse extends LoginFrame {
    private JPanel secondPanel;
    private JButton detaliiModelBtn;
    private JButton serviceButton;
    private JButton detaliiReparatiiBtn;
    private JButton detaliiDistribuitorBtn;
    private JButton incasariBtn;
    private JTextArea detaliiModelArea;
    private JTextArea detaliiIncasariArea;
    private JTextArea detaliiDistribuitorArea;
    private JTextArea detaliiReparatiiArea;
    private JTextArea detaliiServiceArea;

    public DetaliiProduse() {
        setContentPane(secondPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        detaliiModelBtn.addActionListener(e -> veziDetaliiModelLaptop());
        detaliiReparatiiBtn.addActionListener(e -> veziReparatieService());
        detaliiDistribuitorBtn.addActionListener((e -> veziDistribuitor()));
        serviceButton.addActionListener(e -> veziService());
        incasariBtn.addActionListener(e->veziIncasari());
    }

    public void veziDetaliiModelLaptop() {
        if (LoginCheck) {
            String sql = "SELECT * FROM Model_laptop";
            try {
                Statement rs = connection.createStatement();
                ResultSet resultSet = rs.executeQuery(sql);
                detaliiModelArea.append("Model  Pret  Cod_reducere  ID");
                detaliiModelArea.append("\n");
                while (resultSet.next()) {
                    detaliiModelArea.append(resultSet.getString("Model_Laptop"));
                    detaliiModelArea.append(" ");
                    detaliiModelArea.append(resultSet.getString("Pret"));
                    detaliiModelArea.append(" ");
                    detaliiModelArea.append(resultSet.getString("Cod_reducere"));
                    detaliiModelArea.append(" ");
                    detaliiModelArea.append(resultSet.getString("ID_model"));
                    detaliiModelArea.append("\n");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "You must connect to database first.");
        }
    }

    public void veziReparatieService() {
        if (LoginCheck) {
            detaliiReparatiiArea.setText("");

            String sql1 = "SELECT Model_laptop.Model_Laptop, Service.Nume_Service \n" +
                    "FROM Model_laptop\n" +
                    "INNER JOIN Reparatie_Service ON Model_Laptop.ID_model = Reparatie_Service.ID_model\n" +
                    "INNER JOIN Service ON Service.ID_Service = Reparatie_Service.Service_ID";

            String sql2 = "SELECT TOP(1) Model_laptop.Model_Laptop, Service.Nume_Service, Model_laptop.Pret\n" +
                    "FROM Model_laptop\n" +
                    "INNER JOIN Reparatie_Service ON Model_Laptop.ID_model = Reparatie_Service.ID_model\n" +
                    "INNER JOIN Service ON Service.ID_Service = Reparatie_Service.Service_ID\n" +
                    "ORDER BY Model_laptop.Pret desc";

            detaliiReparatiiArea.append("Modele de laptop aflate in service sunt:\n");

            try {
                Statement rs = connection.createStatement();
                ResultSet resultSet = rs.executeQuery(sql1);
                while (resultSet.next()) {
                    detaliiReparatiiArea.append(resultSet.getString("Model_Laptop"));
                    detaliiReparatiiArea.append(" ");
                    detaliiReparatiiArea.append(resultSet.getString("Nume_Service"));
                    detaliiReparatiiArea.append("\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            detaliiReparatiiArea.append("Cel mai scump laptop aflat in service este: \n");
            detaliiReparatiiArea.append("Model_laptop Pret Nume_Service \n");
            try {
                Statement rs = connection.createStatement();
                ResultSet resultSet = rs.executeQuery(sql2);
                while (resultSet.next()) {
                    detaliiReparatiiArea.append(resultSet.getString("Model_Laptop"));
                    detaliiReparatiiArea.append(" ");
                    detaliiReparatiiArea.append(resultSet.getString("Pret"));
                    detaliiReparatiiArea.append(" ");
                    detaliiReparatiiArea.append(resultSet.getString("Nume_Service"));
                    detaliiReparatiiArea.append("\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "You must connect to database first.");
        }
    }

    public void veziDistribuitor() {
        if (LoginCheck) {
            detaliiDistribuitorArea.setText("");

            String sql1 = "select Model_laptop.Model_Laptop from Model_laptop\n" +
                    "where Model_laptop.ID_model= (select top 1 Model_laptop.ID_model  from Model_laptop  join Distribuitor_vanzari on Model_laptop.ID_model = Distribuitor_vanzari.ID_model\n" +
                    "group by Model_laptop.ID_model\n" +
                    "order by count(Distribuitor_vanzari.ID_model) desc)";

            String sql2 = "SELECT Model_laptop.Model_Laptop, Distribuitor_vanzari.Distribuitor_Nume,(Distribuitor_vanzari.Numar_Produse_Vandute * Model_laptop.Pret) as Incasare_Distribuitor\n" +
                    "FROM Distribuitor_vanzari INNER JOIN Model_laptop ON Distribuitor_vanzari.ID_model = Model_laptop.ID_model";

            detaliiDistribuitorArea.append("Modelul cel care apartine celor mai multi distribuitori:\n");
            try {
                Statement rs = connection.createStatement();
                ResultSet resultSet = rs.executeQuery(sql1);
                while (resultSet.next()) {
                    detaliiDistribuitorArea.append(resultSet.getString("Model_Laptop"));
                    detaliiDistribuitorArea.append("\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            detaliiDistribuitorArea.append("\n");
            detaliiDistribuitorArea.append("Model_Laptop  Distribuitor_Nume  Incasare_Distribuitor \n");
            try {
                Statement rs = connection.createStatement();
                ResultSet resultSet = rs.executeQuery(sql2);
                while (resultSet.next()) {
                    detaliiDistribuitorArea.append(resultSet.getString("Model_Laptop"));
                    detaliiDistribuitorArea.append("");
                    detaliiDistribuitorArea.append(resultSet.getString("Distribuitor_Nume"));
                    detaliiDistribuitorArea.append("");
                    detaliiDistribuitorArea.append(resultSet.getString("Incasare_Distribuitor"));
                    detaliiDistribuitorArea.append("\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "You must connect to database first.");
        }
    }

    public void veziIncasari() {
        if(LoginCheck) {
            String sql = "SELECT Marci_de_laptop.Nume_marca_laptop\n" +
                    "FROM Marci_de_laptop\n" +
                    "WHERE Marci_de_laptop.ID_marca = (SELECT Incasari_Marca_Model.ID_marca FROM Incasari_Marca_Model\n" +
                    "WHERE Profit = (SELECT  TOP(1) Profit FROM Incasari_Marca_Model\n" +
                    "ORDER BY Profit DESC)\n" +
                    ")";
            detaliiIncasariArea.append("Marca cu cel mai mare profit este:\n");
            try {
                Statement rs = connection.createStatement();
                ResultSet resultSet = rs.executeQuery(sql);
                while (resultSet.next()) {
                    detaliiIncasariArea.append(resultSet.getString("Nume_marca_laptop"));
                    detaliiDistribuitorArea.append("\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void veziService() {
        if(LoginCheck) {
            String sql = "SELECT * FROM Service";
            try {
                Statement rs = connection.createStatement();
                ResultSet resultSet = rs.executeQuery(sql);
                detaliiServiceArea.append("ID_Service  Nume_Service  Numar_Telefon  Locatie");
                detaliiServiceArea.append("\n");
                while (resultSet.next()) {
                    detaliiServiceArea.append(resultSet.getString("ID_Service"));
                    detaliiServiceArea.append(" ");
                    detaliiServiceArea.append(resultSet.getString("Nume_Service"));
                    detaliiServiceArea.append(" ");
                    detaliiServiceArea.append(resultSet.getString("Numar_Telefon"));
                    detaliiServiceArea.append(" ");
                    detaliiServiceArea.append(resultSet.getString("Locatie"));
                    detaliiServiceArea.append("\n");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {JOptionPane.showMessageDialog(null,"You must connect to database first.");}
    }
}
