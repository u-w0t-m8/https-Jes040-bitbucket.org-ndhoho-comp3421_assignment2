package ass2.spec;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;

/**
 * COMMENT: Comment Road 
 *
 * @author malcolmr
 */
public class Road {

	private List<Double> myPoints;
	private double myWidth;

	private Terrain terrain;

	private Texture texture;
//	private String textureFileRoad = "src/ass2/images/brickRoad.jpg";
	private String textureFileRoad = "src/ass2/images/road.jpg";
	private String textureExtRoad = "jpg";

	/** 
	 * Create a new road starting at the specified point
	 */
	public Road(double width, double x0, double y0, Terrain terrain) {
		myWidth = width;
		myPoints = new ArrayList<Double>();
		myPoints.add(x0);
		myPoints.add(y0);
		this.terrain = terrain;
	}

	/**
	 * Create a new road with the specified spine 
	 *
	 * @param width
	 * @param spine
	 */
	public Road(double width, double[] spine, Terrain terrain) {
		myWidth = width;
		myPoints = new ArrayList<Double>();
		for (int i = 0; i < spine.length; i++) {
			myPoints.add(spine[i]);
		}
		this.terrain = terrain;
	}

	/**
	 * The width of the road.
	 * 
	 * @return
	 */
	public double width() {
		return myWidth;
	}

	/**
	 * Add a new segment of road, beginning at the last point added and ending at (x3, y3).
	 * (x1, y1) and (x2, y2) are interpolated as bezier control points.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 */
	public void addSegment(double x1, double y1, double x2, double y2, double x3, double y3) {
		myPoints.add(x1);
		myPoints.add(y1);
		myPoints.add(x2);
		myPoints.add(y2);
		myPoints.add(x3);
		myPoints.add(y3);        
	}

	/**
	 * Get the number of segments in the curve
	 * 
	 * @return
	 */
	public int size() {
		return myPoints.size() / 6;
	}

	/**
	 * Get the specified control point.
	 * 
	 * @param i
	 * @return
	 */
	public double[] controlPoint(int i) {
		double[] p = new double[2];
		p[0] = myPoints.get(i*2);
		p[1] = myPoints.get(i*2+1);
		return p;
	}

	/**
	 * Get a point on the spine. The parameter t may vary from 0 to size().
	 * Points on the kth segment take have parameters in the range (k, k+1).
	 * 
	 * @param t
	 * @return
	 */
	public double[] point(double t) {
		int i = (int)Math.floor(t);
		t = t - i;

		i *= 6;

		double x0 = myPoints.get(i++);
		double y0 = myPoints.get(i++);
		double x1 = myPoints.get(i++);
		double y1 = myPoints.get(i++);
		double x2 = myPoints.get(i++);
		double y2 = myPoints.get(i++);
		double x3 = myPoints.get(i++);
		double y3 = myPoints.get(i++);

		double[] p = new double[2];

		p[0] = b(0, t) * x0 + b(1, t) * x1 + b(2, t) * x2 + b(3, t) * x3;
		p[1] = b(0, t) * y0 + b(1, t) * y1 + b(2, t) * y2 + b(3, t) * y3;        

		return p;
	}

	/**
	 * Calculate the Bezier coefficients
	 * 
	 * @param i
	 * @param t
	 * @return
	 */
	private double b(int i, double t) {

		switch(i) {

		case 0:
			return (1-t) * (1-t) * (1-t);

		case 1:
			return 3 * (1-t) * (1-t) * t;

		case 2:
			return 3 * (1-t) * t * t;

		case 3:
			return t * t * t;
		}

		// this should never happen
		throw new IllegalArgumentException("" + i);
	}


	/**SOMEWHERE IS WRONG HERE....
	 * 
	 * @param pPrev
	 * @param pCurr
	 * @param pNext
	 * @return
	 */
	
