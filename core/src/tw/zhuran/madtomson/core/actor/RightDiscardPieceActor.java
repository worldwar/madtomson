package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtomson.P;

public class RightDiscardPieceActor extends PieceActorBase {
    public static float SCALE_X = 0.3f;
    public static float SCALE_Y = 0.4f;
    public RightDiscardPieceActor(Piece piece) {
        super(piece, P.rightSprite(piece), SCALE_X, SCALE_Y);
        setWidth(sprite.getHeight() * SCALE_Y);
        setHeight(sprite.getWidth() * SCALE_X);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setPosition(getX() + getWidth(), getY());
        sprite.setScale(sx, sy);
        sprite.draw(batch);
    }
}
