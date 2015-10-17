package ass2.spec;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

public class Camera implements GLEventListener{
		
	private static final Boolean DEBUG = false;

	private Terrain terrain;
	
	private double aspect; 
	
	private double myAngle;
	
	private double[] myPosition;
	private double[] myRotation;
	
	private double viewY;
	
	public Camera(Terrain terrain){
		this.terrain = terrain;
		aspect = 1.0;
		
		myAngle = 90.3;
		
		//x, z, y
        myPosition = new double[3];
        myPosition[0] = 0;
        myPosition[1] = 0;
        myPosition[2] = terrain.altitude(0,0);
        
        myRotation = new double[2];
        myRotation[0] = 0;
        myRotation[1] = 0;
        
        viewY = 1.2;
	}
	
	public void setView(GL2 gl){
		if(DEBUG) System.out.println("Camera - setView");
		
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU glu = new GLU();

		//gluPerspective(fieldOfView, aspectRatio, near, far)
		glu.gluPerspective(80, aspect, 1, 20);

		//Number took from teapotview week4 example code
		//FUCKING MAGICAL CODE now i need to figure out the right number
		//gluLookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ)
		
		myRotation[0] = Math.sin(myAngle);
		myRotation[1] = -Math.cos(myAngle);

		double eyeX = myPosition[0] - myRotation[0];
		
		//Border restrictions
		if(myPosition[0] < 0){
			myPosition[0] = 0;
		} else if(myPosition[1] < 0){
			myPosition[1] = 0;
		} else if(myPosition[0] > terrain.size().getWidth() - 1){
			myPosition[0] = terrain.size().getWidth() - 1;
		} else if (myPosition[1] > terrain.size().getHeight() - 1){
			myPosition[1] = terrain.size().getHeight() - 1;
		}
		
		if(DEBUG) System.out.println(myPosition[0] + " " + myPosition[1] + " " + myPosition[2] + " " + terrain.size().getWidth());
		
		myPosition[2] = terrain.altitude(myPosition[0],myPosition[1]);

		double eyeY;
		if(myPosition[0] <= 0 || myPosition[1] <= 0){
			eyeY = 0;
		} else if(myPosition[0] >= terrain.size().getWidth() - 1 || myPosition[1] >= terrain.size().getHeight() - 1){
			eyeY = 0;
		} else {
			eyeY = myPosition[2];
		}
		
		if(DEBUG) System.out.println(eyeY);
		
    	double eyeZ = myPosition[1] - myRotation[1];
		
    	double viewX = myPosition[0] + myRotation[0];
    	double viewZ = myPosition[1] + myRotation[1];
    	
    	if(DEBUG) System.out.println(myAngle + " " + myRotation[0] + " " + myRotation[1]);
    	
        glu.gluLookAt(eyeX, eyeY+3, eyeZ, viewX, viewY ,viewZ, 0, 1, 0);
        if(DEBUG) System.out.println(viewY);
	}

	public void setAspectRatio(double ratio){
		aspect = ratio;
		if(DEBUG) System.out.println(aspect);
	}
	
	public void forward(){
		double scale = 0.2;
		myPosition[0] += myRotation[0] * scale;
		myPosition[1] += myRotation[1] * scale;
	}
	
	public void backward(){
		double scale = 0.2;
		myPosition[0] -= myRotation[0] * scale;
		myPosition[1] -= myRotation[1] * scale;
	}
	
	public void turnLeft(){
		myAngle += -0.1;
		myRotation[0] = Math.sin(myAngle);
		myRotation[1] = -Math.cos(myAngle);
	}
	
	public void turnRight(){
		myAngle += 0.1;
		myRotation[0] = Math.sin(myAngle);
		myRotation[1] = -Math.cos(myAngle);
	}
	
	public void keyW(){
		viewY += 0.15;
	}
	
	public void keyS(){
		viewY -= 0.15;
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
	public void reshape(GLAutoDrawable arg0, int x, int y, int w, int h) {
		// TODO Auto-generated method stub		
	}

}
