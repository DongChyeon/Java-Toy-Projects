package ShootingGame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class ShootingGame extends JFrame {
	private Image bufferImage;
	private Graphics screenGraphic;
	
	private Image mainScreen = new ImageIcon("src/images/main_screen.png").getImage();
	private Image gameScreen = new ImageIcon("src/images/game_screen.png").getImage();
	private Image loadingScreen = new ImageIcon("src/images/loading_screen.png").getImage();
	
	private boolean isMainScreen, isGameScreen, isLoadingScreen;
	
	public static Game game = new Game();
	
	public ShootingGame() {
		setTitle("Shooting Game");
		setUndecorated(true);
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		setResizable(false);			
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setLayout(null);
		
		init();	// 초기화
	}
	
	private void init() {
		isMainScreen = true;
		isGameScreen = false;
		isLoadingScreen = false;
		
		addKeyListener(new KeyListener());
		setFocusable(true);
	}
	
	private void gameStart() {
		isMainScreen = false;
		isLoadingScreen = true;
		
		// 로딩 화면
		Timer timer1 = new Timer();
		TimerTask task1 = new TimerTask() {
			@Override
			public void run() {
				isLoadingScreen = false;
				isGameScreen = true;
				game.start();	// 게임 클래스의 run 메소드 실행
			}
		};
		timer1.schedule(task1, 3000);	// 3초 뒤에 실행
	}
	
	public void paint(Graphics g) {
		bufferImage = createImage(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		screenGraphic = bufferImage.getGraphics();
		screenDraw(screenGraphic);
		g.drawImage(bufferImage, 0, 0, null);
	}	// 더블 버퍼링
	
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
			switch(e.getKeyCode()) {
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
			case KeyEvent.VK_ENTER:
				if (isMainScreen) gameStart();
				break;
			case KeyEvent.VK_SPACE:
				game.setShooting(true);
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;
			}
		}
		
		public void keyReleased(KeyEvent e) {
			switch(e.getKeyCode()) {
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