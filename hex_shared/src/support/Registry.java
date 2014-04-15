package support;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;
import domain.board.HexCoord;
import domain.board.HexRegion;
import domain.board.HexTile;
import domain.effects.Effect;
import domain.entities.Entity;
import domain.entities.EntityList;
import domain.entities.Worker;
import exchanges.MoveRequest;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * created by Faisal on 3/5/14 4:46 PM
 */
public class Registry {
    public static void register(Kryo kryo) {
        // change the serialization format first
        // kryo.setDefaultSerializer(TaggedFieldSerializer.class);

        // and register some library/core classes
        kryo.register(HashMap.class);
        kryo.register(ArrayList.class);
        kryo.register(PVector.class);

        // domain entities
        kryo.register(HexRegion.class);
        kryo.register(HexTile.class);
        kryo.register(HexCoord.class);

        kryo.register(Entity.class);
        kryo.register(Worker.class);
        kryo.register(EntityList.class);

        // client actions/responses
        kryo.register(MoveRequest.class);
        kryo.register(Effect.class);
    }
}
