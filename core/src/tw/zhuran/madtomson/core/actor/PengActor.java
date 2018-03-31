package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import tw.zhuran.madtomson.core.Client;

public class PengActor extends ActionActor {

    public PengActor(final Client client) {
        super(client, "Peng");
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                client.sendPeng();
                getParent().setVisible(false);
                return true;
            }
        });
    }
}
