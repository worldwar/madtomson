package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import tw.zhuran.madtom.domain.Piece;

public class PieceActorBase extends Actor {
    private Piece piece;
    private Sprite sprite;
    private float scale;

    public PieceActorBase(Piece piece, Sprite sprite, float scale) {
        this.piece = piece;
        this.sprite = sprite;
        this.scale = scale;
        setWidth(sprite.getWidth() * scale);
        setHeight(sprite.getHeight() * scale);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setPosition(getX(), getY());
        sprite.setScale(scale);
        sprite.draw(batch);
    }

    public Piece piece() {
        return piece;
    }
}
