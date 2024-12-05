package com.example.cafereview.gui;

import javax.swing.*;
import java.awt.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CafeReviewGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JTextArea resultArea;
    private String token;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public CafeReviewGUI() {
        setTitle("Cafe Review App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create panels
        JPanel authPanel = createAuthPanel();
        JPanel cafePanel = createCafePanel();
        JPanel resultPanel = createResultPanel();

        // Add panels to frame
        add(authPanel, BorderLayout.NORTH);
        add(cafePanel, BorderLayout.CENTER);
        add(resultPanel, BorderLayout.SOUTH);
    }

    private JPanel createAuthPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Authentication"));

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> register());

        return panel;
    }

    private JPanel createCafePanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Cafe Operations"));

        JButton viewCafesButton = new JButton("View All Cafes");
        JButton addCafeButton = new JButton("Add New Cafe");
        JButton addReviewButton = new JButton("Add Review");

        panel.add(viewCafesButton);
        panel.add(addCafeButton);
        panel.add(addReviewButton);

        viewCafesButton.addActionListener(e -> viewCafes());
        addCafeButton.addActionListener(e -> showAddCafeDialog());
        addReviewButton.addActionListener(e -> showAddReviewDialog());

        return panel;
    }

    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Results"));

        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(resultArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void login() {
        try {
            String json = mapper.writeValueAsString(new LoginRequest(
                usernameField.getText(),
                new String(passwordField.getPassword())
            ));

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

            HttpResponse<String> response = client.send(request, 
                HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                resultArea.setText("Login successful!");
                token = response.body(); // Store token if using JWT
            } else {
                resultArea.setText("Login failed: " + response.body());
            }
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    private void viewCafes() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/cafes"))
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, 
                HttpResponse.BodyHandlers.ofString());

            resultArea.setText(response.body());
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    private void showAddCafeDialog() {
        JDialog dialog = new JDialog(this, "Add New Cafe", true);
        dialog.setLayout(new GridLayout(6, 2));

        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JCheckBox wifiBox = new JCheckBox();
        JCheckBox restroomBox = new JCheckBox();
        JSpinner seatingSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));

        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Address:"));
        dialog.add(addressField);
        dialog.add(new JLabel("Has WiFi:"));
        dialog.add(wifiBox);
        dialog.add(new JLabel("Has Restroom:"));
        dialog.add(restroomBox);
        dialog.add(new JLabel("Seating Capacity:"));
        dialog.add(seatingSpinner);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            addCafe(nameField.getText(), addressField.getText(), 
                   wifiBox.isSelected(), restroomBox.isSelected(), 
                   (Integer) seatingSpinner.getValue());
            dialog.dispose();
        });

        dialog.add(submitButton);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void addCafe(String name, String address, boolean hasWifi, 
                        boolean hasRestroom, int seatingCapacity) {
        try {
            String json = mapper.writeValueAsString(new CafeRequest(
                name, address, hasWifi, hasRestroom, seatingCapacity
            ));

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/cafes"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

            HttpResponse<String> response = client.send(request, 
                HttpResponse.BodyHandlers.ofString());
            resultArea.setText("Cafe added: " + response.body());
        } catch (Exception e) {
            resultArea.setText("Error: " + e.getMessage());
        }
    }

    // Record classes for requests
    private record LoginRequest(String username, String password) {}
    private record CafeRequest(String name, String address, boolean hasWifi, 
                             boolean hasRestroom, int seatingCapacity) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CafeReviewGUI().setVisible(true);
        });
    }
}