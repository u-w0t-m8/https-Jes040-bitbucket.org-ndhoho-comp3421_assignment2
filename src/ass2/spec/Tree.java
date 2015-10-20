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
	private Texture texture1;
	private Texture texture2;
    //private final int NUM_TEXTURES = 2;
    //Added image
	private String textureFileTreeTrunk = "src/ass2/images/treeTrunk.jpg";
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
    	//texture = new Texture[NUM_TEXTURES];
        
        texture1 = new Texture(gl,textureFileTreeTrunk,textureExtTreeTrunk,true);
        texture2 = new Texture(gl,textureFileTreeBranches,textureExtTreeBranches,true);
    			
    	gl.glPushMatrix();
    	
        gl.glTranslated(getPosition()[0], getPosition()[1], getPosition()[2]);
	    
    	gl.glPushMatrix();
    	gl.glEnable(GL2.GL_TEXTURE_GEN_S);
	    gl.glEnable(GL2.GL_TEXTURE_GEN_T);
	        gl.glRotated(270, 1.0, 0.0, 0.0);
	        GLUquadric cylinder = glu.gluNewQuadric();
	        glu.gluQuadricNormals(cylinder, GL2.GL_SMOOTH);
	        glu.gluQuadricTexture(cylinder, true);
	    	gl.glBindTexture(GL2.GL_TEXTURE_2D, texture1.getTextureId());
			gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);
	        glu.gluCylinder(cylinder, cylinderRadius, cylinderRadius, height, SLICES, STACKS);
	        gl.glDisable(GL2.GL_TEXTURE_GEN_S); 
		    gl.glDisable(GL2.GL_TEXTURE_GEN_T);
    	gl.glPopMatrix();

		gl.glTranslated(0.0, height, 0.0);
		gl.glRotated(90, 1.0, 0.0, 0.0);
		
    	gl.glPushMatrix();
    	gl.glEnable(GL2.GL_TEXTURE_GEN_S);
	    gl.glEnable(GL2.GL_TEXTURE_GEN_T);
	        GLUquadric sphere = glu.gluNewQuadric();
	        glu.gluQuadricNormals(sphere, GL2.GL_SMOOTH);
	        glu.gluQuadricTexture(sphere, true);
	        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture2.getTextureId());
			gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE); 
	        glu.gluSphere(sphere, sphereRadius, SLICES, STACKS);
	        gl.glDisable(GL2.GL_TEXTURE_GEN_S); 
		    gl.glDisable(GL2.GL_TEXTURE_GEN_T);
    	gl.glPopMatrix();
        
        gl.glPopMatrix();

    }
}
