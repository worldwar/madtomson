package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.scenes.scene2d.Group;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtomson.P;

import java.util.ArrayList;
import java.util.List;

public class DiscardGroup extends Group {
    protected List<PieceActorBase> pieceActors;
    protected int cols = 6;
    protected int rows = 4;

    public DiscardGroup() {
        pieceActors = new ArrayList<>();
        float x = (P.TABLE_WIDTH - cols * P.PIECE_WIDTH * DiscardPieceActor.scale) / 2;
        setX(x);
        setY(P.TABLE_HEIGHT * 0.1f);
    }

    public void add(PieceActorBase pieceActor) {
        int size = pieceActors.size();
        int index = size;
        int row = index / cols;
        int col = index % cols;
        pieceActor.setX(col * pieceActor.getWidth());
        pieceActor.setY(getY() + (rows - row - 1)  * pieceActor.getHeight() * 0.82f);
        pieceActors.add(pieceActor);
        addActor(pieceActor);
    }

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
