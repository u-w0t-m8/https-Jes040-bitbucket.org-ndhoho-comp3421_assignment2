package ass2.spec;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class KeyboardListener implements KeyListener, MouseMotionListener{

	private static final Boolean DEBUG = false;

	private static final int ROTATION_SCALE = 1;
	private Camera camera;
	private Point myMousePoint;
	private int myRotateX = 0;
	private int myRotateY = 0;

	private boolean torch = false;
	
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

		case KeyEvent.VK_W:
			if(DEBUG) System.out.println("Key: W");
			camera.keyW();
			break;

		case KeyEvent.VK_S:
			if(DEBUG) System.out.println("Key: S");
			camera.keyS();
			break;

		case KeyEvent.VK_A:
			if(DEBUG) System.out.println("Key: A");
			camera.keyA();
			break;
			
		case KeyEvent.VK_D:
			if(DEBUG) System.out.println("Key: D");
			camera.keyD();
			break;
			
		case KeyEvent.VK_1:
			if(torch == false){
				torch = true;
			} else {
				torch = false;
			}
		}
	}

	public boolean getTorch(){
		return torch;
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
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		double prevYPoint = myMousePoint.getY();
		myMousePoint = e.getPoint();
		//up is less
		if((prevYPoint - myMousePoint.getY()) >= 1){
			camera.keyW();
		} else {
			camera.keyS();
		}
		if(DEBUG) System.out.println(myMousePoint.toString() + " " + e.getPoint().getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		Point p = e.getPoint();

		if (myMousePoint != null) {
			int dx = p.x - myMousePoint.x;
			int dy = p.y - myMousePoint.y;
			// Note: dragging in the x dir rotates about y
			//       dragging in the y dir rotates about x
			myRotateY += dx * ROTATION_SCALE;
			myRotateX += dy * ROTATION_SCALE;
		}
		myMousePoint = p;
	}

}
