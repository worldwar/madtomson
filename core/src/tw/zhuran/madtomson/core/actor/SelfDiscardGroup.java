package tw.zhuran.madtomson.core.actor;

import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtomson.P;

public class SelfDiscardGroup extends AbstractDiscardGroup {
    public SelfDiscardGroup() {
        super();
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

    @Override
    public void add(Piece piece) {
        add(new DiscardPieceActor(piece));
    }
}
