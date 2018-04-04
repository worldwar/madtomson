package tw.zhuran.madtomson.core.actor;

import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtomson.P;

public class LeftDiscardPieceActor extends PieceActorBase {
    public static float SCALE_X = 0.3f;
    public static float SCALE_Y = 0.4f;
    public LeftDiscardPieceActor(Piece piece) {
        super(piece, P.leftSprite(piece), SCALE_X, SCALE_Y);
        setWidth(sprite.getHeight() * SCALE_Y);
        setHeight(sprite.getWidth() * SCALE_X);
    }
}
