package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtomson.P;

public class LeftDiscardPieceActor extends PieceActorBase {
    public static float SCALE = 1.0f;
    public LeftDiscardPieceActor(Piece piece) {
        super(piece, P.leftSprite(piece), SCALE);
        setWidth(sprite.getHeight() * SCALE);
        setHeight(sprite.getWidth() * SCALE);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setPosition(getX(), getY());
        sprite.setScale(scale);
        sprite.draw(batch);
    }
}
