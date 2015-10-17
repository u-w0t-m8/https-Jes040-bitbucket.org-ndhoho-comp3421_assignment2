package ass2.spec;

import java.nio.ByteBuffer;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import com.jogamp.common.nio.Buffers;

import ass2.spec.*;

public class Texture {
    private final int NUM_TEXTURES = 4;
	private int imageSize = 64;
    private static final int rgba = 4;
	private MyTexture myTextures[];

    
    //Buffers for the procedural textures
    private ByteBuffer chessImageBuf = Buffers.newDirectByteBuffer(imageSize*imageSize* rgba);
    private ByteBuffer randomImageBuf = Buffers.newDirectByteBuffer(imageSize*imageSize* rgba);
    private String textureFileName1 = "src/ass2/images/brickRoad.jpg";
    private String textureExt1 = "jpg";	
	public Texture(){
		
	}
	
	 public void init(GLAutoDrawable drawable) {
	    	GL2 gl = drawable.getGL().getGL2();
	    	gl.glClearColor(0.8f, 0.8f, 0.8f, 0.0f); 
	    	
	    	gl.glEnable(GL2.GL_DEPTH_TEST); // Enable depth testing.
	    	
	    	// Turn on OpenGL texturing.
	    	gl.glEnable(GL2.GL_TEXTURE_2D);
	   
	    	//Load in textures from files
	    	myTextures = new MyTexture[NUM_TEXTURES];
	    	myTextures[0] = new MyTexture(gl,textureFileName1,textureExt1,true);

	    	// Generate procedural texture.
	        gl.glBindTexture(GL2.GL_TEXTURE_2D, myTextures[0].getTextureId());
	        gl.glBegin(GL2.GL_POLYGON);{
	 
	    	//Load procedural textures
	        
	     
	    }

	 }
}

