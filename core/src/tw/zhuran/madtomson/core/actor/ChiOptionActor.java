package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import tw.zhuran.madtom.domain.Action;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtomson.P;
import tw.zhuran.madtomson.core.Client;

import java.util.ArrayList;
import java.util.List;

public class ChiOptionActor extends Actor {
    private Action action;
    private int index;
    private List<Sprite> sprites;
    private Sprite sprite;
    private float scale = 0.4f;
    private ChiActor chiActor;

    public ChiOptionActor(ChiActor parent, final Action action, int index) {
        this.chiActor = parent;
        this.action = action;
        this.index = index;
        sprites = new ArrayList<>(3);
        for (Piece piece : action.getGroup().getPieces()) {
            Sprite sprite = P.sprite(piece);
            sprites.add(sprite);
            if (this.sprite == null) {
                this.sprite = sprite;
            }
        }

        setWidth(sprite.getWidth() * 3 * scale);
        setHeight(sprite.getHeight() * scale);
        setX(getWidth() * index);
        setY(parent.getY() + 50);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Client client = chiActor.client();
                client.sendChi(action.getPiece(), action.getGroup());
                chiActor.hide();
                return true;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int i = 0;
        for (Sprite sprite : sprites) {
            sprite.setPosition(getX() + sprite.getWidth() * scale * i, getY());
            sprite.setScale(scale);
            sprite.draw(batch, parentAlpha);
            i++;
        }
    }
}
