package tw.zhuran.madtomson.core;

import com.badlogic.gdx.scenes.scene2d.Stage;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtomson.core.actor.HandActor;
import tw.zhuran.madtomson.core.actor.SelfActionGroup;
import tw.zhuran.madtomson.core.actor.SelfDiscardGroup;

public class SelfDumbTrunk extends DumbTrunk {
    public SelfDumbTrunk(Client client, Stage stage) {
        super(client, stage);
        handGroup = new HandActor(client);
        discardGroup = new SelfDiscardGroup();
        actionGroup = new SelfActionGroup();
        gangGroup = new SelfDiscardGroup(4, 2);
        gangGroup.setX(150);
        gangGroup.setY(50);
    }

    @Override
    public void feed(Piece piece) {
        handGroup.add(piece);
    }
}
