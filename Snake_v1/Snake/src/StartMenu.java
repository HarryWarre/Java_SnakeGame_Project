import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                GamePanel gamePanel = new GamePanel();
                gamePanel.startGame();
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showSettings();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
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
    SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            StartMenu startMenu = new StartMenu();
        }
    });
}

}
