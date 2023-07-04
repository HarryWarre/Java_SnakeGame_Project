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

public class Menu extends JPanel{
    private final JButton btn_start;
    public Menu(){
        setLayout(new BorderLayout());

        btn_start = new JButton("Bắt đầu chơi");
        btn_start.setFont(new Font("Arial", Font.BOLD, 16));
        btn_start.setForeground(Color.WHITE);
        btn_start.setBackground(new Color(30, 39, 46)); // Màu nền dark theme
        btn_start.setOpaque(true);
        btn_start.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        btn_start.setBorder(BorderFactory.createLineBorder(new Color(64, 151, 141), 2)); // Border
        btn_start.setFocusPainted(false); // Loại bỏ hiệu ứng focus
        btn_start.addActionListener((ActionEvent e) -> {
            // Thực hiện hành động khi nút "Bắt đầu chơi" được nhấn
            new GameFrame();
        });
        add(btn_start, BorderLayout.CENTER);
    }
}