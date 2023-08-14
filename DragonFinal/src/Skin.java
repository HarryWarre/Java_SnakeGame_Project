
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Duy Long
 */
public class Skin {

    public static Image imghead;
    public static Image imgbody;
    public static Image imgapple;

    public static void loadImage() {
        try {
            imghead = ImageIO.read(new File("img/snakehead.png"));
            imgbody = ImageIO.read(new File("img/snakebody.png"));
            imgapple = ImageIO.read(new File("img/apple.png"));
        } catch (IOException e) {
        }
    }
}
