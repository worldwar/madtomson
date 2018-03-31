package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import tw.zhuran.madtomson.P;
import tw.zhuran.madtomson.core.Client;

import java.util.ArrayList;
import java.util.List;

public class ActionActor extends Actor {
    protected Client client;
    protected TextButton textButton;
    protected int index;

    public ActionActor(Client client, String text) {
        this.client = client;
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = P.BITMAP_FONT;
        textButtonStyle.fontColor = Color.BLACK;
        textButton = new TextButton(text, textButtonStyle);
        textButton.scaleBy(1.5f);
        setWidth(50);
        setHeight(20);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        textButton.setBounds(getX(), getY(), getWidth(), getHeight());
        textButton.draw(batch, parentAlpha);
    }

    protected List<Actor> children() {
        return new ArrayList<Actor>();
    }

    public void setIndex(int index) {
        this.index = index;
        setBounds(getParent().getX() + getWidth() * index, 0, getWidth(), getHeight());
    }
}
