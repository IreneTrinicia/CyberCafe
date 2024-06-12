package Minip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EditRecordsWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField userNameField;
    private JTextField arrivalDateField;
    private JTextField arrivalTimeField;
    private JTextField exitTimeField;
    private JTextField totalHoursField;
    private JTextField foodCostField;
    private JTextField additionalCostField;
    private JTextField totalAmountField;
    private JButton saveChangesButton;

    public EditRecordsWindow(String selectedRecord) {
        setTitle("Edit Records");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(9, 2, 5, 5));

        userNameField = new JTextField();
        arrivalDateField = new JTextField();
        arrivalTimeField = new JTextField();
        exitTimeField = new JTextField();
        totalHoursField = new JTextField();
        foodCostField = new JTextField();
        additionalCostField = new JTextField();
        totalAmountField = new JTextField();

        retrieveRecordData(selectedRecord);

        mainPanel.add(new JLabel("User Name:"));
        mainPanel.add(userNameField);
        mainPanel.add(new JLabel("Arrival Date:"));
        mainPanel.add(arrivalDateField);
        mainPanel.add(new JLabel("Arrival Time:"));
        mainPanel.add(arrivalTimeField);
        mainPanel.add(new JLabel("Exit Time:"));
        mainPanel.add(exitTimeField);
        mainPanel.add(new JLabel("Total Hours:"));
        mainPanel.add(totalHoursField);
        mainPanel.add(new JLabel("Food Cost:"));
        mainPanel.add(foodCostField);
        mainPanel.add(new JLabel("Additional Cost:"));
        mainPanel.add(additionalCostField);
        mainPanel.add(new JLabel("Total Amount:"));
        mainPanel.add(totalAmountField);

        saveChangesButton = new JButton("Save Changes");
        saveChangesButton.addActionListener(new SaveChangesButtonListener());

        mainPanel.add(new JPanel());
        mainPanel.add(saveChangesButton);

        add(mainPanel);
        setLocationRelativeTo(null);
    }

    private void retrieveRecordData(String selectedRecord) {
        try {
            String url = "jdbc:mysql://localhost:3306/cybercafe";
            String user = "root";
            String password = "root123";

            Connection connection = DriverManager.getConnection(url, user, password);
            String query = "SELECT * FROM user_payments WHERE user_name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, selectedRecord);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                userNameField.setText(resultSet.getString("user_name"));
                arrivalDateField.setText(resultSet.getString("arrival_date"));
                arrivalTimeField.setText(resultSet.getString("arrival_time"));
                exitTimeField.setText(resultSet.getString("exit_time"));
                totalHoursField.setText(resultSet.getString("total_hours"));
                foodCostField.setText(resultSet.getString("food_cost"));
                additionalCostField.setText(resultSet.getString("additional_cost"));
                totalAmountField.setText(Integer.toString(resultSet.getInt("total_amount")));
            } else {
                JOptionPane.showMessageDialog(this, "Record not found.", "Error", JOptionPane.ERROR_MESSAGE);
                dispose();
            }

            preparedStatement.close();
            connection.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving data from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class SaveChangesButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveChanges();
        }
    }

    private void saveChanges() {
        try {
            String url = "jdbc:mysql://localhost:3306/cybercafe";
            String user = "root";
            String password = "root123";

            Connection connection = DriverManager.getConnection(url, user, password);
            String updateQuery = "UPDATE user_payments SET " +
                    "user_name = ?, " +
                    "arrival_date = ?, " +
                    "arrival_time = ?, " +
                    "exit_time = ?, " +
                    "total_hours = ?, " +
                    "food_cost = ?, " +
                    "additional_cost = ?, " +
                    "total_amount = ? " +
                    "WHERE user_name = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

            updateStatement.setString(1, userNameField.getText());
            updateStatement.setString(2, arrivalDateField.getText());
            updateStatement.setString(3, arrivalTimeField.getText());
            updateStatement.setString(4, exitTimeField.getText());
            updateStatement.setString(5, totalHoursField.getText());
            updateStatement.setString(6, foodCostField.getText());
            updateStatement.setString(7, additionalCostField.getText());
            updateStatement.setInt(8, Integer.parseInt(totalAmountField.getText()));
            updateStatement.setString(9, userNameField.getText());

            System.out.println("Generated SQL Statement: " + updateStatement);  

            int rowsAffected = updateStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Changes saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No records were updated.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            updateStatement.close();
            connection.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating records in the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EditRecordsWindow editRecordsWindow = new EditRecordsWindow("SelectedUserName");
            editRecordsWindow.setVisible(true);
        });
    }
}
