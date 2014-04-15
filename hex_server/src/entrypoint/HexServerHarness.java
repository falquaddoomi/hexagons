package entrypoint;

import framework.World;
import framework.network.HexServer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import static java.util.concurrent.TimeUnit.*;

import java.io.IOException;

/**
 * created by Faisal on 3/5/14 4:32 PM
 */
public class HexServerHarness {
    public static void main(String[] args) throws IOException {
        World world = new World();
        HexServer server = new HexServer(world);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        try {
            // we should probably run a world update loop here in a scheduledexecutor thing
            scheduler.scheduleAtFixedRate(server, 0, 200, MILLISECONDS);
        }
        finally {
            // shut down the server, if started
            server.shutdown();
        }

    }
}
