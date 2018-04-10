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
    private String name;

    public BackGroup(String name) {
        this.name = name;
        this.sprite = P.back(name);
        if (name.equals("left-stand")) {
            setX(100);
            setY(200);
        } else if (name.equals("right-stand")) {
            setX(P.TABLE_WIDTH - 200);
            setY(P.TABLE_HEIGHT - 200);
        }
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
            double x = 0, y = 0;
            if (name.equals("left-stand")) {
                x = getX() + sprite.getWidth() * scale * angleRatio * Math.sin(angle) * i;
                y = getY() + sprite.getWidth() * scale * angleRatio * Math.cos(angle) * i;
            } else if (name.equals("right-stand")) {
                x = getX() + sprite.getWidth() * scale * angleRatio * Math.sin(angle) * (size - i);
                y = getY() - sprite.getWidth() * scale * angleRatio * Math.cos(angle) * (size - i);
            }
            sprite.setPosition((int)x, (int)y);
            sprite.setScale(scale);
            sprite.draw(batch, parentAlpha);
        }
    }
}
