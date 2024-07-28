import javax.swing.JFrame;

// GAME WINDOW
public class Frame extends JFrame {
    private Panel panel;
    private Menu menu;

    Frame() {
        panel = new Panel();
        menu = new Menu(this);

        add(menu);
        setTitle("Snake");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void startGame() {
        getContentPane().remove(menu);
        getContentPane().add(panel);
        panel.requestFocusInWindow();
        panel.startGame();
        pack();
    }
}
