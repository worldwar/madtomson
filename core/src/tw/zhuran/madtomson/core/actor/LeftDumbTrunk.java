package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtomson.P;
import tw.zhuran.madtomson.core.Client;
import tw.zhuran.madtomson.core.DumbTrunk;

public class LeftDumbTrunk extends DumbTrunk {
    public LeftDumbTrunk(Client client, Stage stage) {
        super(client, stage);
        this.handGroup = new LeftHandGroup("left-stand");
        this.discardGroup = new LeftDiscardGroup();
        this.gangGroup = new LeftDiscardGroup(2, 4);
        this.actionGroup = new LeftActionGroup();
        actionGroup.setX(200);
        actionGroup.setY(P.TABLE_HEIGHT - 100);
    }

    @Override
    public void feed(Piece piece) {
        handCount++;
        handGroup.add(piece);
    }
}
