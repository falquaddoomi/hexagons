package framework.faces;

import framework.World;
import support.Face;

/**
 * created by Faisal on 3/16/14 6:33 PM
 */
public class UpdateFace<T> extends Face<T> {
    protected final World world;

    public UpdateFace(T wrapped, World world) {
        super(wrapped);
        this.world = world;
    }

    /**
     * Updates the given thing however it should be updated. Derived classes should override this.
     * @return true if the update changed our state (and thus should be synced to the client), false otherwise.
     */
    public boolean update() {
        return false;
    }
}
