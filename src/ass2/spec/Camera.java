package ass2.spec;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Camera implements GLEventListener{
		
	private static final Boolean DEBUG = false;

	private Terrain terrain;
	
	private float test1;
	private float test2;
	private float test3;
	private float test4;
	
	public Camera(Terrain terrain){
		this.terrain = terrain;
		test1 = -5f;
		test2 = 0f;
		test3 = 12f;
	}
	
	public void setView(GL2 gl){
		if(DEBUG) System.out.println("Camera - setView");
		
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU glu = new GLU();

		//gluPerspective(fieldOfView, aspectRatio, near, far)
		glu.gluPerspective(100, 1, 1, 20);
		
		//Number took from teapotview week4 example code
		//FUCKING MAGICAL CODE now i need to figure out the right number
		//gluLookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ)
		glu.gluLookAt(test1, 3, test3, 0, 0, 0, 0, 1, 0);
		if(DEBUG) System.out.println(test1 + " " + test3);
//		glu.gluLookAt(0, 2, 5, 0, 0, 0, 0, 1, 0);
//		glu.gluLookAt(test1, 2, test2, 0, 0, 0, 0, 1, 0);

	}

	public void forward(){
		test1 -= 1;
	}
	
	public void backward(){
		test1 += 1;
	}
	
	public void turnLeft(){
		test3 += 1;
	}
	
	public void turnRight(){
		test3 -= 1;
	}
	
	@Override
	public void display(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		
	}

}
