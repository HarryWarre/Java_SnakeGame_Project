
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * 
 * @author FPT-ACER
 */
public class SnakeGame {
    public static void main(String[] args) {
        // TODO code application logic here
        SwingUtilities.invokeLater(() -> {
            Menu menu = new Menu();
            JFrame frame = new JFrame("Dragon Eat Noodles Game");
            ImageIcon icon = new ImageIcon("img/snakehead.png"); 
            frame.setIconImage(icon.getImage());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(610, 630);
            frame.setLocationRelativeTo(null);
            frame.add(menu);
            frame.setVisible(true);
        });
    }
    
}
