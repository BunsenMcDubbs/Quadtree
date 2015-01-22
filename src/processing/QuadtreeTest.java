package processing;

import quadtree.Quadtree;
import processing.core.PApplet;

import java.awt.*;

/**
 * Created by andrew on 1/21/15.
 */
public class QuadtreeTest extends PApplet{

    public static int WIDTH = 800;
    public static int HEIGHT = 640;

    public Quadtree q;

    public void setup() {
        size(WIDTH, HEIGHT);
        background(256f);

        q = new Quadtree(new Rectangle(0, 0, WIDTH, HEIGHT));
        q.add(new Rectangle(0, 0, 10, 10));
        q.add(new Rectangle(20, 0, 10, 10));
        q.add(new Rectangle(30, 0, 10, 10));
        q.add(new Rectangle(40, 0, 10, 10));
        q.add(new Rectangle(60, 0, 10, 10));
    }

    public void draw() {
        drawQuad(q);
    }

    public void mouseClicked() {
        q.add(new Rectangle(mouseX - 5, mouseY - 5, 10, 10));
    }

    public void drawQuad(Quadtree curr) {
        drawBounds(curr.bounds(), curr.level());
        for (Shape s : curr.getImmediate()) {
            drawRectangle((Rectangle) s, curr.level());
        }
        if (!curr.isLeaf()) {
            Quadtree[] branches = curr.getChildren();
            for (Quadtree b : branches) {
                drawQuad(b);
            }
        }
    }

    public void drawBounds(Rectangle r, int level) {
        noFill();
        stroke(20 * level);
        rect(r.x, r.y, r.width, r.height);
    }

    public void drawRectangle(Rectangle r, int level) {
        stroke(255, 100, 100);
        fill(100);
        rect(r.x, r.y, r.width, r.height);
    }

}
