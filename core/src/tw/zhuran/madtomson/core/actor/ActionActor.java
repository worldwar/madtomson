package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import tw.zhuran.madtomson.P;
import tw.zhuran.madtomson.core.Client;

import java.util.ArrayList;
import java.util.List;

public class ActionActor extends Actor {
    protected Client client;
    protected Sprite sprite;
    protected int index;
    protected float scale;

    public ActionActor(Client client, String text) {
        this.client = client;

        scale = 0.3f;
        sprite = P.action(text.toLowerCase());
        setWidth(P.ACTION_WIDTH * scale);
        setHeight(P.ACTION_HEIGHT * scale);
        setY(20);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.setOrigin(0, 0);
        sprite.setPosition(getX(), getY());
        sprite.setScale(scale);
        sprite.draw(batch);
    }

    protected List<Actor> children() {
        return new ArrayList<Actor>();
    }

    public void setIndex(int index) {
        this.index = index;
        setX(getParent().getX() + getWidth() * index);
    }
}
