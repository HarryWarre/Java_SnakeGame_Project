/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author PhanHoangVietHS
 */
import java.awt.*;
public class Apple {
    private final int x;
    private final int y;
    private final int size;

    public Apple(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw(Graphics g) {
        g.drawImage(Skin.imgapple, x, y,size,size,null);
        //g.fillOval(x, y, size, size);
    }
}