package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtomson.P;
import tw.zhuran.madtomson.core.Client;

public class GangOptionActor extends PieceActorBase {
    public static float scale = 0.5f;
    private GangActor gangActor;
    public GangOptionActor(GangActor parent, final Piece piece, int index) {
        super(piece, P.sprite(piece), scale);
        gangActor = parent;
        setX((getWidth() + 10) * index);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Client client = gangActor.client();
                client.genericGang(piece);
                gangActor.hide();
                return true;
            }
        });
    }
}
