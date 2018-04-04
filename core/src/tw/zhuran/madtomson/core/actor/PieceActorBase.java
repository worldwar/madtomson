package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import tw.zhuran.madtom.domain.Piece;

public class PieceActorBase extends Actor {
    protected Piece piece;
    protected Sprite sprite;
    protected float sx;
    protected float sy;

    public PieceActorBase(Piece piece, Sprite sprite, float scale) {
        this(piece, sprite, scale, scale);
    }

    public PieceActorBase(Piece piece, Sprite sprite, float sx, float sy) {
        this.piece = piece;
        this.sprite = sprite;
        this.sx = sx;
        this.sy = sy;
        setWidth(sprite.getWidth() * sx);
        setHeight(sprite.getHeight() * sy);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setPosition(getX(), getY());
        sprite.setScale(sx, sy);
        sprite.draw(batch);
    }

    public Piece piece() {
        return piece;
    }
}
