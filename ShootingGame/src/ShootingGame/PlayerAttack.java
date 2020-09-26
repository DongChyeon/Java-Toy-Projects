package ShootingGame;

import java.awt.Image;

import javax.swing.ImageIcon;

public class PlayerAttack {
	Image attackImage = new ImageIcon("src/images/player_attack.png").getImage();
	int x, y;
	int width = attackImage.getWidth(null);
	int height = attackImage.getHeight(null);
	int attack = 5;	// °ø°Ý·Â
	
	public PlayerAttack(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void fire() {
		this.x += 10;
	}
}
