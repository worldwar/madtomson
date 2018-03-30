package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import tw.zhuran.madtom.domain.Piece;

import java.util.ArrayList;
import java.util.List;

public class GangActor extends Actor {
    private List<Piece> pieces = new ArrayList<>();

    public GangActor(Piece piece) {
        pieces.add(piece);
    }

    public GangActor(List<Piece> pieces) {
        this.pieces = pieces;
    }
}
