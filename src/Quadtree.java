import java.awt.*;
import java.util.ArrayList;

public class Quadtree {

    public static final int MAX_OBJECTS = 10;

    private Quadtree[] nodes;
    private int level;
    private ArrayList<Shape> objects;
    private Rectangle bounds;
    private boolean leaf;

    public Quadtree(int pLevel, Rectangle pbounds) {
        level = pLevel;
        bounds = pbounds;
        objects = new ArrayList<Shape>();
        nodes = new Quadtree[4];
        leaf = false;
    }

    public int add(Shape s) {
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

	private boolean encompasses(Shape s) {
		Rectangle sb = s.getBounds();
		Point leftTop = new Point(sb.x, sb.y);
		Point rightBottom = new Point(sb.x + sb.width, sb.y + sb.height);
		return bounds.contains(leftTop) && bounds.contains(rightBottom);
	}

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
