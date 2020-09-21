package RSP;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Game extends Thread {
	private Image playerImage;
	private Image enemyImage;
	private Image background = new ImageIcon(Main.class.getResource("../images/gameScreen.png")).getImage();
	private Image winImage = new ImageIcon(Main.class.getResource("../images/youWin.png")).getImage();
	private Image loseImage = new ImageIcon(Main.class.getResource("../images/youLose.png")).getImage();
	private boolean left, right, shooting;
	private boolean win, lose;
	
	private int playerX, playerY, playerHP;
	private int playerWidth, playerHeight, playerSpeed;
	private int enemyX, enemyY, enemyHP;
	private int enemyWidth, enemyHeight, enemySpeed, enemyMove;
	
	private int delay;
	private long pretime;
	private int cnt;	// 주기 컨트롤
	
	String playerStatus;
	String enemyStatus;
	
	ArrayList<playerBullet> playerBulletList = new ArrayList<playerBullet>();
	ArrayList<enemyBullet> enemyBulletList = new ArrayList<enemyBullet>();
	
	playerBullet pB;
	enemyBullet eB;
	
	public void backgroundDraw(Graphics2D g) {
		g.drawImage(background, 0, 0, null);
	}
	
	public void playerDraw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(playerX, playerY-20, (int)(playerHP/2), 10);
		g.drawImage(playerImage, playerX, playerY, null);
		
		for(int i=0; i<playerBulletList.size(); i++) {
			pB = (playerBullet)(playerBulletList.get(i));
			g.drawImage(pB.bulletImage, pB.x, pB.y, null);
		}
	}
	
	public void enemyDraw(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillRect(enemyX, enemyY-20, (int)(enemyHP/2), 10);
		g.drawImage(enemyImage, enemyX, enemyY, null);
		
		for(int i=0; i<enemyBulletList.size(); i++) {
			eB = (enemyBullet)(enemyBulletList.get(i));
			g.drawImage(eB.bulletImage, eB.x, eB.y, null);
		}
	}
	
	public void gameInfoDraw(Graphics2D g) {
		if(win)
			g.drawImage(winImage, 100, 210, null);
		if(lose)
			g.drawImage(loseImage, 100, 210, null);
	}
	
	@Override
	public void run() {
		Init();
		while (!lose && !win) {
			keyProcess();
			playerBulletProcess();
			enemyBulletProcess();
			enemyMoveProcess();
			
			pretime = System.currentTimeMillis();
			if (System.currentTimeMillis() - pretime < delay) {
				try {
					Thread.sleep(delay - System.currentTimeMillis() + pretime);
					cnt++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	// 일정한 주기로 cnt가 증가하게 유지
		}
	}
	
	public void Init() {
		playerHP = 100;
		enemyHP = 100;
		playerSpeed = 5;
		enemySpeed = 5;
		delay = 20;
		
		switch(playerStatus) {
		case "rock":
			playerImage = new ImageIcon(Main.class.getResource("../images/rock.png")).getImage();
			playerWidth = 50;
			playerHeight = 59;
			break;
		case "scissor":
			playerImage = new ImageIcon(Main.class.getResource("../images/scissor.png")).getImage();
			playerWidth = 50;
			playerHeight = 77;
			break;
		case "paper":
			playerImage = new ImageIcon(Main.class.getResource("../images/paper.png")).getImage();
			playerWidth = 50;
			playerHeight = 60;
			break;
		}
		System.out.println(playerStatus);
		
		switch(enemyStatus) {
		case "rock":
			enemyImage = new ImageIcon(Main.class.getResource("../images/rockReverse.png")).getImage();
			enemyWidth = 50;
			enemyHeight = 59;
			break;
		case "scissor":
			enemyImage = new ImageIcon(Main.class.getResource("../images/scissorReverse.png")).getImage();
			enemyWidth = 50;
			enemyHeight = 77;
			break;
		case "paper":
			enemyImage = new ImageIcon(Main.class.getResource("../images/paperReverse.png")).getImage();
			enemyWidth = 50;
			enemyHeight = 60;
			break;
		}
		System.out.println(enemyStatus);
		
		playerX = 225;
		playerY = 400;
		enemyX = 225;
		enemyY = 60;
	}
	
	public void setPlayerStatus(String playerStatus) {
		this.playerStatus = playerStatus;
	}

	public void setEnemyStatus(String enemyStatus) {
		this.enemyStatus = enemyStatus;
	}

	private void keyProcess() {
		if (left && playerX-playerSpeed > 0) {
			playerX-=playerSpeed;
		}
		if (right && playerX+playerSpeed+playerWidth < 500) {
			playerX+=playerSpeed;
		}
		if (shooting) {
			if (cnt%30 == 0) {	// 안에 있는 숫자가 작을수록 공격주기가 짧음
				pB = new playerBullet(playerX+15, playerY-10);
				playerBulletList.add(pB);
			}
		else
			return;
		}
	}	
	
	private void playerBulletProcess() {
		for(int i=0; i<playerBulletList.size(); i++) {
			pB = (playerBullet)(playerBulletList.get(i));
			pB.fire();
			if(pB.y <= enemyY + enemyHeight && pB.x <= enemyX+enemyWidth && pB.x+pB.width >= enemyX) {
				RSP.audio.playSound("src/audio/hitSound.wav");
				enemyHP-=pB.attack;
				playerBulletList.remove(playerBulletList.get(i));
				if(enemyHP <= 0) {
					RSP.audio.playSound("src/audio/winSound.wav");
					win = true;
				}
			}
			if(pB.y <= 0) {
				playerBulletList.remove(playerBulletList.get(i));
			}
		}
	}
	
	private void enemyBulletProcess() {
		if(cnt%30 == 0) {
			eB = new enemyBullet(enemyX+15, enemyY+enemyHeight+10);
			enemyBulletList.add(eB);
		}
		for(int i=0; i<enemyBulletList.size(); i++) {
			eB = (enemyBullet)(enemyBulletList.get(i));
			eB.fire();
			if(eB.y <= playerY+20 &&eB.y+20 >= playerY && eB.x <= playerX+playerWidth && eB.x+eB.width >= playerX) {
				RSP.audio.playSound("src/audio/hittenSound.wav");
				playerHP-=eB.attack;
				enemyBulletList.remove(enemyBulletList.get(i));
				if(playerHP <= 0) {
					RSP.audio.playSound("src/audio/loseSound.wav");
					lose = true;
				}
			}
			if(eB.y <= 0) {
				enemyBulletList.remove(enemyBulletList.get(i));
			}
		}
	}
	
	private void enemyMoveProcess() {
		if (cnt%20 == 0)
			enemyMove = (int)(Math.random()*2)+1;
		switch(enemyMove) {
			case 1:
				if(enemyX-enemySpeed > 0)
					enemyX-=enemySpeed;
				break;
			case 2:
				if(enemyX+enemySpeed+enemyWidth < 500)
					enemyX+=enemySpeed;
				break;
		}
	}

	public void true_Left() {
		if(left)
			return;
		left = true;
	}
	
	public void true_Right() {
		if(right)
			return;
		right = true;
	}
	
	public void true_Shooting() {
		if(shooting)
			return;
		shooting = true;
	}
	
	public void false_Left() {
		if(!left)
			return;
		left = false;
	}
	
	public void false_Right() {
		if(!right)
			return;
		right = false;
	}
	
	public void false_Shooting() {
		if(!shooting)
			return;
		shooting = false;
	}
}
