import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import org.json.simple.parser.ParseException;

public final class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 30;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    int DELAY = 160;//speed

    final int X[] = new int[GAME_UNITS];
    final int Y[] = new int[GAME_UNITS];
    int bodyParts = 3;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    
    Timer timer;
    Random random;
    int wallSize = UNIT_SIZE;
    private MyKeyAdapter keyAdapter;
    
    boolean resetFlag = false;
    private boolean moving = false;
    
    private Apple apple;
    private java.util.List<Point> apples;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        keyAdapter = new MyKeyAdapter();
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        apples = new ArrayList<>();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
        X[0] = UNIT_SIZE;
        Y[0] = UNIT_SIZE;
        resetSnake();
        newApple();
        System.out.println("START GAME:"+ DELAY);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            draw(g);
        } catch (ParseException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void draw(Graphics g) throws ParseException {
        if (running) {
            // Vẽ ma trận
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }

            g.setColor(Color.RED);
            for (Point _apple : apples) {
                //g.fillOval(_apple.x, _apple.y, UNIT_SIZE, UNIT_SIZE);
                apple = new Apple(_apple.x, _apple.y,  UNIT_SIZE);
                apple.draw(g);
            }

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
        for (int i = 0; i < 10; i++) {
            appleInWall = true;
            while (appleInWall) {
                appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE - 2)) * UNIT_SIZE + UNIT_SIZE;
                appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE - 2)) * UNIT_SIZE + UNIT_SIZE;
                apple = new Apple(appleX, appleY, UNIT_SIZE);
                // Kiểm tra xem táo có trùng với vị trí của tường không
                if (appleX >= wallSize && appleX < SCREEN_WIDTH - wallSize
                        && appleY >= wallSize && appleY < SCREEN_HEIGHT - wallSize) {
                    Point _apple = new Point(apple.getX(), apple.getY());
                if (!apples.contains(_apple)) {
                    apples.add(_apple);
                    appleInWall = false;
                }
                }
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
        moving = false;
    }

    public void registerKeyListener() {
        setFocusable(true);
        requestFocus();
        addKeyListener(keyAdapter);
    }

    //check ăn táo
    public void checkApple() {
        Point head = new Point(X[0], Y[0]);
        java.util.List<Point> eatenApples = new ArrayList<>();
        for (Point apple : apples) {
            if (head.equals(apple)) {
                eatenApples.add(apple);
                applesEaten++;
                increaseSpeed();
                increaseSnakeLength();
            }
        }
        apples.removeAll(eatenApples);

        if (apples.isEmpty()) {
            newApple();
        }
    }

    private void increaseSpeed() {
        if (applesEaten % 5 == 0 && DELAY > 15) {
            // Tăng tốc độ sau khi ăn đủ số lượng thức ăn được xác định
            if (DELAY >= 15) {
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

    public void gameOver(Graphics g) throws ParseException {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 70));
        FontMetrics metrics = getFontMetrics(getFont());
        g.drawString("Thua rùi :((", (SCREEN_WIDTH - metrics.stringWidth("Thua rùi")) / 3, SCREEN_HEIGHT / 2);
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 3, g.getFont().getSize());
        PlayerManager.savePlayer(applesEaten);
        //restart
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.ITALIC, 30));
        FontMetrics metrics1 = getFontMetrics(getFont());
        g.drawString("Nhan SPACE de choi lai ", (SCREEN_WIDTH - metrics1.stringWidth("Nhan SPACE de choi lai")) -300, SCREEN_HEIGHT -100);
        resetSnake();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
            checkAppleDisappearance();
        } else if (e.getSource() == timer && !running) {
            if (resetFlag) {
                resetSnake();
                resetFlag = false;
            }
        }
        repaint();
    }

    private void resetSnake() {
        X[0] = UNIT_SIZE;
        Y[0] = UNIT_SIZE;
        for (int i = 0; i < bodyParts; i++) {
            X[i] = UNIT_SIZE;
            Y[i] = UNIT_SIZE;
        }
    }

    private void increaseSnakeLength() {
        int lastIndex = bodyParts - 1;
        int newX = X[lastIndex] - (X[lastIndex] - X[lastIndex - 1]);
        int newY = Y[lastIndex] - (Y[lastIndex] - Y[lastIndex - 1]);

        X[bodyParts] = newX;
        Y[bodyParts] = newY;
        bodyParts++;
    }

    private void checkAppleDisappearance() {
        java.util.List<Point> applesToRemove = new ArrayList<>();
        for (Point _apple : apples) {
        if (System.currentTimeMillis() - _apple.getTime() >= 2000) {
            applesToRemove.add(_apple);
        }
        }
        apples.removeAll(applesToRemove);
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode()==KeyEvent.VK_SPACE){
                if(running==false){
                    apples.clear();
                    applesEaten=0;
                    bodyParts=3;
                    DELAY=160;
                    direction = 'R';
                    newApple();
                    running = true;
                    timer.start();
                    X[0] = UNIT_SIZE;
                    Y[0] = UNIT_SIZE;
                }    
            }
            if (!moving) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> {
                        if (direction != 'R') {
                            direction = 'L';
                            moving = true; // Đánh dấu rắn đang di chuyển
                        }
                    }

                    case KeyEvent.VK_RIGHT -> {
                        if (direction != 'L') {
                            direction = 'R';
                            moving = true; // Đánh dấu rắn đang di chuyển
                        }
                    }

                    case KeyEvent.VK_UP -> {
                        if (direction != 'D') {
                            direction = 'U';
                            moving = true; // Đánh dấu rắn đang di chuyển
                        }
                    }

                    case KeyEvent.VK_DOWN -> {
                        if (direction != 'U') {
                            direction = 'D';
                            moving = true; // Đánh dấu rắn đang di chuyển
                        }
                    }
                }
            }
        }
    }
    
    class Point {
        int x;
        int y;
        long time;

        Point(int x, int y) {
                this.x = x;
                this.y = y;
                this.time = System.currentTimeMillis();
            }

            public long getTime() {
                return time;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (obj == null || getClass() != obj.getClass()) {
                    return false;
                }
                Point point = (Point) obj;
                return x == point.x && y == point.y;
            }

            @Override
            public int hashCode() {
                return Objects.hash(x, y);
            }   
    }
}
