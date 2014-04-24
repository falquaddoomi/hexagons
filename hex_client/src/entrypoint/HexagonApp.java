package entrypoint;

import framework.WorldView;
import framework.interaction.Camera;
import framework.interaction.HUD;
import framework.interaction.Keyboard;
import framework.network.HexClient;
import processing.core.*;
import processing.event.MouseEvent;

import java.awt.event.KeyEvent;
import java.util.prefs.Preferences;

public class HexagonApp extends PApplet {
    // game flags
    public static boolean debug = true;

    // reference objects
    public Preferences prefs;

    // manager objects
    public WorldView game;
    public final Keyboard keys = new Keyboard();
    public final Camera cam = new Camera(this);
    public final HUD hud = new HUD(this);
    public HexClient client;
    // public final EventBus bus = new EventBus();

    public void setup() {
        size(640, 480);
        noStroke();
        colorMode(PConstants.HSB);

        // nab that settings object
        prefs = Preferences.userRoot().node(this.getClass().getName());

        game = new WorldView(this);
        client = new HexClient(this, game);

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
        hud.drawHUD();
    }

    public void keyPressed() { keys.changed((key == PConstants.CODED)?(keyCode + 256):key, true); }
    public void keyReleased() {
        keys.changed((key == PConstants.CODED)?(keyCode + 256):key, false);
        if (key == CODED) {
            if (keyCode == KeyEvent.VK_F1)
                debug = !debug;
        }
    }
    public void mousePressed() {
        if (!hud.mouseEvent(true, mouseButton))
            cam.mouseEvent(true, mouseButton);
    }
    public void mouseReleased() {
        if (!hud.mouseEvent(false, mouseButton))
            cam.mouseEvent(false, mouseButton);
    }

    public void mouseWheel(MouseEvent event) {
        cam.mouseScrolled(event.getAmount());
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "entrypoint.HexagonApp" };
        if (passedArgs != null) {
            PApplet.main(PApplet.concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