	public double[] normalToTangent(double[] pPrev, double[] pCurr, double[] pNext){
        // compute the Frenet frame as an affine matrix
        double[][] m = new double[4][4];
        
        // phi = pCurr        
        m[0][3] = pCurr[0];
        m[1][3] = terrain.altitude(pCurr[0], pCurr[1]);
        m[2][3] = pCurr[1];
        m[3][3] = 1;
        
        // k = pNext - pPrev (approximates the tangent)
       
        m[0][2] = pNext[0] - pPrev[0];
        m[1][2] = terrain.altitude(pNext[0], pNext[1]) - terrain.altitude(pPrev[0], pPrev[1]);
        m[2][2] = pNext[1] - pPrev[1];
        m[3][2] = 0;
      
        
        // normalise k
        double d = Math.sqrt(m[0][2] * m[0][2] + m[1][2] * m[1][2] + m[2][2] * m[2][2]);  
        m[0][2] /= d;
        m[1][2] /= d;
        m[2][2] /= d;
        
        // i = simple perpendicular to k
        m[0][0] = -m[1][2];
        m[1][0] =  m[0][2];
        m[2][0] =  0;
        m[3][0] =  0;
        
        // j = k x i
        m[0][1] = m[1][2] * m[2][0] - m[2][2] * m[1][0];
        m[1][1] = m[2][2] * m[0][0] - m[0][2] * m[2][0];
        m[2][1] = m[0][2] * m[1][0] - m[1][2] * m[0][0];
        m[3][1] =  0;
		
		double vectorJ[] = new double[4];
		vectorJ[0] = m[0][1];
		vectorJ[1] = m[1][1];
		vectorJ[2] = m[2][1];
		vectorJ[3] = m[3][1];

		return vectorJ;
	}

	public void draw(GL2 gl){
		texture = new Texture(gl,textureFileRoad,textureExtRoad,true);
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureId());
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE); 

		
//		gl.glDisable(GL2.GL_LIGHTING);
//		gl.glLineWidth(3);
//		gl.glBegin(GL2.GL_LINE);
//		gl.glBegin(GL2.GL_LINE_STRIP);
//		gl.glBegin(GL2.GL_TRIANGLE_STRIP);
//		gl.glBegin(GL2.GL_POLYGON);
		gl.glBegin(GL2.GL_QUAD_STRIP);
//		System.out.println("-----");

		gl.glColor3f(1, 1, 1);
		
		//prev
		double[] p0 = point(0);
		//current
		double[] p1 = point(0);
		//next
		double[] p2 = point(0.01);
		
		double height = 0.01;
		double width = width()/2;
		
		double[] n0 = normalToTangent(p0 ,p1 ,p2);
		
		gl.glNormal3d(0,0,1);
		gl.glTexCoord2d(1, 1);
		gl.glVertex3d(p1[0]-(n0[0]*width), terrain.altitude(p1[0], p1[1]) + height, p1[1]-(n0[2]*width));
//		gl.glVertex3d(p1[0]-(n0[0]*width), 0 + height, p0[1]-(n0[2]*width));
		gl.glTexCoord2d(0, 1);
		gl.glVertex3d(p1[0]+(n0[0]*width), terrain.altitude(p1[0], p0[1]) + height, p1[1]+(n0[2]*width));
		
		for(double i = 0.01; i < size() - 0.01; i += 0.01){
			p0 = p1; //prev = curr
			p1 = p2; //curr = next
			p2 = point(i+0.01); //next
			
			n0 = normalToTangent(p0 ,p1 ,p2);
    		gl.glTexCoord2d(1, i);
			gl.glVertex3d(p1[0]-(n0[0]*width), terrain.altitude(p1[0], p0[1]) + height, p1[1]-(n0[2]*width));
//			gl.glVertex3d(p1[0]-(n0[0]*width), 0 + height, p0[1]-(n0[2]*width));
    		gl.glTexCoord2d(0, i);

			gl.glVertex3d(p1[0]+(n0[0]*width), terrain.altitude(p1[0], p0[1]) + height, p1[1]+(n0[2]*width));

		}

		p0 = p1;
		p1 = p2;
		
		n0 = normalToTangent(p0 ,p1 ,p1);

    	gl.glTexCoord2d(1, 0);
		gl.glVertex3d(p1[0]-(n0[0]*width), terrain.altitude(p1[0], p0[1]) + height, p1[1]-(n0[2]*width));
    	gl.glTexCoord2d(0, 0);
		gl.glVertex3d(p1[0]+(n0[0]*width), terrain.altitude(p1[0], p0[1]) + height, p1[1]+(n0[2]*width));

		
//		for(double j = 0.01; j < size() - 0.01; j += 0.01){
//			gl.glColor3f(0, 1, 1);
//			gl.glVertex3d(p1[0], 2, p1[1]);
//		}
		
//		for(double i = 0; i < size(); i++){
//			for(double j = 0; j <= 0.99; j += 0.01){
//				System.out.println(i+j);
//			}
//		}
		
		gl.glEnd();
//		gl.glEnable(GL2.GL_LIGHTING);
	}
}
