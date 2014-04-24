package framework.interaction;

import entrypoint.HexagonApp;
import processing.core.PConstants;

/**
 * Provides methods for both drawing overlays on the game field
 * and for displaying non-overlay (i.e. window-relative) data.
 */
public class HUD {
    protected final HexagonApp state;
    protected boolean was_handling = false; // true if we captured a mousedown previously

    public HUD(HexagonApp state) {
        this.state = state;
    }

    public void drawHUD() {
        // output a bit of debugging text
        if (HexagonApp.debug) {
            state.textAlign(PConstants.LEFT, PConstants.TOP);
            state.fill(255);
            state.text("Tiles: " + state.game.cache.map.size(), 5, 5);
            state.text("Entities: " + state.game.entities.size(), 5, 12);
        }
    }

    /***
     * Decorates the transformed map with information
     * TODO: actually implement HUD's drawOverlay method
     */
    public void drawOverlay() {

    }

    /**
     * Handles untransformed (i.e. window-relative) mouse-clicking events.
     * @param pressed whether the mouse is pressed or not
     * @param button which mouse button was pressed
     * @return true if handled, false if the event should continue
     */
    public boolean mouseEvent(boolean pressed, int button) {
        boolean handled = false;

        // we can't possibly handle a mouseup event for which we weren't handling the mousedown
        if (!was_handling && !pressed)
            return false;

        // TODO: window events occur in here

        // this should occur last; if we handled the task and it was a mousedown, we should
        // probably also handle the mouseup when it occurs
        if (handled && pressed)
            was_handling = true;

        return handled;
    }
}
