import javax.swing.*;
import java.awt.*;

public class PlayerAttack {
    Image image = new ImageIcon("src/images/player_attack.png").getImage();
    int x, y;
    int width = image.getWidth(null);
    int height = image.getHeight(null);
    int attack = 5;

    public PlayerAttack(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void fire() {
        this.x += 15;
    }
}
