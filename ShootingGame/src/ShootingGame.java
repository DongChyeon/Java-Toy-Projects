import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

public class ShootingGame extends JFrame {
    private Image bufferImage;
    private Graphics screenGraphic;

    private Image mainScreen = new ImageIcon("src/images/main_screen.png").getImage();
    private Image loadingScreen = new ImageIcon("src/images/loading_screen.png").getImage();
    private Image gameScreen = new ImageIcon("src/images/game_screen.png").getImage();

    private boolean isMainScreen, isLoadingScreen, isGameScreen;

    private Game game = new Game();

    private Audio backgroundMusic;

    public ShootingGame() {
        setTitle("Shooting Game");
        setUndecorated(true);
        setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLayout(null);

        init();
    }

    private void init() {
        isMainScreen = true;
        isLoadingScreen = false;
        isGameScreen = false;

        backgroundMusic = new Audio("src/audio/menuBGM.wav", true);
        backgroundMusic.start();

        addKeyListener(new KeyListener());
    }

    private void gameStart() {
        isMainScreen = false;
        isLoadingScreen = true;

        Timer loadingTimer = new Timer();
        TimerTask loadingTask = new TimerTask() {
            @Override
            public void run() {
                backgroundMusic.stop();
                isLoadingScreen = false;
                isGameScreen = true;
                game.start();
            }
        };
        loadingTimer.schedule(loadingTask, 3000);
    }

    public void paint(Graphics g) {
        bufferImage = createImage(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
        screenGraphic = bufferImage.getGraphics();
        screenDraw(screenGraphic);
        g.drawImage(bufferImage, 0, 0, null);
    }

    public void screenDraw(Graphics g) {
        if (isMainScreen) {
            g.drawImage(mainScreen, 0, 0, null);
        }
        if (isLoadingScreen) {
            g.drawImage(loadingScreen, 0, 0, null);
        }
        if (isGameScreen) {
            g.drawImage(gameScreen, 0, 0, null);
            game.gameDraw(g);
        }
        this.repaint();
    }

    class KeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    game.setUp(true);
                    break;
                case KeyEvent.VK_S:
                    game.setDown(true);
                    break;
                case KeyEvent.VK_A:
                    game.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    game.setRight(true);
                    break;
                case KeyEvent.VK_R:
                    if (game.isOver()) game.reset();
                    break;
                case KeyEvent.VK_SPACE:
                    game.setShooting(true);
                    break;
                case KeyEvent.VK_ENTER:
                    if (isMainScreen) gameStart();
                    break;
                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
                    break;
            }
        }

        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    game.setUp(false);
                    break;
                case KeyEvent.VK_S:
                    game.setDown(false);
                    break;
                case KeyEvent.VK_A:
                    game.setLeft(false);
                    break;
                case KeyEvent.VK_D:
                    game.setRight(false);
                    break;
                case KeyEvent.VK_SPACE:
                    game.setShooting(false);
                    break;
            }
        }
    }
}
