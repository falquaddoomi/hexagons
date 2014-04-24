package framework.faces;

import entrypoint.HexagonApp;
import support.Face;

/**
 * created by Faisal on 3/16/14 6:28 PM
 */
public class RenderFace<T> extends Face<T> {
    protected final HexagonApp state;

    public RenderFace(T wrapped, HexagonApp state) {
        super(wrapped);
        this.state = state;
    }

    /**
     * Draws the given thing however it should be drawn. Derived classes should override this.
     */
    public void draw() {

    }
}
