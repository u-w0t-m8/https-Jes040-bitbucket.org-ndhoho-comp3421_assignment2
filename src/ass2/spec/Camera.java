package ass2.spec;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Camera implements GLEventListener{
		
	private static final Boolean DEBUG = true;

	private Terrain terrain;
	
	public Camera(Terrain terrain){
		this.terrain = terrain;
	}
	
	public void setView(GL2 gl){
		if(DEBUG) System.out.println("Camera - setView");
		
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU glu = new GLU();

		//fieldOfView, aspectRatio, near, far
		glu.gluPerspective(100, 1, 1, 20);
		
		//Number took from teapotview week4 example code
		//FUCKING MAGICAL CODE now i need to figure out the right number
        glu.gluLookAt(0, 2, 5, 0, 0, 0, 0, 1, 0); 

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
