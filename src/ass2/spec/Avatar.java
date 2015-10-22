package ass2.spec;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.gl2.GLUT;

public class Avatar {

	private static final Boolean DEBUG = false;
	
	private Terrain terrain;

	private double sphereRadius = 0.2;
	private double sphere1Radius = 0.15;

	private double cylinderHeights = 0.15;
	private double cylinderRadius = 0.03;

	private static final int SLICES = 32; //32
	private static final int STACKS = 50; //50
	
	//textures
	private Texture texture1;

	
	private String textureFileCat = "src/ass2/images/cat.jpg";
    private String textureExtCat = "jpg";


	public Avatar(Terrain terrain){
		this.terrain = terrain;
	}

	public void draw(GL2 gl){
		GLU glu = new GLU();
		GLUT glut = new GLUT();

		gl.glPushMatrix();

		double myPosition[] = Camera.getMyPosition();
		double myAngle = Camera.getMyAngle();
		
        texture1 = new Texture(gl,textureFileCat,textureExtCat, true);
		
		if(DEBUG) System.out.println("x: " + myPosition[0] + " y: " + myPosition[2] + " z: " + myPosition[1]);
		
		if(myPosition[0] <= 0 || myPosition[1] <= 0){
			myPosition[2] = 0;
		} else if(myPosition[0] >= terrain.size().getWidth() - 1 || myPosition[1] >= terrain.size().getHeight() - 1){
			myPosition[2] = 0;
		}
		if (DEBUG) System.out.println("x: " + myPosition[0] + " y: " + myPosition[2] + " z: " + myPosition[1]);
		if (DEBUG) System.out.println("angle: " + myAngle );

//		gl.glTranslated(0, 2, 0);
		
		gl.glTranslated(myPosition[0], cylinderHeights+myPosition[2], myPosition[1]);
		gl.glRotated(-Math.toDegrees(myAngle+180), 0, 1, 0);
		gl.glRotated(90, 1.0, 0.0, 0.0);
		
//		gl.glTranslated(0, cylinderHeights, 0);
//		gl.glRotated(90, 1.0, 0.0, 0.0);

		GLUquadric cylinder = glu.gluNewQuadric();
		glu.gluQuadricNormals(cylinder, GL2.GL_SMOOTH);

//		glu.gluCylinder(cylinder, 0.05, 0.05, 2, SLICES, STACKS);

		//4 legs
		gl.glPushMatrix();
    	gl.glEnable(GL2.GL_TEXTURE_GEN_S);
	    gl.glEnable(GL2.GL_TEXTURE_GEN_T);
		gl.glTranslated(0.0, 0.12, 0);
		glu.gluCylinder(cylinder, cylinderRadius, cylinderRadius, cylinderHeights, SLICES, STACKS);
		gl.glPopMatrix();

		gl.glPushMatrix();
    	gl.glEnable(GL2.GL_TEXTURE_GEN_S);
	    gl.glEnable(GL2.GL_TEXTURE_GEN_T);

		gl.glTranslated(0.0, -0.12, 0.0);
		glu.gluCylinder(cylinder, cylinderRadius, cylinderRadius, cylinderHeights, SLICES, STACKS);
		gl.glPopMatrix();

		gl.glPushMatrix();
    	gl.glEnable(GL2.GL_TEXTURE_GEN_S);
	    gl.glEnable(GL2.GL_TEXTURE_GEN_T);

		gl.glTranslated(-0.12, 0.0, 0.0);
		glu.gluCylinder(cylinder, cylinderRadius, cylinderRadius, cylinderHeights, SLICES, STACKS);
		gl.glPopMatrix();

		gl.glPushMatrix();
    	gl.glEnable(GL2.GL_TEXTURE_GEN_S);
	    gl.glEnable(GL2.GL_TEXTURE_GEN_T);
		gl.glTranslated(0.12, 0.0, 0.0);
		glu.gluCylinder(cylinder, cylinderRadius, cylinderRadius, cylinderHeights, SLICES, STACKS);
		gl.glPopMatrix();

		gl.glRotated(270, 1.0, 0.0, 0.0);

		gl.glTranslated(0, sphereRadius-0.1, 0);
		
    	gl.glEnable(GL2.GL_TEXTURE_GEN_S);
	    gl.glEnable(GL2.GL_TEXTURE_GEN_T);
		//Lower Body
		GLUquadric sphere = glu.gluNewQuadric();
//		glu.gluQuadricNormals(sphere, GL2.GL_SMOOTH);
		glu.gluSphere(sphere, sphereRadius, SLICES, STACKS);
        glu.gluQuadricTexture(sphere, true);
//        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture1.getTextureId());
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE); 
        glu.gluSphere(sphere, sphereRadius, SLICES, STACKS);
        gl.glDisable(GL2.GL_TEXTURE_GEN_S); 
	    gl.glDisable(GL2.GL_TEXTURE_GEN_T);

