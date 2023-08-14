/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author PhanHoangVietHS
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Menu extends JPanel {

    private final JButton btn_start;
    private GamePanel gamePanel;
    private int highestPlayerScore;

    public Menu() {
        // Bắt đầu chơi
        setLayout(new BorderLayout());
        findHighestPlayer();

        // Tạo một JLabel để hiển thị tên và điểm người chơi cao nhất
        JLabel highestPlayerLabel = new JLabel("High Score: " + highestPlayerScore);
        highestPlayerLabel.setFont(new Font("Arial", Font.BOLD, 32));
        highestPlayerLabel.setForeground(Color.WHITE);
        //highestPlayerLabel.setBackground(new Color(30, 39, 46)); 
        highestPlayerLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding

        btn_start = new JButton();
        // Load the image from a file (change the path accordingly)
        ImageIcon buttonImageIcon = new ImageIcon("img/start.png");

        // Resize the image to the desired dimensions
        int buttonImageWidth = 650; // Set your desired width here
        int buttonImageHeight = 100; // Set your desired height here
        Image buttonImage = buttonImageIcon.getImage().getScaledInstance(buttonImageWidth, buttonImageHeight, Image.SCALE_SMOOTH);

        // Set the image as the icon for the button
        btn_start.setIcon(new ImageIcon(buttonImage));
        btn_start.setFocusPainted(false); // Loại bỏ hiệu ứng focus
        btn_start.addActionListener((ActionEvent e) -> {
            // Thực hiện hành động khi nút "Bắt đầu chơi" được nhấn
            startGame();
        });

        // Load the image from a file (change the path accordingly)
        ImageIcon originalImageIcon = new ImageIcon("img/logo.png");

        // Resize the image to the desired dimensions
        int desiredWidth = 600; // Set your desired width here
        int desiredHeight = 450; // Set your desired height here
        Image originalImage = originalImageIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedImageIcon = new ImageIcon(resizedImage);

        // Tạo một JLabel chứa hình đã thu nhỏ
        JLabel imageLabel = new JLabel(resizedImageIcon);

        // Tạo một JPanel để chứa highestPlayerLabel và nút "Bắt đầu chơi" cùng với hình
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel.add(highestPlayerLabel);
        labelPanel.setBackground(Color.BLACK); // Màu nền dark theme

        // Add the imageLabel at the top and the btn_start below it in the mainPanel
        mainPanel.add(imageLabel, BorderLayout.PAGE_START);
        mainPanel.add(labelPanel, BorderLayout.PAGE_END);
        mainPanel.add(btn_start, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void startGame() {
        removeAll(); // Xóa nút "Bắt đầu chơi" khỏi panel
        setLayout(new BorderLayout());
        gamePanel = new GamePanel();
        add(gamePanel, BorderLayout.CENTER);
        revalidate(); // Cập nhật layout     
        gamePanel.registerKeyListener();
    }
    

    private void findHighestPlayer() {
        JSONParser parser = new JSONParser();

        try {
            JSONArray playersArray = (JSONArray) parser.parse(new FileReader("players.json"));

            int maxScore = 1;

            for (Object playerObj : playersArray) {
                JSONObject playerJson = (JSONObject) playerObj;
                int score = ((Long) playerJson.get("score")).intValue();
                if (score > maxScore) {
                    maxScore = score;
                    highestPlayerScore = maxScore;
                }
            }
        } catch (IOException | ParseException e) {
        }
    }
}
