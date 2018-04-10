package tw.zhuran.madtomson.core;

import com.badlogic.gdx.scenes.scene2d.Stage;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtomson.P;
import tw.zhuran.madtomson.core.actor.*;

public class RightDumbTrunk extends DumbTrunk {
    public RightDumbTrunk(Client client, Stage stage) {
        super(client, stage);
        this.handGroup = new LeftHandGroup("right-stand");
        this.discardGroup = new RightDiscardGroup();
        this.gangGroup = new RightDiscardGroup(2, 4);
        gangGroup.setY(150);
        this.actionGroup = new RightActionGroup();
        actionGroup.setX(P.TABLE_WIDTH - 100);
        actionGroup.setY(100);
    }

    @Override
    public void feed(Piece piece) {
        handCount++;
        handGroup.add(piece);
    }
}
