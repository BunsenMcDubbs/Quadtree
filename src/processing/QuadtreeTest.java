package processing;

import quadtree.Quadtree;
import processing.core.PApplet;

import java.awt.*;

/**
 * Created by andrew on 1/21/15.
 *
 * @author Andrew Dai (bunsenmcdubbs)
 */
public class QuadtreeTest extends PApplet{

    public static int WIDTH = 800;
    public static int HEIGHT = 640;

    public Quadtree q;
    public void setup() {
        size(WIDTH, HEIGHT);
        background(100);

        q = new Quadtree(new Rectangle(0, 0, WIDTH, HEIGHT));
    }

    public void draw() {
        drawQuad(q);
    }

    public void mouseClicked() {
        q.add(new Rectangle(mouseX - 10, mouseY - 10, 20, 20));
    }

    public void drawQuad(Quadtree curr) {
        for (Shape s : curr.getImmediate()) {
            drawRectangle((Rectangle) s, curr.level());
        }
        if (!curr.isLeaf()) {
            Quadtree[] branches = curr.getChildren();
            for (Quadtree b : branches) {
                drawQuad(b);
            }
        }
        drawBounds(curr.bounds(), curr.level());
    }

    public void drawBounds(Rectangle r, int level) {
        noFill();
        stroke(0);
        rect(r.x, r.y, r.width, r.height);
    }

    public void drawRectangle(Rectangle r, int level) {
        stroke(0);
        fill(128, 50 * level, 256, 50);
        rect(r.x, r.y, r.width, r.height);
    }

}
