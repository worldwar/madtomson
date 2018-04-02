package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import tw.zhuran.madtomson.P;

public class BackGroup extends Actor {
    private int size = 0;
    private float scale = 1.2f;
    private float angleRatio = 0.7f;
    private double angle = Math.toRadians(15f);
    private Sprite sprite;

    public BackGroup(String name) {
        this.sprite = P.back(name);
        setX(100);
        setY(200);
    }

    public void increment() {
        add(1);
    }

    public void decrement() {
        remove(1);
    }

    public void add(int delta) {
        this.size += delta;
    }

    public void remove(int delta) {
        this.size -= delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (int i = size - 1; i >= 0; i--) {
            double x = getX() + sprite.getWidth() * scale * angleRatio * Math.sin(angle) * i;
            double y = getY() + sprite.getWidth() * scale * angleRatio * Math.cos(angle) * i;
            sprite.setPosition((int)x, (int)y);
            sprite.setScale(scale);
            sprite.draw(batch, parentAlpha);
        }
    }
}
