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
        //Bắt đầu chơi
        setLayout(new BorderLayout());
        findHighestPlayer();
        // Tạo một JLabel để hiển thị tên và điểm người chơi cao nhất
        JLabel highestPlayerLabel = new JLabel("High Score : " + highestPlayerScore);
        highestPlayerLabel.setFont(new Font("Arial", Font.BOLD, 32));
        highestPlayerLabel.setForeground(Color.WHITE);
        highestPlayerLabel.setBackground(new Color(30, 39, 46)); // Màu nền dark theme
        highestPlayerLabel.setOpaque(true);
        highestPlayerLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
//        highestPlayerLabel.setBorder(BorderFactory.createLineBorder(new Color(64, 151, 141), 2)); // Border
        
//        add(highestPlayerLabel, BorderLayout.SOUTH);

        btn_start = new JButton("Bắt đầu chơi");
        btn_start.setFont(new Font("Arial", Font.BOLD, 32));
        btn_start.setForeground(Color.WHITE);
        btn_start.setBackground(new Color(30, 39, 46)); // Màu nền dark theme
        btn_start.setOpaque(true);
        btn_start.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10)); // Padding
        btn_start.setBorder(BorderFactory.createLineBorder(new Color(64, 151, 141), 2)); // Border
        btn_start.setFocusPainted(false); // Loại bỏ hiệu ứng focus
        btn_start.addActionListener((ActionEvent e) -> {
            // Thực hiện hành động khi nút "Bắt đầu chơi" được nhấn
            startGame();
        });
        add(btn_start, BorderLayout.CENTER);
        // Tạo một JPanel để chứa highestPlayerLabel và btn_start
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Tạo một JPanel để chứa highestPlayerLabel và canh giữa
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel.add(highestPlayerLabel);
        labelPanel.setBackground(new Color(30, 39, 46)); // Màu nền dark theme

        mainPanel.add(labelPanel, BorderLayout.PAGE_END);
        mainPanel.add(btn_start, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
        loadPlayers();
    }

    private void startGame() {
        removeAll(); // Xóa nút "Bắt đầu chơi" khỏi panel
        setLayout(new BorderLayout());
        gamePanel = new GamePanel();
        add(gamePanel, BorderLayout.CENTER);
        revalidate(); // Cập nhật layout
        //gamePanel.startGame(); // Bắt đầu trò chơi       
        gamePanel.registerKeyListener();
    }

    private void loadPlayers() {
        JSONParser parser = new JSONParser();

        try {
            JSONArray playersArray = (JSONArray) parser.parse(new FileReader("players.json"));

            for (Object playerObj : playersArray) {
                JSONObject playerJson = (JSONObject) playerObj;
                int score = ((Long) playerJson.get("score")).intValue();

                // Xử lý thông tin người chơi theo nhu cầu của bạn
            }
        } catch (IOException | ParseException e) {
        }
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
