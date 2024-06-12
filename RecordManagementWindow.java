package Minip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RecordManagementWindow extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextArea recordsTextArea;

    public RecordManagementWindow() {
        setTitle("Record Management");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(255, 220, 220));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(255, 220, 220)); 

        recordsTextArea = new JTextArea();
        recordsTextArea.setEditable(false);

        JButton viewRecordsButton = new JButton("View Records");
        viewRecordsButton.addActionListener(new ViewRecordsButtonListener());

        JButton editRecordsButton = new JButton("Edit Records");
        editRecordsButton.addActionListener(new EditRecordsButtonListener());

        JButton deleteRecordButton = new JButton("Delete Record");
        deleteRecordButton.addActionListener(new DeleteRecordButtonListener());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewRecordsButton);
        buttonPanel.add(editRecordsButton);
        buttonPanel.add(deleteRecordButton);

        mainPanel.add(new JScrollPane(recordsTextArea), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);
    }

    private class ViewRecordsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            fetchAndDisplayRecords();
        }
    }

    private class EditRecordsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            openEditRecordsWindow();
        }
    }

    private class DeleteRecordButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            deleteRecord();
        }
    }

    private void fetchAndDisplayRecords() {
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/cybercafe";
            String user = "root";
            String password = "root123";

            Connection connection = DriverManager.getConnection(url, user, password);
            String query = "SELECT * FROM user_payments";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            StringBuilder records = new StringBuilder("Records:\n");

            while (resultSet.next()) {
                String userName = resultSet.getString("user_name");
                String arrivalDate = resultSet.getString("arrival_date");
                String arrivalTime = resultSet.getString("arrival_time");
                int totalAmount = resultSet.getInt("total_amount");

                records.append("User Name: ").append(userName).append(", ")
                        .append("Login Date: ").append(arrivalDate).append(", ")
                        .append("Login Time: ").append(arrivalTime).append(", ")
                        .append("Total Amount: ₹").append(totalAmount).append("\n");
            }

            recordsTextArea.setText(records.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            recordsTextArea.setText("Error retrieving data from the database.\n\n");
        }
    }

    private void openEditRecordsWindow() {
        String selectedRecord = JOptionPane.showInputDialog(this, "Enter User Name to edit:");

        if (selectedRecord != null && !selectedRecord.isEmpty()) {
            EditRecordsWindow editRecordsWindow = new EditRecordsWindow(selectedRecord);
            editRecordsWindow.setVisible(true);
        }
    }

    private void deleteRecord() {
        String selectedRecord = JOptionPane.showInputDialog(this, "Enter User Name to delete:");

        if (selectedRecord != null && !selectedRecord.isEmpty()) {
            try {
                String url = "jdbc:mysql://127.0.0.1:3306/cybercafe";
                String user = "root";
                String password = "root123";

                Connection connection = DriverManager.getConnection(url, user, password);
                String deleteQuery = "DELETE FROM user_payments WHERE user_name = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setString(1, selectedRecord);

                int rowsAffected = deleteStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Record deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    fetchAndDisplayRecords();
                } else {
                    JOptionPane.showMessageDialog(this, "No records were deleted.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                deleteStatement.close();
                connection.close();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting record from the database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RecordManagementWindow recordManagementWindow = new RecordManagementWindow();
            recordManagementWindow.setVisible(true);
        });
    }
}
