package RSP;

import java.awt.Image;

import javax.swing.ImageIcon;

public class playerBullet {
	
	int x, y, width, height, attack;
	Image bulletImage;
	
	public playerBullet(int bulletX, int bulletY) {
		this.x = bulletX;
		this.y = bulletY;
		this.width = 30;
		this.height = 100;
		this.attack = 20;
		this.bulletImage = new ImageIcon(Main.class.getResource("../images/playerBullet.png")).getImage();
	}
	
	public void fire() {
		this.y -= 5;
	}
}
