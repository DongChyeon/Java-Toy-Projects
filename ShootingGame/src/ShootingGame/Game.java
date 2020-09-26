package ShootingGame;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Game extends Thread {
	private int delay = 20;	// 스레드 슬립 주기
	private long pretime;
	
	private int cnt;	// 0.02초마다 증가
	
	private Image player = new ImageIcon("src/images/player.png").getImage();
	
	private int playerX, playerY;	// 플레이어 위치
	private int playerWidth = player.getWidth(null);
	private int playerHeight = player.getHeight(null);	// 플레이어 가로, 세로 크기
	private int playerSpeed = 10;

	private boolean up, down, left, right, shooting;	// 키 눌림
	
	ArrayList<PlayerAttack> playerAttackList = new ArrayList<PlayerAttack>();
	private PlayerAttack playerAttack;
	
	@Override
	public void run() {
		cnt = 0;
		playerX = 10;
		playerY = (1080 - playerHeight) / 2;
		
		while (true) {
			pretime = System.currentTimeMillis();
			if (System.currentTimeMillis() - pretime < delay) {
				try {
					Thread.sleep(delay - System.currentTimeMillis() + pretime);
					keyProcess();
					playerAttackProcess();
					cnt++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 플레이어 움직임을 받아들이는 메소드
	private void keyProcess() {
		if (up && playerY - playerSpeed > 0) playerY -= 10;
		if (down && playerY + playerHeight + playerSpeed < Main.SCREEN_HEIGHT) playerY += 10;
		if (left && playerX - playerSpeed > 0) playerX -= 10;
		if (right && playerX + playerWidth + playerSpeed < Main.SCREEN_WIDTH) playerX += 10;
		if (shooting && cnt % 20 == 0) {
			playerAttack = new PlayerAttack(playerX + 222, playerY + 35);
			playerAttackList.add(playerAttack);
		}	// 0.4초마다 미사일 발사
	}
	
	// 플레이어의 공격을 처리해주는 메소드
	private void playerAttackProcess() {
		for (PlayerAttack attack : playerAttackList) {
			attack.fire();
		}
	}
	
	// 게임에 필요한 것들을 그려주는 메소드
	public void gameDraw(Graphics g) {
		playerDraw(g);
	}
	
	// 플레이어와 플레이어의 공격을 그려주는 메소드
	public void playerDraw(Graphics g) {
		g.drawImage(player, playerX, playerY, null);
		for (PlayerAttack attack : playerAttackList) {
			g.drawImage(attack.attackImage, attack.x, attack.y, null);
		}
	}
	
	// 방향키 제어에 대한 setter
	public void setUp(boolean up) {
		this.up = up;
	}
		
	public void setDown(boolean down) {
		this.down = down;
	}
		
	public void setLeft(boolean left) {
		this.left = left;
	}

	public void setRight(boolean right) {
		this.right = right;
	}
	
	public void setShooting(boolean shooting) {
		this.shooting = shooting;
	}
}
