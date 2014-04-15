package framework.faces.entities;

import domain.entities.Entity;
import domain.entities.Worker;
import entrypoint.hexagon_maps;
import framework.faces.board.HexMapRenderer;
import processing.core.PApplet;

/**
 * created by Faisal on 3/16/14 6:46 PM
 */
public class WorkerRenderer extends EntityRenderer<Worker>  {

    public WorkerRenderer(Worker entity, hexagon_maps state) {
        super(entity, state);
    }

    @Override
    public void drawRelative() {
        state.rectMode(PApplet.CENTER);

        // left square
        state.rotate(state.frameCount/200.0f);
        state.fill(255, 100);
        state.rect(0, 0, 20, 20);

        // right square
        state.rotate(-state.frameCount/200.0f * 2.0f);
        state.fill(255, 100);
        state.rect(0, 0, 20, 20);
    }
}
