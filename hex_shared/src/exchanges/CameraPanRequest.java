package exchanges;

import domain.board.HexCoord;

/**
 * created by Faisal on 4/21/2014 2:33 PM
 */
public class CameraPanRequest extends Request {
    public HexCoord newLocation;

    public CameraPanRequest() { }

    public CameraPanRequest(HexCoord newLocation) {
        this.newLocation = newLocation;
    }
}
