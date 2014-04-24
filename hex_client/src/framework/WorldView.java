package framework;

import domain.board.HexCoord;
import domain.board.HexMap;
import domain.board.HexRegion;
import domain.effects.Effect;
import entrypoint.HexagonApp;
import framework.faces.board.HexMapRenderer;
import framework.faces.entities.EntityRenderer;
import framework.interaction.KeyListener;
import framework.interaction.MouseListener;
import framework.faces.EffectSetRenderer;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * created by Faisal on 3/10/14 4:38 PM
 */
public class WorldView implements KeyListener, MouseListener {
    public final HexMap cache;
    public final HashMap<Long, EntityRenderer> entities = new HashMap<Long, EntityRenderer>();
    public final ArrayList<Effect> effects = new ArrayList<Effect>();
    public HexagonApp state;
    private final HexMapRenderer renderer;
    private final EffectSetRenderer fx_renderer;

    public WorldView(HexagonApp state) {
        this.state = state;
        this.cache = new HexMap();
        this.renderer = new HexMapRenderer(state, this.cache);
        this.fx_renderer = new EffectSetRenderer(state, this.effects);
    }

    public void draw() {
        renderer.draw();

        for (EntityRenderer e : entities.values())
            e.draw();

        // draw effects on top of the board
        fx_renderer.draw();
    }

    public void addRegion(HexRegion region) {
        cache.map.putAll(region.region);
    }

    public boolean mouseClicked(PVector pos, int button) {
        if (button == PApplet.LEFT) {
            // select the tile that the user clicked
            HexCoord clicked = renderer.getHexCoordForPixel(pos.x, pos.y);
            renderer.setSelection(clicked);
        }
        else if (button == PApplet.RIGHT) {
            // issue an order if there's a selection
            if (renderer.getSelection() != null) {
                // issue a move order from the selection to the new selection
                HexCoord clicked = renderer.getHexCoordForPixel(pos.x, pos.y);
                state.client.sendMoveRequest(renderer.getSelection(), clicked);

                // and change our selection to the target
                renderer.setSelection(clicked);
            }
        }

        return true;
    }

    public boolean keyPressed(int k) {
        return false;
    }

    public boolean keyReleased(int k) {
        return false;
    }
}
