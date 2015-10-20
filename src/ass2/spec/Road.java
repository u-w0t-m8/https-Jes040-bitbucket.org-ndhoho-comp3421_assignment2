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

	//Given Code
    private List<Double> myPoints;
    private double myWidth;
    
    //Our Code
    private List<Polygon> myMesh;
    // how much the cross-section is scaled before extrusion
    private static final double SCALE = 0.01;
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
    
    /**
     * The extrusion code.
     * This method extrudes the cross section along the spine
     */
    private void computeMesh() {
        Polygon cs = getCrossSection();
        if (cs == null) {
            return;
        }
        
        List<Point> crossSection = cs.getPoints();
        List<Point> spine = getSpine();
        if (spine == null) {
            return;
        }

        // Step 1: create a vertex list by moving the cross section along the spine
        List<Point> vertices = new ArrayList<Point>();

        Point pPrev;
        Point pCurr = spine.get(0);
        Point pNext = spine.get(1);
        
        // first point is a special case
        addPoints(crossSection, vertices, pCurr, pCurr, pNext);
        
        // mid points
        for (int i = 1; i < spine.size() - 1; i++) {
            pPrev = pCurr;
            pCurr = pNext;
            pNext = spine.get(i+1);
            addPoints(crossSection, vertices, pPrev, pCurr, pNext);            
        }
        
        // last point is a special case
        pPrev = pCurr;
        pCurr = pNext;
        addPoints(crossSection, vertices, pPrev, pCurr, pCurr);

        // Step 2: connect points in successive cross-sections to form quads
        
        myMesh = new ArrayList<Polygon>();

        int n = crossSection.size();
        
        // for each point along the spine
        for (int i = 0; i < spine.size() - 1; i++) {
            // for each point in the cross section
            for (int j = 0; j < n; j++) {
                // create a quad joining this point and the next one
                // to the equivalent points in the next cross-section
                // note: make sure they are in anti-clockwise order
                // so they are facing outwards
                Polygon quad = new Polygon();                
                quad.addPoint(vertices.get(i * n + j));
                quad.addPoint(vertices.get(i * n + (j+1) % n));
                quad.addPoint(vertices.get((i+1) * n + (j+1) % n));
                quad.addPoint(vertices.get((i+1) * n + j));
                myMesh.add(quad);
            }
        }
    }
    /**
     * Transform the points in the cross-section using the Frenet frame
     * and add them to the vertex list.
     * 
     * @param crossSection The cross section
     * @param vertices The vertex list
     * @param pPrev The previous point on the spine
     * @param pCurr The current point on the spine
     * @param pNext The next point on the spine
     */
    private void addPoints(List<Point> crossSection, List<Point> vertices,
            Point pPrev, Point pCurr, Point pNext) {

        // compute the Frenet frame as an affine matrix
        double[][] m = new double[4][4];
        
        // phi = pCurr        
        m[0][3] = pCurr.x;
        m[1][3] = pCurr.y;
        m[2][3] = pCurr.z;
        m[3][3] = 1;
        
        // k = pNext - pPrev (approximates the tangent)
        m[0][2] = pNext.x - pPrev.x;
        m[1][2] = pNext.y - pPrev.y;
        m[2][2] = pNext.z - pPrev.z;
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
        
        // transform the points
        for (Point cp : crossSection) {
            Point q = cp.transform(m);
            vertices.add(q);
        }
    }

    /**
     * Get the currently selected spine
     * @return
     */
    public List<Point> getSpine() {        
        List<Point> spinePoints = new ArrayList<Point>();
        for(int i = 0; i < size(); i++){
			for (double j = 0.0; j < 1.0; j += 0.1) {
	        	double[] pointOnSpine = point(i+j);
	        	spinePoints.add(new Point(pointOnSpine[0], terrain.altitude(pointOnSpine[0],pointOnSpine[1]), pointOnSpine[1])); //I dont get this Value.. y 
			}
        }
        
        return spinePoints;
    }
    

    
    /**
     * Get the currently selected cross section
     * @return
     */
    public Polygon getCrossSection() {
        Polygon road = new Polygon();

        road.addPoint(SCALE, width()/2, 0);
        road.addPoint(-SCALE, width()/2, 0);
        road.addPoint(-SCALE, -width()/2, 0);
        road.addPoint(SCALE, -width()/2, 0);
        
        return road;
    }
    
    /**
     * 
     * @param gl
     */
    public void draw(GL2 gl){
    	
    	computeMesh();
    	if(myMesh == null){
    		System.out.println("myMesh = Null");
    		return;
    	}
    	
        texture = new Texture(gl,textureFileRoad,textureExtRoad,true);
    	gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureId());
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE); 
		
    	
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    	gl.glPushMatrix();
        gl.glColor4d(0, 0, 0, 1);
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        
        for (Polygon p : myMesh) {
            p.draw(gl);
        }
    	gl.glPopMatrix();

    }
}
