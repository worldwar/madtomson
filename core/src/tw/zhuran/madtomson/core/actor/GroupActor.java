package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import tw.zhuran.madtom.domain.Action;
import tw.zhuran.madtom.domain.ActionType;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtomson.P;

import java.util.ArrayList;
import java.util.List;

public class GroupActor extends Actor {
    private Action action;
    private int index;
    private List<Sprite> sprites;
    private Sprite sprite;
    private float scale = 0.6f;


    public GroupActor(Action action) {
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
        setRotation(15);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int i = 0;
        for (Sprite sprite : sprites) {
            sprite.setPosition(getX() + sprite.getWidth() * scale * i, getY());
            sprite.setScale(scale);
            sprite.setRotation(getRotation());
            sprite.draw(batch, parentAlpha);
            i++;
        }

        if (action.getType() == ActionType.GANG || action.getType() == ActionType.XUGANG) {
            sprite.setPosition(getX() + sprite.getWidth() * scale, getY() + 10);
            sprite.setScale(scale);
            sprite.setRotation(getRotation());
            sprite.draw(batch, parentAlpha);
        }
    }
}