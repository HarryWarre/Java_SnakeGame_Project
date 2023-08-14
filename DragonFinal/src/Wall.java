
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Wall {
    private BufferedImage wallImage;

    public Wall() {
        loadWall();
    }

    private void loadWall() {
        try {
            // Tải hình ảnh từ thư mục img trong src
            wallImage = ImageIO.read(new File("img/wall.png"));
        } catch (IOException e) {
        }
    }

    public void drawWall(Graphics g, int wallSize, int screenWidth, int screenHeight) {
    // Vẽ hình ảnh tường trên (top wall)
    for (int i = 0; i < screenWidth; i += wallSize) {
        g.drawImage(wallImage, i, 0, wallSize, wallSize, null);
    }

    // Vẽ hình ảnh tường dưới (bottom wall)
    for (int i = 0; i < screenWidth; i += wallSize) {
        g.drawImage(wallImage, i, screenHeight - wallSize, wallSize, wallSize, null);
    }

    // Vẽ hình ảnh tường trái (left wall)
    for (int i = wallSize; i < screenHeight - wallSize; i += wallSize) {
        g.drawImage(wallImage, 0, i, wallSize, wallSize, null);
    }

    // Vẽ hình ảnh tường phải (right wall)
    for (int i = wallSize; i < screenHeight - wallSize; i += wallSize) {
        g.drawImage(wallImage, screenWidth - wallSize, i, wallSize, wallSize, null);
    }
}
}
