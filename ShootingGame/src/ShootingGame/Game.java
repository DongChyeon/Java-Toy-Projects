package ShootingGame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Game extends Thread {
	private int delay = 20;	// 스레드 슬립 주기
	private long pretime;
	private int cnt;	// 0.02초마다 증가
	private boolean isOver = false;	// 게임 오버 여부

	private Image player = new ImageIcon("src/images/player.png").getImage();

	private int playerX, playerY;	// 플레이어 위치
	private int playerWidth = player.getWidth(null);
	private int playerHeight = player.getHeight(null);	// 플레이어 가로, 세로 크기
	private int playerSpeed = 10;
	private int playerHp = 30;

	private boolean up, down, left, right, shooting;	// 키 눌림

	ArrayList<PlayerAttack> playerAttackList = new ArrayList<PlayerAttack>();
	ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
	ArrayList<EnemyAttack> enemyAttackList = new ArrayList<EnemyAttack>();
	private PlayerAttack playerAttack;
	private Enemy enemy;
	private EnemyAttack enemyAttack;

	@Override
	public void run() {
		cnt = 0;
		playerX = 10;
		playerY = (1080 - playerHeight) / 2;

		while (!isOver) {
			pretime = System.currentTimeMillis();
			if (System.currentTimeMillis() - pretime < delay) {
				try {
					Thread.sleep(delay - System.currentTimeMillis() + pretime);
					keyProcess();
					playerAttackProcess();
					enemyAppearProcess();
					enemyMoveProcess();
					enemyAttackProcess();
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
		if (shooting && cnt % 15 == 0) {
			playerAttack = new PlayerAttack(playerX + 222, playerY + 35);
			playerAttackList.add(playerAttack);
		}	// 0.4초마다 미사일 발사
	}

	// 플레이어의 공격을 처리해주는 메소드
	private void playerAttackProcess() {
		for (int i = 0; i < playerAttackList.size(); i++) {
			playerAttack = playerAttackList.get(i);
			playerAttack.fire();

			for (int j = 0; j < enemyList.size(); j++) {
				enemy = enemyList.get(j);
				if (playerAttack.x > enemy.x && playerAttack.x < enemy.x + enemy.width && playerAttack.y > enemy.y && playerAttack.y < enemy.y + enemy.height) {
					enemy.hp -= playerAttack.attack;
					playerAttackList.remove(playerAttack);
				}	// 충돌 판정 후 공격이 적중했으면 체력 플레이어 공격력만큼 체력 감소시키기
				if (enemy.hp <= 0) {
					enemyList.remove(enemy);	// 체력이 없으면 제거
				}
			}
		}
	}

	// 적의 등장을 처리해주는 메소드
	private void enemyAppearProcess() {
		if (cnt % 80 == 0) {
			int enemyY = (int)(Math.random() * 621);
			enemy = new Enemy(1120, enemyY);
			enemyList.add(enemy);
		}
	}

	// 적의 움직임을 처리해주는 메소드
	private void enemyMoveProcess() {
		for (int i = 0; i < enemyList.size(); i++) {
			enemyList.get(i).move();
		}
	}

	// 적의 공격을 처리해주는 메소드
	private void enemyAttackProcess() {
		if (cnt % 50 == 0) {
			enemyAttack = new EnemyAttack(enemy.x - 79, enemy.y + 35);
			enemyAttackList.add(enemyAttack);
		}

		for (int i = 0; i < enemyAttackList.size(); i++) {
			enemyAttack = enemyAttackList.get(i);
			enemyAttack.fire();

			if (enemyAttack.x > playerX && enemyAttack.x < playerX + playerWidth && enemyAttack.y > playerY && enemyAttack.y < playerY + playerHeight) {
				playerHp -= enemyAttack.attack;
				enemyAttackList.remove(enemyAttack);
			}	// 충돌 판정 후 공격이 적중했으면 플레이어 체력 적의 공격력만큼 체력 감소시키기
			if (playerHp <= 0) {
				isOver = true;
				System.out.println("게임 오버");
			}
		}
	}

	// 게임에 필요한 것들을 그려주는 메소드
	public void gameDraw(Graphics g) {
		playerDraw(g);
		enemyDraw(g);
	}

	// 플레이어와 플레이어의 공격을 그려주는 메소드
	public void playerDraw(Graphics g) {
		g.drawImage(player, playerX, playerY, null);
		g.setColor(Color.GREEN);
		g.fillRect(playerX - 1, playerY - 40, playerHp * 6, 20);
		for (int i = 0; i < playerAttackList.size(); i++) {
			playerAttack = playerAttackList.get(i);
			g.drawImage(playerAttack.image, playerAttack.x, playerAttack.y, null);
		}
	}

	// 적과 적의 공격을 그려주는 메소드
	public void enemyDraw(Graphics g) {
		for (int i = 0; i < enemyList.size(); i++) {
			enemy = enemyList.get(i);
			g.drawImage(enemy.image, enemy.x, enemy.y, null);
			g.setColor(Color.GREEN);
			g.fillRect(enemy.x + 1, enemy.y - 40, enemy.hp * 15, 20);
		}
		for (int i = 0; i < enemyAttackList.size(); i++) {
			enemyAttack = enemyAttackList.get(i);
			g.drawImage(enemyAttack.image, enemyAttack.x, enemyAttack.y, null);
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