package RSP;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyListener extends KeyAdapter{
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
			case KeyEvent.VK_A :
				RSP.game.true_Left();
				break;
			case KeyEvent.VK_D :
				RSP.game.true_Right();
				break;
			case KeyEvent.VK_SPACE :
				RSP.game.true_Shooting();
				break;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
			case KeyEvent.VK_A :
				RSP.game.false_Left();
				break;
			case KeyEvent.VK_D :
				RSP.game.false_Right();
				break;
			case KeyEvent.VK_SPACE :
				RSP.game.false_Shooting();
				break;
		}
	}
}
