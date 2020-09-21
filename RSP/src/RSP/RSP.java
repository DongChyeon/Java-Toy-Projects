package RSP;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.util.Timer;
import java.util.TimerTask;

public class RSP extends JFrame {
	
	private Image screenImage;
	private Graphics screenGraphic;
	
	private Image mainImage = new ImageIcon(Main.class.getResource("../images/mainScreen.png")).getImage();
	private Image loadingImage = new ImageIcon(Main.class.getResource("../images/loadingScreen.png")).getImage();
	private Image title = new ImageIcon(Main.class.getResource("../images/title.png")).getImage();
	
	private Image rock = new ImageIcon(Main.class.getResource("../images/rockLabel.png")).getImage();
	private Image scissor = new ImageIcon(Main.class.getResource("../images/scissorLabel.png")).getImage();
	private Image paper = new ImageIcon(Main.class.getResource("../images/paperLabel.png")).getImage();
	
	private JLabel menuBar = new JLabel(new ImageIcon(Main.class.getResource("../images/menuBar.png")));
	private JButton quitButton = new JButton(new ImageIcon(Main.class.getResource("../images/quitButton.png")));
	private JButton rockButton = new JButton(new ImageIcon(Main.class.getResource("../images/rockButton.png")));
	private JButton scissorButton = new JButton(new ImageIcon(Main.class.getResource("../images/scissorButton.png")));
	private JButton paperButton = new JButton(new ImageIcon(Main.class.getResource("../images/paperButton.png")));
	
	private boolean isMainScreen = true;
	private boolean isLoadingScreen, isGameScreen = false;
	
	private int random;
	
	private String player, enemy;
	
	public static Game game = new Game();
	public static Audio audio = new Audio();
	
	public RSP() {
		setTitle("가위바위보는 실력겜이다.");
		setUndecorated(true);
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setLayout(null);
		setBackground(new Color(0, 0, 0, 0));
		
		menuBar.setBounds(0, 0, 500, 30);
		quitButton.setBounds(470, 0, 30, 30);
		quitButton.setBorderPainted(false);
		quitButton.setContentAreaFilled(false);
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				System.exit(0);
			}
		});
		rockButton.setBounds(50, 300, 100, 100);
		rockButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameLoading();
				gameStart();
			}
		});
		scissorButton.setBounds(200, 300, 100, 100);
		scissorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameLoading();
				gameStart();
			}
		});
		paperButton.setBounds(350, 300, 100, 100);
		paperButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameLoading();
				gameStart();
			}
		});
		
		add(quitButton);
		add(menuBar);
		add(rockButton);
		add(scissorButton);
		add(paperButton);
	}
	
	public void paint(Graphics g) {
		screenImage = createImage(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		screenGraphic = screenImage.getGraphics();
		screenDraw((Graphics2D) screenGraphic);
		g.drawImage(screenImage, 0, 0, null);
	}
	
	public void screenDraw(Graphics2D g) {
		if(isMainScreen) {
			g.drawImage(mainImage, 0, 0, null);
			g.drawImage(title, 0, 100, null);
		}
		else if(isLoadingScreen) {
			g.drawImage(loadingImage, 0, 0, null);
			switch(player) {
			case "rock":
				g.drawImage(rock, 30, 200, null);
				break;
			case "scissor":
				g.drawImage(scissor, 30, 200, null);
				break;
			case "paper":
				g.drawImage(paper, 30, 200, null);
				break;
			}
			switch(enemy) {
			case "rock":
				g.drawImage(rock, 270, 200, null);
				break;
			case "scissor":
				g.drawImage(scissor, 270, 200, null);
				break;
			case "paper":
				g.drawImage(paper, 270, 200, null);
				break;
			}
		}
		else if(isGameScreen) {
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 500, 500);
			RSP.game.backgroundDraw(g);
			RSP.game.playerDraw(g);
			RSP.game.enemyDraw(g);
			RSP.game.gameInfoDraw(g);
		}
		paintComponents(g);
		this.repaint();
	}
	
	private void gameLoading() {
		Timer timer1 = new Timer();
		TimerTask task1 = new TimerTask() {

			@Override
			public void run() {
				audio.playSound("src/audio/boxingBell.wav");
		    	RSP.game.setPlayerStatus("scissor");
				player = "scissor";
				random = (int)(Math.random()*3)+1;
				switch (random) {
				case 1:
					RSP.game.setEnemyStatus("rock");
					enemy = "rock";
					break;
				case 2:
					RSP.game.setEnemyStatus("scissor");
					enemy = "scissor";
					break;
				case 3:
					RSP.game.setEnemyStatus("paper");
					enemy = "paper";
					break;
				}
				isLoadingScreen = true;
				isMainScreen = false;
				
				rockButton.setVisible(false);
				scissorButton.setVisible(false);
				paperButton.setVisible(false);
			}
		};
		
		timer1.schedule(task1, 1000);
	}
	
	private void gameStart() {
		Timer timer2 = new Timer();
		TimerTask task2 = new TimerTask() {

		    @Override
			public void run() {
				isGameScreen = true;
				isLoadingScreen = false;
				isMainScreen = false;
				addKeyListener(new KeyListener());
				setFocusable(true);
				requestFocus();
				
				game.start(); 
			}
		};
		
		timer2.schedule(task2, 3000);
	}
}
