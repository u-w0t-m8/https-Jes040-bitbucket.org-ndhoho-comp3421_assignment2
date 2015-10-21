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
    
    public void draw(GL2 gl){
		gl.glBegin(GL2.GL_POLYGON);
		System.out.println("-----");
		
		for(int i = 0; i < size(); i++){
			for(double j = 0; j <= 0.99 ; j += 0.01){
				double[] p0 = point(i+j);
				double x0 = p0[0];
				double z0 = p0[1];
				double y0 = terrain.altitude(x0, z0);
//				System.out.println(x0 + " " + y0 + " " + z0);
				
				double[] p1 = point(i+j+0.01);
				double x1 = p1[0];
				double z1 = p1[1];
				double y1 = terrain.altitude(x1, z1);
				
		        // k = pNext - pPrev (approximates the tangent)
//		        m[0][2] = pNext.x - pPrev.x;
//		        m[1][2] = pNext.y - pPrev.y;
//		        m[2][2] = pNext.z - pPrev.z;
//		        m[3][2] = 0;
			    double[] tangent = {x1 - x0, y1 - y0, z1 - z0, 0};
//			    System.out.println("tangent: " + tangent[0] + " " + tangent[1] + " " + tangent[2]);
			    
		        // normalise k
//		        double d = Math.sqrt(m[0][2] * m[0][2] + m[1][2] * m[1][2] + m[2][2] * m[2][2]);  
//		        m[0][2] /= d;
//		        m[1][2] /= d;
//		        m[2][2] /= d;
		        double d = Math.sqrt(tangent[0] * tangent[0] + tangent[1] * tangent[1] + tangent[2] * tangent[2]);  
		        tangent[0] /= d;
		        tangent[1] /= d;
		        tangent[2] /= d;
//			    System.out.println("normalise tangent: " + tangent[0] + " " + tangent[1] + " " + tangent[2]);
			    
		        // i = simple perpendicular to k
//		        m[0][0] = -m[1][2];
//		        m[1][0] =  m[0][2];
//		        m[2][0] =  0;
//		        m[3][0] =  0;
		        double perpendicular[] = new double[4];
		        perpendicular[0] = -tangent[1];
		        perpendicular[1] = tangent[0];
		        perpendicular[2] = 0;
		        perpendicular[3] = 0;
		        
		        // j = k x i
//		        m[0][1] = m[1][2] * m[2][0] - m[2][2] * m[1][0];
//		        m[1][1] = m[2][2] * m[0][0] - m[0][2] * m[2][0];
//		        m[2][1] = m[0][2] * m[1][0] - m[1][2] * m[0][0];
//		        m[3][1] =  0;
		        double vectorJ[] = new double[4];
		        vectorJ[0] = tangent[1] * perpendicular[2] - tangent[2] * perpendicular[1];
		        vectorJ[1] = tangent[2] * perpendicular[0] - tangent[0] * perpendicular[2];
		        vectorJ[2] = tangent[0] * perpendicular[1] - tangent[1] * perpendicular[0];
		        vectorJ[3] = 0;
		        
		        

//				double x = i+j;
//				double y = i+j+0.01;
//				System.out.println(x + " " + y);
			}
		}
		gl.glEnd();
    }

}
