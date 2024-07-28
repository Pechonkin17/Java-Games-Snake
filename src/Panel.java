import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Panel extends JPanel implements ActionListener {
    private Image[] snakeHeadImages = new Image[4];
    private Image appleImage;

    private static final short SCREEN_WIDTH = 600;
    private static final short SCREEN_HEIGHT = 600;
    private static final short UNIT_SIZE = 40;
    private static final short GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    private static short DELAY = 400;
    private final short x[] = new short[GAME_UNITS];
    private final short y[] = new short[GAME_UNITS];
    private short bodyParts = 6;
    private short appleX;
    private short appleY;
    private Direction direction = Direction.RIGHT;
    private boolean running = false;
    private Timer timer;
    private boolean canChangeDirection = true;

    public Panel(){
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(new Color(255, 255, 153));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        loadImage();

        startGame();
    }

    // load images
    private void loadImage() {
        ImageIcon headUpIcon = new ImageIcon("snake_head_up.png");
        snakeHeadImages[0] = headUpIcon.getImage();

        ImageIcon headDownIcon = new ImageIcon("snake_head_down.png");
        snakeHeadImages[1] = headDownIcon.getImage();

        ImageIcon headLeftIcon = new ImageIcon("snake_head_left.png");
        snakeHeadImages[2] = headLeftIcon.getImage();

        ImageIcon headRightIcon = new ImageIcon("snake_head_right.png");
        snakeHeadImages[3] = headRightIcon.getImage();

        ImageIcon appleIcon = new ImageIcon("apple.png");
        appleImage = appleIcon.getImage();
    }

    // start game
    public void startGame(){
        newApple(new Random());

        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        draw(graphics);

        graphics.dispose();
    }

    // draw snake components
    private void draw(Graphics graphics){
        if (running) {
            graphics.drawImage(appleImage, appleX, appleY, UNIT_SIZE, UNIT_SIZE, this);

            graphics.drawImage(snakeHeadImages[getDirectionIndex()], x[0], y[0], UNIT_SIZE, UNIT_SIZE, null);

            // draw gradient body
            Graphics2D g2d = (Graphics2D) graphics;

            int startX = x[1];
            int startY = y[1];
            int endX = x[bodyParts - 1];
            int endY = y[bodyParts - 1];

            GradientPaint gradient = new GradientPaint(startX, startY, new Color(45, 180, 0),
                    endX, endY, new Color(153, 255, 51));

            g2d.setPaint(gradient);

            for (short i = 1; i < bodyParts; i++) {
                g2d.fillRect(x[i] + UNIT_SIZE / 4, y[i] + UNIT_SIZE / 4, UNIT_SIZE / 2, UNIT_SIZE / 2);
            }
        } else gameOver(graphics);
    }

    // change directions of snake head
    private int getDirectionIndex() {
        switch (direction) {
            case UP: return 0;
            case DOWN: return 2;
            case LEFT: return 1;
            case RIGHT: return 3;
            default: return 0;
        }
    }

    // spawn random apple
    private void newApple(Random random) {
        boolean appleGenerated = false;
        while (!appleGenerated) {
            appleX = (short) (random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE);
            appleY = (short) (random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE);

            appleGenerated = true;
            for (short i = 0; i < bodyParts; i++) {
                if (appleX == x[i] && appleY == y[i]) {
                    appleGenerated = false;
                    break;
                }
            }
        }
    }

    // change move of all snake
    private void move(){
        for (short i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case UP:
                y[0] -= UNIT_SIZE;
                break;

            case DOWN:
                y[0] += UNIT_SIZE;
                break;

            case LEFT:
                x[0] -= UNIT_SIZE;
                break;

            case RIGHT:
                x[0] += UNIT_SIZE;
                break;
        }
    }

    // check is apple eaten
    private void checkApple(){
        if (x[0] == appleX && y[0] == appleY){
            bodyParts++;

            newApple(new Random());
        }
    }

    // check collosoions
    private void checkCollisions(){
        if (x[0] < 0) {
            x[0] = SCREEN_WIDTH - UNIT_SIZE;
        } else if (x[0] >= SCREEN_WIDTH) {
            x[0] = 0;
        }

        if (y[0] < 0) {
            y[0] = SCREEN_HEIGHT - UNIT_SIZE;
        } else if (y[0] >= SCREEN_HEIGHT) {
            y[0] = 0;
        }

        for (short i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }

        if (!running){
            timer.stop();
        }
    }

    // loss screen
    private void gameOver(Graphics graphics){
        for (Image snakeHead: snakeHeadImages){
            snakeHead.flush();
        }
        appleImage.flush();

        short applesEaten = (short) (bodyParts - 6);

        graphics.setColor(new Color(45, 180,0));
        graphics.setFont(new Font("Monotype Corsiva", Font.BOLD, 70));
        FontMetrics metrics_score = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + applesEaten,
                (SCREEN_WIDTH - metrics_score.stringWidth("Score: " + applesEaten))/2,  SCREEN_HEIGHT / 2);
    }

    // game
    @Override
    public void actionPerformed(ActionEvent event) {
        if (running){
            move();
            checkCollisions();
            checkApple();
        }
        repaint();
    }

    // keyboard set up
    private class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent keyEvent){
            if (canChangeDirection) {
                switch (keyEvent.getKeyCode()){
                    case KeyEvent.VK_DOWN -> {
                        if (direction != Direction.UP){
                            direction = Direction.DOWN;
                        }
                    }

                    case KeyEvent.VK_UP -> {
                        if (direction != Direction.DOWN){
                            direction = Direction.UP;
                        }
                    }

                    case KeyEvent.VK_LEFT -> {
                        if (direction != Direction.RIGHT){
                            direction = Direction.LEFT;
                        }
                    }

                    case KeyEvent.VK_RIGHT -> {
                        if (direction != Direction.LEFT){
                            direction = Direction.RIGHT;
                        }
                    }
                }
            }

            // delay for direction
            canChangeDirection = false;
            Timer directionTimer = new Timer(150, e -> canChangeDirection = true);
            directionTimer.setRepeats(false);
            directionTimer.start();
        }
    }
}
