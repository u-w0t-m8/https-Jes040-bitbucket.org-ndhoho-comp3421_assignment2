package ass2.spec;

import java.awt.Dimension; 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


/**
 * COMMENT: Comment HeightMap 
 *
 * @author malcolmr
 */
public class Terrain {

    private Dimension mySize;
    private double[][] myAltitude;
    private List<Tree> myTrees;
    private List<Road> myRoads;
    private float[] mySunlight;

    /**
     * Create a new terrain
     *
     * @param width The number of vertices in the x-direction
     * @param depth The number of vertices in the z-direction
     */
    public Terrain(int width, int depth) {
        mySize = new Dimension(width, depth);
        myAltitude = new double[width][depth];
        myTrees = new ArrayList<Tree>();
        myRoads = new ArrayList<Road>();
        mySunlight = new float[3];
    }
    
    public Terrain(Dimension size) {
        this(size.width, size.height);
    }

    public Dimension size() {
        return mySize;
    }

    public List<Tree> trees() {
        return myTrees;
    }

    public List<Road> roads() {
        return myRoads;
    }

    public float[] getSunlight() {
        return mySunlight;
    }

    /**
     * Set the sunlight direction. 
     * 
     * Note: the sun should be treated as a directional light, without a position
     * 
     * @param dx
     * @param dy
     * @param dz
     */
    public void setSunlightDir(float dx, float dy, float dz) {
        mySunlight[0] = dx;
        mySunlight[1] = dy;
        mySunlight[2] = dz;        
    }
    
    /**
     * Resize the terrain, copying any old altitudes. 
     * 
     * @param width
     * @param height
     */
    public void setSize(int width, int height) {
        mySize = new Dimension(width, height);
        double[][] oldAlt = myAltitude;
        myAltitude = new double[width][height];
        
        for (int i = 0; i < width && i < oldAlt.length; i++) {
            for (int j = 0; j < height && j < oldAlt[i].length; j++) {
                myAltitude[i][j] = oldAlt[i][j];
            }
        }
    }

    /**
     * Get the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public double getGridAltitude(int x, int z) {
        return myAltitude[x][z];
    }

    /**
     * Set the altitude at a grid point
     * 
     * @param x
     * @param z
     * @return
     */
    public void setGridAltitude(int x, int z, double h) {
        myAltitude[x][z] = h;
    }

    /**
     * Get the altitude at an arbitrary point. 
     * Non-integer points should be interpolated from neighbouring grid points
     * 
     * TO BE COMPLETED
     *  Using equation from:
     *  http://www.ajdesigner.com/phpinterpolation/bilinear_interpolation_equation.php
     * 
     * @param x
     * @param z
     * @return
     */
    public double altitude(double x, double z) {
        double altitude = 0;
        
        int x1 = (int) Math.floor(x);
        int x2 = (int) Math.ceil(x);
        int z1 = (int) Math.floor(z);
        int z2 = (int) Math.ceil(z);
        
        double a11 = getGridAltitude(x1,z1);
        double a21 = getGridAltitude(x2,z1);
        double a12 = getGridAltitude(x1,z2);
        double a22 = getGridAltitude(x2,z2);

        double p1 = a11 * (((x2 - x) * (z2 - z)) / ((x2 - x1) * (z2 - z1)));
        double p2 = a21 * (((x - x1) * (z2 - z)) / ((x2 - x1) * (z2 - z1)));
        double p3 = a12 * (((x2 - x) * (z - z1)) / ((x2 - x1) * (z2 - z1)));
        double p4 = a22 * (((x - x1) * (z - z1)) / ((x2 - x1) * (z2 - z1)));

        altitude = p1 + p2 + p3 + p4;
        
        return altitude;
    }
    
