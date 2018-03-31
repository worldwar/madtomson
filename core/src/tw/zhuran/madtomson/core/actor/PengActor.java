package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtomson.core.Client;

public class PengActor extends ActionActor {
    private Piece piece;

    public PengActor(final Client client, final Piece piece) {
        super(client, "Peng");
        this.piece = piece;
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                client.sendPeng(piece);
                getParent().setVisible(false);
                return true;
            }
        });
    }
}
