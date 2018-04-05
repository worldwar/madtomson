package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.scenes.scene2d.Group;
import tw.zhuran.madtom.domain.Piece;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDiscardGroup extends Group {
    protected List<PieceActorBase> pieceActors = new ArrayList<>();
    protected int cols = 6;
    protected int rows = 4;


    public abstract void add(Piece piece);

    public void remove(Piece piece) {
        for (int i = pieceActors.size() - 1; i >= 0; i--) {
            PieceActorBase pieceActor = pieceActors.get(i);
            if (pieceActor.piece().equals(piece)) {
                pieceActors.remove(i);
                pieceActor.remove();
                return;
            }
        }
    }
}
