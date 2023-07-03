import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class StartMenu {
    private JFrame frame;
    private JButton playButton;
    private JButton settingsButton;
    private JButton exitButton;

    public StartMenu() {
        frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new FlowLayout());

        playButton = new JButton("Chơi game");
        settingsButton = new JButton("Cài đặt");
        exitButton = new JButton("Thoát game");

        playButton.addActionListener((ActionEvent e) -> {
            frame.dispose();
            GamePanel gamePanel = new GamePanel();
            gamePanel.startGame();
        });

        settingsButton.addActionListener((ActionEvent e) -> {
            showSettings();
        });

        exitButton.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });

        frame.add(playButton);
        frame.add(settingsButton);
        frame.add(exitButton);

        frame.setVisible(true);
    }

    private void showSettings() {
        // Hiển thị cửa sổ cài đặt
    }

    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        StartMenu startMenu = new StartMenu();
    });
}

}
