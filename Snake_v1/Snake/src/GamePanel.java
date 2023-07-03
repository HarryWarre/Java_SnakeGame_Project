
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import javax.swing.*;
import java.util.Random;
import java.util.Set;

import javax.swing.JPanel;

public final class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 30;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static int DELAY = 150;//speed

    final int X[] = new int[GAME_UNITS];
    final int Y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    int wallSize = UNIT_SIZE;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
        X[0] =  UNIT_SIZE;
        Y[0] =  UNIT_SIZE;

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
    if (running) {
        // Vẽ ma trận
        for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
        }

        g.setColor(Color.RED);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                g.setColor(Color.GREEN);
                g.fillRect(X[i], Y[i], UNIT_SIZE, UNIT_SIZE);
            } else {
                g.setColor(Color.BLUE);
                g.fillRect(X[i], Y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }

        // Vẽ tường
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, SCREEN_WIDTH, wallSize); // Tường trên
        g.fillRect(0, SCREEN_HEIGHT - wallSize, SCREEN_WIDTH, wallSize); // Tường dưới
        g.fillRect(0, wallSize, wallSize, SCREEN_HEIGHT - 2 * wallSize); // Tường trái
        g.fillRect(SCREEN_WIDTH - wallSize, wallSize, wallSize, SCREEN_HEIGHT - 2 * wallSize); // Tường phải

        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metrics = getFontMetrics(getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
    } else {
        gameOver(g);
    }
}


    public void newApple() {
    boolean appleInWall = true;
    while (appleInWall) {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE - 2)) * UNIT_SIZE + UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE - 2)) * UNIT_SIZE + UNIT_SIZE;

        // Kiểm tra xem táo có trùng với vị trí của tường không
        if (appleX >= wallSize && appleX < SCREEN_WIDTH - wallSize &&
            appleY >= wallSize && appleY < SCREEN_HEIGHT - wallSize) {
            appleInWall = false;
        }
    }
}

    //di chuyển
    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            X[i] = X[i - 1];
            Y[i] = Y[i - 1];
        }

        switch (direction) {
            case 'U' -> {
                if (Y[0] > UNIT_SIZE) {
                    Y[0] = Y[0] - UNIT_SIZE;
                } else {
                    running = false; // Đụng vào tường trên, kết thúc trò chơi
                }
            }

            case 'D' -> {
                if (Y[0] < SCREEN_HEIGHT - UNIT_SIZE * 2) {
                    Y[0] = Y[0] + UNIT_SIZE;
                } else {
                    running = false; // Đụng vào tường dưới, kết thúc trò chơi
                }
            }

            case 'L' -> {
                if (X[0] > UNIT_SIZE) {
                    X[0] = X[0] - UNIT_SIZE;
                } else {
                    running = false; // Đụng vào tường trái, kết thúc trò chơi
                }
            }

            case 'R' -> {
                if (X[0] < SCREEN_WIDTH - UNIT_SIZE * 2) {
                    X[0] = X[0] + UNIT_SIZE;
                } else {
                    running = false; // Đụng vào tường phải, kết thúc trò chơi
                }
            }
        }
    }

    //check ăn táo
    public void checkApple() {
        if (X[0] == appleX && Y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            increaseSpeed();
            newApple();
        }
    }

    private void increaseSpeed() {
        if (applesEaten % 5 == 0 && DELAY > 10) {
            // Tăng tốc độ sau khi ăn đủ số lượng thức ăn được xác định
            if (DELAY >= 10) {
                DELAY -= 15; // Giảm thời gian delay để tăng tốc độ
            }
            timer.setDelay(DELAY); // Cập nhật tốc độ của timer
        }
    }

    //check va chạm
    public void checkCollisions() {
        //cắn đuôi
        for (int i = bodyParts; i > 0; i--) {
            if ((X[0] == X[i]) && (Y[0] == Y[i])) {
                running = false;
            }
        }
        //if(!snakeHeadTouchesWall()) running = false;
        //đụng lề trái
        if (X[0] < 0) {
            running = false;
        }

        //đụng lề phải
        if (X[0] > SCREEN_WIDTH - 2 * UNIT_SIZE) {
            running = false;
        }

        //đụng lề trên
        if (Y[0] < 0) {
            running = false;
        }

        //đụng lề dưới
        if (Y[0] > SCREEN_HEIGHT - 2 * UNIT_SIZE) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 70));
        FontMetrics metrics = getFontMetrics(getFont());
        g.drawString("Thua rùi :((", (SCREEN_WIDTH - metrics.stringWidth("Thua rùi")) / 3, SCREEN_HEIGHT / 2);
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 3, g.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> {
                    if (direction != 'R') {
                        direction = 'L';
                    }
                }

                case KeyEvent.VK_RIGHT -> {
                    if (direction != 'L') {
                        direction = 'R';
                    }
                }

                case KeyEvent.VK_UP -> {
                    if (direction != 'D') {
                        direction = 'U';
                    }
                }

                case KeyEvent.VK_DOWN -> {
                    if (direction != 'U') {
                        direction = 'D';
                    }
                }
            }
        }
    }
}
