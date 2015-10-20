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
    
	double height = 1.75;
	double cylinderRadius = 0.15;
	double sphereRadius = 0.5;
	
	//It does generate a Mesh however my value for slices and stacks
	//are high enough to make it not visble, lower it to 10, and you can see a mesh
	private static final int SLICES = 32; //32
	private static final int STACKS = 50; //50
	
	//Texture - Jess Replace the PICTURE!!!
	private Texture texture[];
    private final int NUM_TEXTURES = 2;
	private String textureFileTreeTrunk = "src/ass2/images/brickRoad.jpg";
    private String textureExtTreeTrunk = "jpg";
    
	private String textureFileTreeBranches = "src/ass2/images/brickRoad.jpg";
    private String textureExtTreeBranches = "jpg";
	
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
    	texture = new Texture[NUM_TEXTURES];
        
        texture[0] = new Texture(gl,textureFileTreeTrunk,textureExtTreeTrunk,true);
        texture[1] = new Texture(gl,textureFileTreeBranches,textureExtTreeBranches,true);
    	
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE); 
		
    	gl.glPushMatrix();
    	
        gl.glTranslated(myPos[0], myPos[1], myPos[2]);        
        gl.glRotated(270, 1.0, 0.0, 0.0);
        
        GLUquadric cylinder = glu.gluNewQuadric();
        glu.gluQuadricNormals(cylinder, GL2.GL_SMOOTH);
        glu.gluQuadricTexture(cylinder, true);
        glu.gluCylinder(cylinder, cylinderRadius, cylinderRadius, height, SLICES, STACKS);
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, texture[0].getTextureId());
		gl.glTexCoord2f(0.0f, 0.0f);


		gl.glRotated(90, 1.0, 0.0, 0.0);
		gl.glTranslated(0.0, height, 0.0);

        GLUquadric sphere = glu.gluNewQuadric();
        glu.gluQuadricNormals(sphere, GL2.GL_SMOOTH);
        glu.gluQuadricTexture(sphere, true);
        glu.gluSphere(sphere, sphereRadius, SLICES, STACKS);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture[1].getTextureId());
		gl.glTexCoord2f(0.0f, 0.0f);

        gl.glPopMatrix();
    }
}
