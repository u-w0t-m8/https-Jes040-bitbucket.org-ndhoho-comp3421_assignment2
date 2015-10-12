package ass2.spec;

import java.io.File;
import java.io.FileNotFoundException;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener{

	//Our Extra Stuff
	private Camera camera;
	private KeyboardListener keyboardlistener;
	
	// create an array of already loaded textures and just bind the one you need whenever you need it.
    private Terrain myTerrain;

    public Game(Terrain terrain) {
    	super("Assignment 2");
        myTerrain = terrain;
   
    }
    
    /** 
     * Run the game.
     *
     */
    public void run() {
    	  //init
    	  GLProfile glp = GLProfile.getDefault();
          GLCapabilities caps = new GLCapabilities(glp);
          GLJPanel panel = new GLJPanel();
          
          // event listener to handle rendering events
          panel.addGLEventListener(this);
          
          camera = new Camera(myTerrain);
          keyboardlistener = new KeyboardListener(camera);
          panel.addKeyListener(keyboardlistener);
 
          // Add an animator to call 'display' at 60fps        
          FPSAnimator animator = new FPSAnimator(60);
          animator.add(panel);
          animator.start();
          
          //set panel options
          getContentPane().add(panel);
          setSize(800, 600);        
          setVisible(true);
          setDefaultCloseOperation(EXIT_ON_CLOSE);        
    }
    
    /**
     * Load a level file and display it.
     * 
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Terrain terrain = LevelIO.load(new File(args[0]));
        Game game = new Game(terrain);
        game.run();
    }

	@Override
	public void display(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		GL2 gl = drawable.getGL().getGL2();
    	
		/************* JESS'S * POTATO * CODE***************/
		
		//clear the colour buffer incase of garbage
		// set background colour
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		
		//make the model-view matrix
		gl.glMatrixMode(GL2.GL_MODELVIEW);
    	gl.glLoadIdentity();
    	
    	// Fill/LineColour
    	gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
		
		//turn on back face culling
		gl.glEnable(GL2.GL_CULL_FACE);
		gl.glCullFace(GL2.GL_BACK);
    	
		//<perspective camera code here>
		
		/****************************************************/
		
		//draw terrain
		myTerrain.draw(gl);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub
		
		/************* JESS'S * POTATO * CODE***************/
		
		GL2 gl = drawable.getGL().getGL2();

    	gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
    	// Enable depth testing
    	gl.glEnable(GL2.GL_DEPTH_TEST);

    	//Enable lighting
    	gl.glEnable(GL2.GL_LIGHTING);
    	// Ambient light source
    	gl.glEnable(GL2.GL_LIGHT0); 
    	
    	gl.glEnable(GL2.GL_NORMALIZE);
    	/****************************************************/
    	
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		// TODO Auto-generated method stub
   	 GL2 gl = drawable.getGL().getGL2();
     gl.glMatrixMode(GL2.GL_PROJECTION);
     gl.glLoadIdentity();
     gl.glOrtho(-9, 9, -9, 9, -9, 9); // testing
		
	}
}
