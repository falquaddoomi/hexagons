package framework.faces;

import entrypoint.hexagon_maps;
import support.Face;

/**
 * created by Faisal on 3/16/14 6:28 PM
 */
public class RenderFace<T> extends Face<T> {
    protected final hexagon_maps state;

    public RenderFace(T wrapped, hexagon_maps state) {
        super(wrapped);
        this.state = state;
    }

    /**
     * Draws the given thing however it should be drawn. Derived classes should override this.
     */
    public void draw() {

    }
}
