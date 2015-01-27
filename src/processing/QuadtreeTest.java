package processing;

import processing.core.PVector;
import quadtree.Quadtree;
import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by andrew on 1/21/15.
 *
 * @author Andrew Dai (bunsenmcdubbs)
 */
public class QuadtreeTest extends PApplet{

    public static int WIDTH = 800;
    public static int HEIGHT = 640;

    public Quadtree q;

    ArrayList<Rectangle> rects;
    ArrayList<PVector> vels;

    int STARTING_PARTICLE_COUNT = 500;

    Random randomGen;
    public void setup() {
        size(WIDTH, HEIGHT);
        background(100);

        q = new Quadtree(new Rectangle(0, 0, WIDTH, HEIGHT));
        randomGen = new Random();

        rects = new ArrayList<Rectangle>();
        vels = new ArrayList<PVector>();

        for (int i = 0; i < STARTING_PARTICLE_COUNT; i++) {
            Rectangle r = new Rectangle(randomGen.nextInt(WIDTH-20), randomGen.nextInt(HEIGHT-20), 20, 20);
            PVector v = new PVector(randomGen.nextInt(6) - 3, randomGen.nextInt(6) - 3);
            rects.add(r);
            vels.add(v);
        }
    }

    public void draw() {
        background(100);
        moveRects();
        drawQuad(q);
    }

    private void moveRects() {
        q.clear();
        for(int i = 0; i < rects.size(); i++) {
            Rectangle r = rects.get(i);
            PVector v = vels.get(i);
            if (r.x < 0 || r.x > WIDTH - 20) {
                v.x = -v.x;
            }
            if (r.y < 0 || r.y > HEIGHT - 20) {
                v.y = -v.y;
            }
            r.setLocation((int) v.x + r.x, (int) v.y + r.y);
            q.add(rects.get(i));
        }
    }

    public void mouseClicked() {
        Rectangle r = new Rectangle(mouseX - 10, mouseY - 10, 20, 20);
        PVector v = new PVector(randomGen.nextInt(6) - 3, randomGen.nextInt(6) - 3);
        q.add(r);
        vels.add(v);
        rects.add(r);
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
        stroke(200f);
        rect(r.x, r.y, r.width - 1, r.height - 1);
    }

    public void drawRectangle(Rectangle r, int level) {
        stroke(0);
        fill(128 - 10 * level, 50 * level, 256, 50);
        rect(r.x, r.y, r.width, r.height);
    }

}
