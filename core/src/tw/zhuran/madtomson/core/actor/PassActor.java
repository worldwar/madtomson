package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import tw.zhuran.madtomson.core.Client;

public class PassActor extends ActionActor {
    public PassActor(final Client client) {
        super(client, "Pass");
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                client.pass();
                getParent().setVisible(false);
                return true;
            }
        });
    }
}
