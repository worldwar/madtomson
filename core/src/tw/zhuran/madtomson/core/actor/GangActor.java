package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtomson.core.Client;

import java.util.ArrayList;
import java.util.List;

public class GangActor extends ActionActor {
    private List<Piece> pieces = new ArrayList<>();

    public GangActor(final Client client) {
        super(client, "Gang");
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                client.sendGang();
                getParent().setVisible(false);
                return true;
            }
        });
    }
}
