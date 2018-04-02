package tw.zhuran.madtomson.core.actor;

import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtomson.P;

public class DiscardPieceActor extends PieceActorBase {
    public static float scale = 0.5f;
    public DiscardPieceActor(Piece piece) {
        super(piece, P.showSelfSprite(piece), scale);
    }
}
