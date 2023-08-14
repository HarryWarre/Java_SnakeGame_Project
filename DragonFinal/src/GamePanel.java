
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;
import org.json.simple.parser.ParseException;

public final class GamePanel extends JPanel implements ActionListener {

    static int SCREEN_WIDTH = 600;
    static int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 30;
    static int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    int DELAY = 180; // Tốc độ

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
    private final MyKeyAdapter keyAdapter;

    boolean resetFlag = false;
    private boolean moving = false;

    private Apple apple;
    private java.util.List<Point> apples;
    private java.util.List<Point> newApplePositions;

    public static final Color DARK_GREEN = new Color(0, 153, 0);

    private final Wall wallDrawer = new Wall();
    private final SoundManager soundManager;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        keyAdapter = new MyKeyAdapter();
        this.addKeyListener(new MyKeyAdapter());
        soundManager = new SoundManager();
        startGame();
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustGameSize();
            }
        });
    }

    private void adjustGameSize() {
        int newWidth = getWidth();
        int newHeight = getHeight();

        // Cập nhật lại số lượng ô theo tỉ lệ mới
        int newGameUnitsX = newWidth / UNIT_SIZE;
        int newGameUnitsY = newHeight / UNIT_SIZE;
        GAME_UNITS = newGameUnitsX * newGameUnitsY;

        // Tạo một danh sách mới chứa tọa độ của ô mới
        newApplePositions = new ArrayList<>();
        for (int x = 0; x < newGameUnitsX; x++) {
            for (int y = 0; y < newGameUnitsY; y++) {
                newApplePositions.add(new Point(x * UNIT_SIZE, y * UNIT_SIZE));
            }
        }

        // Loại bỏ các tọa độ cũ không còn nằm trong màn hình mới
        apples.removeIf(point -> !newApplePositions.contains(point));

        // Cập nhật kích thước và vị trí của các ô
        for (int i = 0; i < bodyParts; i++) {
            X[i] = UNIT_SIZE;
            Y[i] = UNIT_SIZE;
        }

        // Cập nhật kích thước màn hình
        SCREEN_WIDTH = newWidth;
        SCREEN_HEIGHT = newHeight;

        // Cập nhật táo
        for (Point apple : apples) {
            apple.x = apple.x * newWidth / SCREEN_WIDTH;
            apple.y = apple.y * newHeight / SCREEN_HEIGHT;
        }

        // Cập nhật tường
        wallSize = wallSize * newWidth / SCREEN_WIDTH;
    }

    public void startGame() {
        soundManager.playBackgroundMusic();
        apples = new ArrayList<>();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
        X[0] = UNIT_SIZE;
        Y[0] = UNIT_SIZE;
        Skin.loadImage();
        newApple();
        System.out.println("START GAME:" + DELAY);

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
            for (int i = 0; i < getWidth() / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, getHeight());
            }
            for (int i = 0; i < getHeight() / UNIT_SIZE; i++) {
                g.drawLine(0, i * UNIT_SIZE, getWidth(), i * UNIT_SIZE);
            }

            // Cập nhật kích thước và vẽ táo
            for (Point _apple : apples) {
                apple = new Apple(_apple.x, _apple.y, UNIT_SIZE); // Cập nhật kích thước
                apple.draw(g); // Vẽ táo
            }

            // Cập nhật kích thước và vẽ rắn
            for (int i = 0; i < bodyParts; i++) {
                int scaledX = X[i]; // Tính toán tọa độ X mới dựa trên kích thước màn hình mới
                int scaledY = Y[i]; // Tính toán tọa độ Y mới dựa trên kích thước màn hình mới
                int scaledSize = UNIT_SIZE;// Sử dụng kích thước mới tính toán

                if (i == 0) {
                    g.drawImage(Skin.imghead, scaledX, scaledY, scaledSize, scaledSize, null); // Vẽ đầu rắn
                } else {
                    g.drawImage(Skin.imgbody, scaledX, scaledY, scaledSize, scaledSize, null); // Vẽ thân rắn
                }
            }

            // Cập nhật kích thước và vẽ tường
            wallDrawer.drawWall(g, wallSize, getWidth(), getHeight());

            // Vẽ thông tin điểm
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            FontMetrics metrics = getFontMetrics(getFont());
            g.drawString("Score: " + applesEaten, (getWidth() - metrics.stringWidth("Score: " + applesEaten)) / 10, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        boolean appleInWall;
        int attempts = 30;
        for (int i = 0; i < attempts; i++) {
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
                DELAY -= 10; // Giảm thời gian delay để tăng tốc độ
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

        soundManager.stopBackgroundMusic();
        soundManager.playGameOverSound();

        // Tạo font chữ mới từ tệp font đã tải về
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("font/Adonais.ttf")).deriveFont(70f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);

            g.setFont(customFont);
            
            Font customFont2 = Font.createFont(Font.TRUETYPE_FONT, new File("font/PlayMeGames.ttf")).deriveFont(70f);
            GraphicsEnvironment ge2 = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge2.registerFont(customFont2);

            g.setFont(customFont2);
        } catch (IOException | FontFormatException e) {}

        g.setColor(Color.red);
        Font largeFont = new Font("Adonais", Font.BOLD, 100);
        g.setFont(largeFont);
        FontMetrics largeFontMetrics = g.getFontMetrics(largeFont);
        String gameOverMessage = "Game Over ";
        int gameOverMessageX = (SCREEN_WIDTH - largeFontMetrics.stringWidth(gameOverMessage) + 50 ) / 2;
        int gameOverMessageY = SCREEN_HEIGHT - 525;
        g.drawString(gameOverMessage, gameOverMessageX, gameOverMessageY);

        g.setColor(Color.white);
        String scoreMessage = "Score: " + applesEaten;
        int scoreMessageX = (SCREEN_WIDTH - largeFontMetrics.stringWidth(scoreMessage)) / 2;
        int scoreMessageY = gameOverMessageY + largeFontMetrics.getHeight() + 20;
        g.drawString(scoreMessage, scoreMessageX, scoreMessageY);

        PlayerManager.savePlayer(applesEaten);

        // Restart message
        g.setColor(DARK_GREEN);
        Font smallFont = new Font("PlayMeGames",Font.ITALIC,30);
        g.setFont(smallFont);
        FontMetrics smallFontMetrics = g.getFontMetrics(smallFont);
        String restartMessage = "Press SPACE to restart";
        int restartMessageX = (SCREEN_WIDTH - smallFontMetrics.stringWidth(restartMessage)) / 2;
        int restartMessageY = SCREEN_HEIGHT - 325;
        g.drawString(restartMessage, restartMessageX, restartMessageY);

        // ESC message
        String escMessage = "Press ESC to return MENU";
        int escMessageX = (SCREEN_WIDTH - smallFontMetrics.stringWidth(escMessage)) / 2;
        int escMessageY = SCREEN_HEIGHT - 250;
        g.drawString(escMessage, escMessageX, escMessageY);

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
            if (System.currentTimeMillis() - _apple.getTime() >= 3500) {
                applesToRemove.add(_apple);
            }
        }
        apples.removeAll(applesToRemove);
    }

    public void restart() {
        soundManager.playBackgroundMusic();
        apples.clear();
        applesEaten = 0;
        DELAY = 180;
        timer = new Timer(DELAY, this);
        bodyParts = 3;
        direction = 'R';
        newApple();
        running = true;
        timer.start();
        X[0] = UNIT_SIZE;
        Y[0] = UNIT_SIZE;
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                if (running == false) {
                    restart();
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                if (running == false) {
                    removeAll(); // Xóa nút "Bắt đầu chơi" khỏi panel
                    setLayout(new BorderLayout());
                    Menu menu = new Menu();
                    add(menu, BorderLayout.CENTER);
                    revalidate();
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
