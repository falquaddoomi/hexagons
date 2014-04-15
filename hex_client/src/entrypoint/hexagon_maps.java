package entrypoint;

import com.google.common.eventbus.EventBus;
import framework.WorldView;
import framework.interaction.Camera;
import framework.interaction.Keyboard;
import framework.network.HexClient;
import processing.core.*;
import processing.event.MouseEvent;

import java.awt.event.KeyEvent;

public class hexagon_maps extends PApplet {
    public WorldView game;
    public Keyboard keys = new Keyboard();
    public Camera cam = new Camera(this);
    public HexClient client;
    public boolean debug = false;

    public EventBus bus = new EventBus();

    public void setup() {
        size(400, 400);
        noStroke();
        colorMode(PConstants.HSB);
        game = new WorldView(this);
        client = new HexClient(game);

        cam.focus(width/2, height/2);
        cam.addListener(game);

        textFont(createFont("04b03", 8, false));
        textAlign(PConstants.LEFT, PConstants.CENTER);
    }

    public void draw() {
        background(0);

        pushMatrix();

        // update camera, which applies translation
        // and also listens for scrolling events
        cam.update();

        fill(105, 200, 150);
        game.draw();
        popMatrix();

        // output a bit of debugging text
        if (debug) {
            textAlign(PConstants.LEFT, PConstants.TOP);
            fill(255);
            text("Tiles: " + game.cache.map.size(), 5, 5);
            text("Entities: " + game.entities.size(), 5, 12);
        }
    }

    public void keyPressed() { keys.changed((key == PConstants.CODED)?(keyCode + 256):key, true); }
    public void keyReleased() {
        keys.changed((key == PConstants.CODED)?(keyCode + 256):key, false);
        if (key == CODED) {
            if (keyCode == KeyEvent.VK_F1)
                debug = !debug;
        }
    }
    public void mousePressed() { cam.mouseEvent(true, mouseButton); }
    public void mouseReleased() { cam.mouseEvent(false, mouseButton); }

    public void mouseWheel(MouseEvent event) {
        cam.mouseScrolled(event.getAmount());
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "entrypoint.hexagon_maps" };
        if (passedArgs != null) {
            PApplet.main(PApplet.concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
