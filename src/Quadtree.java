import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;

/**
 * This class defines the Quadtree node and all its functions.
 * 
 * @author Andrew Dai (bunsenmcdubbs)
 */
public class Quadtree {

    public static final int MAX_OBJECTS = 10;

    private Quadtree[] nodes;
    private int level;
    private ArrayList<Shape> objects;
    private Rectangle bounds;
    private boolean leaf;
    
    /**
     * Initializes a new Quadtree node with bounds
     * @param pbounds - bounds of the new node (one quarter of the parent node)
     */
    public Quadtree(Rectangle pBounds) {
        this(0, pBounds);
    }
    
    /**
     * Initializes a new Quadtree node with bounds and a level
     * @param pLevel - level of the new node (top level = 0)
     * @param pbounds - bounds of the new node (one quarter of the parent node)
     */
    private Quadtree(int pLevel, Rectangle pBounds) {
        level = pLevel;
        bounds = pBounds;
        objects = new ArrayList<Shape>();
        nodes = new Quadtree[4];
        leaf = false;
    }

    /**
     * Add a shape into the Quadtree.
     *  - It can be added to this node (if density hasn't been reached).
     *  - Added to this node if children don't completely encompass the shape.
     *  - Added to child node.
     *  - Rejected because it doesn't fit in this or child nodes (return -1)
     * @param s - shape to be added to the Quadtree
     * @return the level that the shape was added to.
     */
    public int add(Shape s) {
    	if(!this.encompasses(s)) {
    		return -1; // TODO error code! redo with exception
    	}
    	if(leaf && objects.size() >= MAX_OBJECTS) {
    		split();
    		return add(s);
    	} 
    	if(!leaf) { 
    		for (Quadtree q : nodes) {
    			// successfully added s into child node
    			if(q.encompasses(s)) {
    				return q.add(s);
    			}
    		}
    	}
    	// if failed to fit into child, add here
		objects.add(s);
		return level;
    }
    
    /**
     * Get all objects at this node
     * @return
     */
    @SuppressWarnings("unchecked")
	public ArrayList<Shape> getAll() {
    	if(leaf) { return (ArrayList<Shape>) objects.clone(); }
    	ArrayList<Shape> obj = nodes[0].getAll();
    	for(int i = 1; i < nodes.length; i++) {
    		obj.addAll(nodes[i].getAll());
    	}
    	obj.addAll(objects);
    	return obj;
    }
    
    /**
     * @return all the child nodes of this Quadtree
     */
    public Quadtree[] getChildren() {
    	return nodes;
    }
    
    /**
     * Similar to contains() except it checks if the entire shape (bounding box)
     * is contained inside the bounds of this node.
     * @param s - shape in question
     * @return true if this node complete encompasses the shape
     */
	private boolean encompasses(Shape s) {
		Rectangle sb = s.getBounds();
		Point leftTop = new Point(sb.x, sb.y);
		Point rightBottom = new Point(sb.x + sb.width, sb.y + sb.height);
		return bounds.contains(leftTop) && bounds.contains(rightBottom);
	}

	/**
	 * Splits the Quadtree into nodes and adds the shapes to the proper children
	 */
	private void split() {
		leaf = true;
		int nWidth = bounds.width / 2;
		int nHeight = bounds.height / 2;
		nodes[0] = new Quadtree(level + 1, new Rectangle(bounds.x, bounds.y, nWidth, nHeight));
		nodes[1] = new Quadtree(level + 1, new Rectangle(bounds.x + nWidth, bounds.y, nWidth, nHeight));
		nodes[2] = new Quadtree(level + 1, new Rectangle(bounds.x, bounds.y + nHeight, nWidth, nHeight));
		nodes[3] = new Quadtree(level + 1, new Rectangle(bounds.x + nWidth, bounds.y + nHeight, nWidth, nHeight));
		
		@SuppressWarnings("unchecked")
		ArrayList<Shape> objs = (ArrayList<Shape>) objects.clone();
		objects = new ArrayList<Shape>();
		for(Shape s : objs) {
			add(s);
		}
	}

}
