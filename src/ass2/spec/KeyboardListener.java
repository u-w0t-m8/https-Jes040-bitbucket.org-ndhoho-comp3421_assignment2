package ass2.spec;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener{

	private static final Boolean DEBUG = true;
	private Camera camera;
	
	public KeyboardListener(Camera camera){
		this.camera = camera;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * The up arrow moves forward
	 * The down arrow moves backward
	 * The left arrow turns left
	 * The right arrow turns right.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(e.getKeyCode()) {
        case KeyEvent.VK_UP:
        	if(DEBUG) System.out.println("Key: Up");
        	camera.forward();
            break;

        case KeyEvent.VK_DOWN:
        	if(DEBUG) System.out.println("Key: Down");
        	camera.backward();
            break;

        case KeyEvent.VK_LEFT:
        	if(DEBUG) System.out.println("Key: Left");
        	camera.turnLeft();
            break;

        case KeyEvent.VK_RIGHT:
        	if(DEBUG) System.out.println("Key: Right");
        	camera.turnRight();
            break;
		}
		
	}
	
}