    /**
     * Add a tree at the specified (x,z) point. 
     * The tree's y coordinate is calculated from the altitude of the terrain at that point.
     * 
     * @param x
     * @param z
     */
    public void addTree(double x, double z) {
        double y = altitude(x, z);
        Tree tree = new Tree(x, y, z);
        myTrees.add(tree);
    }


    /**
     * Add a road. 
     * 
     * @param x
     * @param z
     */
    public void addRoad(double width, double[] spine) {
        Road road = new Road(width, spine);
        myRoads.add(road);        
    }


    /**
     * Drawing a mesh of triangles
     * 
     * Example:
     * (0,0,0)  (1,0.5,0)   
     * 		+-----+
     * 		|    /|  
     * 		|  /  |
     * 		|/    |
     * 		+-----+
     * 	(0,0,1)  (1,0.3,1)
     * (x, y, z) where y = altitudes
     * counter-clockwise for front facing
     * @param gl
     */
    public void draw(GL2 gl){
    	
    	for(int x = 0; x < mySize.getWidth()-1; x++){
    		for(int z = 0; z < mySize.getHeight()-1; z++){
    			// x0 x1 x2
    			// y0 y1 y2
    			// z0 z1 z2
    			double[][] LeftTriangle = {{x, x, x+1},
    								{getGridAltitude(x,z), getGridAltitude(x,z+1), getGridAltitude(x+1,z)},
    								{z, z+1, z}};
    			double[][] RightTriangle = {{x+1, x+1, x},
    								{getGridAltitude(x+1,z+1), getGridAltitude(x+1,z), getGridAltitude(x,z+1)},
    								{z+1, z, z+1}};
    			
    			double [] LeftTriNormal = calcNormal(LeftTriangle, 2);
    			double [] RightTriNormal = calcNormal(RightTriangle, 2);
    			
    			gl.glBegin(GL2.GL_TRIANGLES);
    				gl.glNormal3d(LeftTriNormal[0], LeftTriNormal[1], LeftTriNormal[2]);
    				gl.glVertex3d(LeftTriangle[0][0], LeftTriangle[1][0], LeftTriangle[2][0]);
    				gl.glVertex3d(LeftTriangle[0][1], LeftTriangle[1][1], LeftTriangle[2][1]);
    				gl.glVertex3d(LeftTriangle[0][2], LeftTriangle[1][2], LeftTriangle[2][2]);
    			gl.glEnd();
    			
    			gl.glBegin(GL2.GL_TRIANGLES);
					gl.glNormal3d(RightTriNormal[0], RightTriNormal[1], RightTriNormal[2]);
					gl.glVertex3d(RightTriangle[0][0], RightTriangle[1][0], RightTriangle[2][0]);
    				gl.glVertex3d(RightTriangle[0][1], RightTriangle[1][1], RightTriangle[2][1]);
    				gl.glVertex3d(RightTriangle[0][2], RightTriangle[1][2], RightTriangle[2][2]);
    			gl.glEnd();
    			
    			}
    		}
    	}

    /**
     * Newell's Method for computing face normal. lecture note 04_3D page 25
     * 
     * @param triangleCoord
     * @param vertices
     * @return
     */
	public double[] calcNormal(double[][] triangleCoord, int vertices) {
		double nx = 0;
		double ny = 0;
		double nz = 0;
		
		for(int i = 0; i < vertices; i++){
			nx += (triangleCoord[1][i] - triangleCoord[1][i+1]) * (triangleCoord[2][i] + triangleCoord[2][i+1]);
		}
		
		for(int j = 0; j < vertices; j++){
			ny += (triangleCoord[2][j] - triangleCoord[2][j+1]) * (triangleCoord[0][j] + triangleCoord[0][j+1]);
		}
		
		for(int k = 0; k < vertices; k++){
			nz += (triangleCoord[0][k] - triangleCoord[0][k+1]) * (triangleCoord[1][k] - triangleCoord[1][k+1]);
		}
		
		double[] normal = {nx, ny, nz};
		return normal;
	}
    
}
