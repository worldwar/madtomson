package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.*;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtomson.P;
import tw.zhuran.madtomson.core.Client;

public class PieceActor extends Actor {
    private Piece piece;
    private Sprite sprite;
    private int index;
    private Client client;
    private float scale = 0.7f;

    public PieceActor(final Client client, Piece piece, int index) {
        this.client = client;
        this.piece = piece;
        this.sprite = P.sprite(piece);
        this.index = index;
        final PieceActor that = this;
        setTouchable(Touchable.enabled);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                client.tryDiscard(that);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        setBounds(sprite.getWidth() * index * scale, 0, sprite.getWidth(), sprite.getHeight());
        setScale(scale);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setPosition(getX(), getY());
        sprite.setScale(scale);
        sprite.draw(batch);
    }

    public Piece getPiece() {
        return piece;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
