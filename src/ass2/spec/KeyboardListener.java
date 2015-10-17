package ass2.spec;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class KeyboardListener implements KeyListener, MouseMotionListener{

	private static final Boolean DEBUG = true;
	
    private static final int ROTATION_SCALE = 1;
	private Camera camera;
    private Point myMousePoint;
    private int myRotateX = 0;
    private int myRotateY = 0;

	
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
		}	
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
		myMousePoint = e.getPoint();
		if(DEBUG) System.out.println(myMousePoint.toString());
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
