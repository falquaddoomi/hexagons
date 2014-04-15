package framework.interaction;

import entrypoint.hexagon_maps;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;

/**
* created by Faisal on 3/5/14 2:00 PM
*/
public class Camera {
    public PVector focus = new PVector();
    boolean dragging = false;
    float scaling = 1.0f;
    ArrayList<MouseListener> listeners = new ArrayList<MouseListener>();
    private hexagon_maps state;

    public Camera(hexagon_maps state) {
        this.state = state;
    }

    public void focus(int x, int y) {
        focus.set(x, y);
    }

    public void update() {
        state.translate(focus.x, focus.y);
        state.scale(scaling);

        if (state.mousePressed && dragging) {
            focus.add(PVector.sub(new PVector(state.mouseX, state.mouseY), new PVector(state.pmouseX, state.pmouseY)));
        }
    }

    // handles camera events; returns true if handled, false otherwise
    public boolean mouseEvent(boolean pressed, int button) {
        boolean handled = false;

        if (pressed) {
            if (state.keys.pressed(PConstants.SHIFT, true)) {
                // if they're holding shift, allow them to pan the screen-
                dragging = true;
                handled = true;
            }
        }
        else {
            if (dragging) {
                dragging = false;
            }
            else {
                // handle mouse up event as an action
                // raise event for listeners
                PVector pos = new PVector((state.mouseX - focus.x) / scaling, (state.mouseY - focus.y) / scaling);

                for (MouseListener l : listeners) {
                    if (l.mouseClicked(pos, button))
                        handled = true;
                }
            }
        }

        return handled;
    }

    public void addListener(MouseListener in) {
        listeners.add(in);
    }

    public void removeListener(MouseListener out) {
        listeners.remove(out);
    }

    public void mouseScrolled(float amount) {
        scaling += amount/20.0f;

        if (scaling <= 0.02f)
           scaling = 0.02f;
    }
}