		//Tail
		gl.glPushMatrix();
			gl.glTranslated(-sphereRadius+0.07, 0, -sphereRadius+0.07);
			gl.glRotated(225, 0, 1, 0);
			gl.glRotated(-60, 1, 0, 0);
			glut.glutSolidCylinder(0.015, 0.4,SLICES,STACKS);
		gl.glPopMatrix();

		gl.glTranslated(0, sphereRadius+sphere1Radius, 0);

		//Upper Body
		GLUquadric sphere1 = glu.gluNewQuadric();
		glu.gluQuadricNormals(sphere1, GL2.GL_SMOOTH);
		glu.gluSphere(sphere1, sphere1Radius, SLICES, STACKS);

		//The Ears
		gl.glPushMatrix();
			gl.glTranslated(0.05, sphere1Radius+0.025, -0.05);
			gl.glRotated(45, 0, 1, 0);
			gl.glRotated(-15, 0, 0, 1);
			gl.glScaled(0.03, 0.04, 0.03);
			drawPyramid(gl);
		gl.glPopMatrix();

		gl.glPushMatrix();
			gl.glTranslated(-0.05, sphere1Radius+0.025, 0.05);
			gl.glRotated(45, 0, 1, 0);
			gl.glRotated(15, 0, 0, 1);
			gl.glScaled(0.03, 0.04, 0.03);
			drawPyramid(gl);
		gl.glPopMatrix();
		
		gl.glPopMatrix();
	}

	public void drawPyramid(GL2 gl){

		gl.glBegin(GL2.GL_TRIANGLES);
		// Front
		double p0[] = {0.0f, 1.0f, 0.0f};
		double p1[] = {-1.0f, -1.0f, 1.0f};
		double p2[] = {1.0f, -1.0f, 1.0f};

		// Right
		double p3[] = {0.0f, 1.0f, 0.0f};
		double p4[] = {1.0f, -1.0f, 1.0f};
		double p5[] = {1.0f, -1.0f, -1.0f};

		// Back
		double p6[] = {0.0f, 1.0f, 0.0f};
		double p7[] = {1.0f, -1.0f, -1.0f};
		double p8[] = {-1.0f, -1.0f, -1.0f};

		// Left
		double p9[] = {0.0f, 1.0f, 0.0f};
		double p10[] = {-1.0f,-1.0f,-1.0f};
		double p11[] = {-1.0f,-1.0f, 1.0f};

		double [] n1 = getNormal(p0,p1,p2);
		double [] n2 = getNormal(p3,p4,p5);
		double [] n3 = getNormal(p6,p7,p8);
		double [] n4 = getNormal(p9,p10,p11);

		n1 = normalise(n1);
		n2 = normalise(n2);
		n3 = normalise(n3);
  	    n4 = normalise(n4);
  	    
		gl.glNormal3dv(n1,0);
		gl.glVertex3dv(p0,0);
		gl.glVertex3dv(p1,0);
		gl.glVertex3dv(p2,0);
		
		gl.glNormal3dv(n2,0);		
		gl.glVertex3dv(p3,0);
		gl.glVertex3dv(p4,0);
		gl.glVertex3dv(p5,0);
		
		gl.glNormal3dv(n3,0);
		gl.glVertex3dv(p6,0);
		gl.glVertex3dv(p7,0);		
		gl.glVertex3dv(p8,0);
		
		gl.glNormal3dv(n4,0);
		gl.glVertex3dv(p9,0);
		gl.glVertex3dv(p10,0);
		gl.glVertex3dv(p11,0);

		gl.glEnd();
	}

	//Week 4 Normals.java code
	double [] getNormal(double[] p0, double[] p1, double[] p2){
		double u[] = {p1[0] - p0[0], p1[1] - p0[1], p1[2] - p0[2]};
		double v[] = {p2[0] - p0[0], p2[1] - p0[1], p2[2] - p0[2]};
		return cross(u,v);
	}

	double [] cross(double u [], double v[]){
		double crossProduct[] = new double[3];
		crossProduct[0] = u[1]*v[2] - u[2]*v[1];
		crossProduct[1] = u[2]*v[0] - u[0]*v[2];
		crossProduct[2] = u[0]*v[1] - u[1]*v[0];
		return crossProduct;
	}
	
	double [] normalise(double [] n){
		double  mag = getMagnitude(n);
		double norm[] = {n[0]/mag,n[1]/mag,n[2]/mag};
		return norm;
	}
	
	double getMagnitude(double [] n){
		double mag = n[0]*n[0] + n[1]*n[1] + n[2]*n[2];
		mag = Math.sqrt(mag);
		return mag;
	}
}