package framework.interaction.widgets;

import entrypoint.HexagonApp;
import processing.core.PVector;

/**
 * created by Faisal on 4/23/2014 11:51 AM
 */
public abstract class Widget {
    protected final HexagonApp state;
    protected int x, y, width, height;

    public Widget(HexagonApp state, int x, int y, int width, int h) {
        this.state = state;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = h;
    }

    public abstract void draw();

    public PVector getPosition() {
        return new PVector(x, y);
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
