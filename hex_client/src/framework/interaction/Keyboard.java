package framework.interaction;

import java.util.ArrayList;

/**
* created by Faisal on 3/5/14 2:00 PM
*/
public class Keyboard {
    boolean keys[] = new boolean[512];
    ArrayList<KeyListener> listeners = new ArrayList<KeyListener>();

    public void changed(int k, boolean state) {
        keys[k] = state;

        // broadcast notification to all listeners
        int transformed = (k >= 256)?(k-256):k;
        for (KeyListener l : listeners)
            if (state) { l.keyPressed(transformed); } else { l.keyReleased(transformed); }
    }
    public boolean pressed(int k, boolean special) {
        // if (k != UP && k != DOWN && k != LEFT && k != RIGHT && k != ALT && k != CONTROL && k != SHIFT)

        if (!special)
            return keys[k];
        return keys[k + 256];
    }

    public void addListener(KeyListener in) {
        listeners.add(in);
    }

    public void removeListener(KeyListener out) {
        listeners.remove(out);
    }
}
