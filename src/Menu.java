import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JPanel {
    private static final short SCREEN_WIDTH = 600;
    private static final short SCREEN_HEIGHT = 600;
    private JButton startButton;
    private JButton exitButton;
    private Frame frame;

    public Menu(Frame frame) {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(new Color(0, 102, 204));
        this.frame = frame;
        initializeComponents();
    }

    private void initializeComponents() {
        startButton = new JButton("Start Game");
        startButton.setForeground(new Color(255,229,204));
        startButton.setBackground(new Color(0, 128, 255));
        startButton.setBorder(BorderFactory.createLineBorder(new Color(255, 204, 153), 6));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.startGame();
            }
        });
        startButton.setBounds(SCREEN_HEIGHT / 4, SCREEN_WIDTH / 4, 200, 75);

        Font buttonFont = new Font("Monotype Corsiva", Font.BOLD, 32);
        startButton.setFont(buttonFont);

        setLayout(null);
        add(startButton);

        exitButton = new JButton("Exit");
        exitButton.setForeground(new Color(255, 229, 204));
        exitButton.setBackground(new Color(0, 128, 255));
        exitButton.setBorder(BorderFactory.createLineBorder(new Color(255, 204, 153), 6));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        exitButton.setBounds(SCREEN_HEIGHT / 4, SCREEN_WIDTH / 2, 200, 75);
        exitButton.setFont(buttonFont);
        add(exitButton);
    }
}
