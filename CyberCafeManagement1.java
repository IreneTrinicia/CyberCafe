package Minip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CyberCafeManagement1 extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final int NUM_COMPUTERS_PER_SLOT = 50;
    private static final int NUM_SLOTS = 6;
    private static final int ADDITIONAL_CHARGE = 100;

    private Map<String, Map<Integer, Integer>> availableComputersPerDay = new HashMap<>();

    private JTextField userNameField;
    private JTextField arrivalDateField;
    private JTextField arrivalTimeField;
    private JTextField hoursField;
    private JCheckBox printerCheckBox;
    private JCheckBox scannerCheckBox;
    private JCheckBox gamesCheckBox;
    private JCheckBox burgerCheckBox;
    private JCheckBox pizzaCheckBox;
    private JCheckBox sandwichCheckBox;
    private JCheckBox pastaCheckBox;
    private JCheckBox coffeeCheckBox;
    private JCheckBox teaCheckBox;
    private JCheckBox cakeCheckBox;
    private JCheckBox iceCreamCheckBox;
    private JTextArea resultArea;

    private JButton cafeInfoButton;
    private JButton loginButton;

    public CyberCafeManagement1() {
        setTitle("CyberCafe Management");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(255, 240, 240));

        initializeAvailableComputers();

        JPanel cafePanel = new JPanel();
        cafePanel.setLayout(new GridLayout(1, 3));
        cafePanel.setBackground(new Color(255, 240, 240));

        cafePanel.add(new JLabel("Cafe Name: My Cyber Cafe"));

        cafeInfoButton = new JButton("Cafe Info");
        cafeInfoButton.addActionListener(new CafeInfoButtonListener());
        cafeInfoButton.setBackground(new Color(192, 192, 192));
        cafePanel.add(cafeInfoButton);

        loginButton = new JButton("Login");
        loginButton.addActionListener(new OpenLoginPageButtonListener());
        loginButton.setBackground(new Color(192, 192, 192));
        cafePanel.add(loginButton);

        add(cafePanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(10, 2));
        inputPanel.setBackground(new Color(255, 240, 240));

        inputPanel.add(new JLabel("User Name:"));
        userNameField = new JTextField();
        inputPanel.add(userNameField);

        inputPanel.add(new JLabel("Arrival Date (YYYY-MM-DD):"));
        arrivalDateField = new JTextField();
        inputPanel.add(arrivalDateField);

        inputPanel.add(new JLabel("Arrival Time (HH:mm):"));
        arrivalTimeField = new JTextField();
        inputPanel.add(arrivalTimeField);

        inputPanel.add(new JLabel("Number of Hours:"));
        hoursField = new JTextField();
        inputPanel.add(hoursField);

        inputPanel.add(new JLabel("Use Printer:"));
        printerCheckBox = new JCheckBox();
        inputPanel.add(printerCheckBox);

        inputPanel.add(new JLabel("Use Scanner:"));
        scannerCheckBox = new JCheckBox();
        inputPanel.add(scannerCheckBox);

        inputPanel.add(new JLabel("Play Games:"));
        gamesCheckBox = new JCheckBox();
        inputPanel.add(gamesCheckBox);

        JPanel foodPanel = new JPanel();
        foodPanel.setLayout(new GridLayout(2, 4));
        foodPanel.setBorder(BorderFactory.createTitledBorder("Select Food Items"));
        foodPanel.setBackground(new Color(255, 240, 240));

        burgerCheckBox = new JCheckBox("Burger");
        pizzaCheckBox = new JCheckBox("Pizza");
        sandwichCheckBox = new JCheckBox("Sandwich");
        pastaCheckBox = new JCheckBox("Pasta");
        coffeeCheckBox = new JCheckBox("Coffee");
        teaCheckBox = new JCheckBox("Tea");
        cakeCheckBox = new JCheckBox("Cake");
        iceCreamCheckBox = new JCheckBox("Ice Cream");

        foodPanel.add(burgerCheckBox);
        foodPanel.add(pizzaCheckBox);
        foodPanel.add(sandwichCheckBox);
        foodPanel.add(pastaCheckBox);
        foodPanel.add(coffeeCheckBox);
        foodPanel.add(teaCheckBox);
        foodPanel.add(cakeCheckBox);
        foodPanel.add(iceCreamCheckBox);

        inputPanel.add(foodPanel);

        JButton calculateButton = new JButton("Calculate Bill");
        CalculateButtonListener calculateButtonListener = new CalculateButtonListener();
        calculateButton.addActionListener(calculateButtonListener);
        calculateButton.setBackground(new Color(192, 192, 192));

        inputPanel.add(calculateButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);

        add(inputPanel, BorderLayout.CENTER);
        add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeAvailableComputers() {
        availableComputersPerDay = new HashMap<>();
    }

    private class CafeInfoButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            displayCafeInfo();
        }

        private void displayCafeInfo() {
            JOptionPane.showMessageDialog(CyberCafeManagement1.this,
                    "Welcome to My Cyber Cafe!\n\n"
                            + "We provide high-speed internet access and a variety of services.\n\n"
                            + "Additional Charges:\n"
                            + "Printer: ₹" + ADDITIONAL_CHARGE + "\n"
                            + "Scanner: ₹" + ADDITIONAL_CHARGE + "\n"
                            + "Games: ₹" + ADDITIONAL_CHARGE + "\n\n"
                            + "Food Items: ₹80 each\n"
                            + "Burger, Pizza, Sandwich, Pasta, Coffee, Tea, Cake, Ice Cream\n\n"
                            + "Enjoy your time at My Cyber Cafe!",
                    "Cafe Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class CalculateButtonListener implements ActionListener {
        private int foodCost;
        private int totalAmount;
        private String userName;

        public void setFoodCost(int cost) {
            foodCost = cost;
        }

        public void setTotalAmount(int total) {
            totalAmount = total;
        }

        public void setUserName(String name) {
            userName = name;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String userName = userNameField.getText();
                String arrivalDateStr = arrivalDateField.getText();
                String arrivalTimeStr = arrivalTimeField.getText();
                String hoursStr = hoursField.getText();

                DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.ENGLISH);
                DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

                LocalDateTime arrivalDateTime = LocalDateTime.parse(arrivalDateStr + " " + arrivalTimeStr, dateTimeFormat);
                int numberOfHours = Integer.parseInt(hoursStr);

                String dayKey = arrivalDateTime.toLocalDate().toString();
                int slot = getSlot(arrivalDateTime.toLocalTime());

                int totalAmount = numberOfHours * 150;

                if (!availableComputersPerDay.containsKey(dayKey)) {
                    availableComputersPerDay.put(dayKey, new HashMap<>());
                    initializeAvailableComputersForDay(dayKey);
                }

                if (availableComputersPerDay.get(dayKey).containsKey(slot) && availableComputersPerDay.get(dayKey).get(slot) > 0) {
                    availableComputersPerDay.get(dayKey).put(slot, availableComputersPerDay.get(dayKey).get(slot) - 1);

                    resultArea.append("User Name: " + userName + "\n");
                    resultArea.append("Arrival Date: " + arrivalDateTime.toLocalDate() + "\n");
                    resultArea.append("Arrival Time: " + arrivalDateTime.toLocalTime() + "\n");
                    resultArea.append("Exit Time: " + arrivalDateTime.plusHours(numberOfHours).toLocalTime() + "\n");

                    resultArea.append("Food Items:\n");
                    int foodCost = handleFoodItems();

                    if (printerCheckBox.isSelected()) {
                        resultArea.append("Printer Used: Yes (Additional Charge: ₹" + ADDITIONAL_CHARGE + ")\n");
                        totalAmount += ADDITIONAL_CHARGE;
                    }
                    if (scannerCheckBox.isSelected()) {
                        resultArea.append("Scanner Used: Yes (Additional Charge: ₹" + ADDITIONAL_CHARGE + ")\n");
                        totalAmount += ADDITIONAL_CHARGE;
                    }
                    if (gamesCheckBox.isSelected()) {
                        resultArea.append("Games Played: Yes (Additional Charge: ₹" + ADDITIONAL_CHARGE + ")\n");
                        totalAmount += ADDITIONAL_CHARGE;
                    }

                    resultArea.append("Total Food Cost: ₹" + foodCost + "\n");
                    resultArea.append("Total Amount: ₹" + (totalAmount + foodCost) + "\n");
                    resultArea.append("Computers Available for Slot " + slot + " on " + dayKey + ": " + availableComputersPerDay.get(dayKey).get(slot) + "\n\n");

                    storeDataInDatabase(userName, arrivalDateTime, numberOfHours, foodCost, totalAmount + foodCost);
                    storeFoodDataInDatabase(userName, arrivalDateTime, foodCost);

                } else {
                    resultArea.append("Sorry, no computers available for Slot " + slot + " on " + dayKey + ". Please choose another slot.\n\n");
                }

            } catch (Exception ex) {
                resultArea.append("Error: Invalid input. Please enter valid values.\n\n");
            }
        }

        private int handleFoodItems() {
            int foodCost = 0;

            if (burgerCheckBox.isSelected()) {
                resultArea.append("Burger: Yes (Cost: ₹80)\n");
                foodCost += 80;
            }
            if (pizzaCheckBox.isSelected()) {
                resultArea.append("Pizza: Yes (Cost: ₹80)\n");
                foodCost += 80;
            }
            if (sandwichCheckBox.isSelected()) {
                resultArea.append("Sandwich: Yes (Cost: ₹80)\n");
                foodCost += 80;
            }
            if (pastaCheckBox.isSelected()) {
                resultArea.append("Pasta: Yes (Cost: ₹80)\n");
                foodCost += 80;
            }
            if (coffeeCheckBox.isSelected()) {
                resultArea.append("Coffee: Yes (Cost: ₹80)\n");
                foodCost += 80;
            }
            if (teaCheckBox.isSelected()) {
                resultArea.append("Tea: Yes (Cost: ₹80)\n");
                foodCost += 80;
            }
            if (cakeCheckBox.isSelected()) {
                resultArea.append("Cake: Yes (Cost: ₹80)\n");
                foodCost += 80;
            }
            if (iceCreamCheckBox.isSelected()) {
                resultArea.append("Ice Cream: Yes (Cost: ₹80)\n");
                foodCost += 80;
            }

            return foodCost;
        }
    }

    private class OpenLoginPageButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(LoginPage::new);
        }
    }

    private int getSlot(LocalTime arrivalTime) {
        if (arrivalTime.isAfter(LocalTime.of(10, 0)) && arrivalTime.isBefore(LocalTime.of(22, 0))) {
            return (arrivalTime.getHour() - 10) / 2 + 1;
        } else {
            return -1;
        }
    }

    private void initializeAvailableComputersForDay(String dayKey) {
        Map<Integer, Integer> computersPerSlot = availableComputersPerDay.get(dayKey);
        for (int i = 1; i <= NUM_SLOTS; i++) {
            computersPerSlot.put(i, NUM_COMPUTERS_PER_SLOT);
        }
    }

    private void storeDataInDatabase(String userName, LocalDateTime arrivalDateTime, int numberOfHours, int foodCost, int totalAmount) {
        String url = "jdbc:mysql://127.0.0.1:3306/cybercafe";
        String user = "root";
        String password = "root123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "INSERT INTO user_payments (user_name, arrival_date, arrival_time, exit_time, total_hours, food_cost, additional_cost, total_amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, userName);
                preparedStatement.setString(2, arrivalDateTime.toLocalDate().toString());
                preparedStatement.setString(3, arrivalDateTime.toLocalTime().toString());
                preparedStatement.setString(4, arrivalDateTime.plusHours(numberOfHours).toLocalTime().toString());
                preparedStatement.setInt(5, numberOfHours);
                preparedStatement.setInt(6, foodCost);
                preparedStatement.setInt(7, (printerCheckBox.isSelected() || scannerCheckBox.isSelected() || gamesCheckBox.isSelected()) ? ADDITIONAL_CHARGE : 0);
                preparedStatement.setInt(8, totalAmount);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultArea.append("Error storing data in the database.\n\n");
        }
    }

    private void storeFoodDataInDatabase(String userName, LocalDateTime arrivalDateTime, int foodCost) {
        String url = "jdbc:mysql://127.0.0.1:3306/cybercafe";
        String user = "root";
        String password = "root123";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "INSERT INTO foodbill (user_name, arrival_date, arrival_time, food_cost) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, userName);
                preparedStatement.setString(2, arrivalDateTime.toLocalDate().toString());
                preparedStatement.setString(3, arrivalDateTime.toLocalTime().toString());
                preparedStatement.setInt(4, foodCost);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultArea.append("Error storing food data in the database.\n\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CyberCafeManagement1::new);
    }
}
