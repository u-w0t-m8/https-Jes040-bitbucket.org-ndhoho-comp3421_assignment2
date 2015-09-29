package ass2.spec;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

/**
 * COMMENT: Comment Tree 
 *
 * @author malcolmr
 */
public class Tree {

    private double[] myPos;
    
	double height = 1;
	double cylinderRadius = 0.1;
	double sphereRadius = 0.5;
	
	private static final int SLICES = 32;
	private static final int STACKS = 50;
	
    public Tree(double x, double y, double z) {
        myPos = new double[3];
        myPos[0] = x;
        myPos[1] = y;
        myPos[2] = z;
    }
    
    public double[] getPosition() {
        return myPos;
    }
    
    /**
     * 
     * https://www.opengl.org/sdk/docs/man2/xhtml/gluCylinder.xml
     * @param gl
     */
    public void draw(GL2 gl){
    	GLU glu = new GLU();
    	System.out.println("DrawTree");
    	gl.glPushMatrix();
    	
        gl.glTranslated(myPos[0], myPos[1], myPos[2]);        
        gl.glRotated(270, 1.0, 0.0, 0.0);
        
        GLUquadric cylinder = glu.gluNewQuadric();
        glu.gluQuadricNormals(cylinder, GL2.GL_SMOOTH);
        glu.gluCylinder(cylinder, cylinderRadius, cylinderRadius, height, SLICES, STACKS);
        

		gl.glRotated(90, 1.0, 0.0, 0.0);
		gl.glTranslated(0.0, height, 0.0);

        GLUquadric sphere = glu.gluNewQuadric();
        glu.gluQuadricNormals(sphere, GL2.GL_SMOOTH);
        glu.gluSphere(sphere, sphereRadius, SLICES, STACKS);

        gl.glPopMatrix();
    }
}
