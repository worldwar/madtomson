package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import tw.zhuran.madtomson.P;
import tw.zhuran.madtomson.core.DumbTrunk;

public class LeftDumbTrunk extends DumbTrunk {
    public LeftDumbTrunk(Stage stage) {
        super(stage);
        this.handGroup = new BackGroup("left-stand");
        this.discardGroup = new LeftDiscardGroup();
        this.gangGroup = new LeftDiscardGroup(2, 4);
        this.actionGroup = new LeftActionGroup();
        actionGroup.setX(200);
        actionGroup.setY(P.TABLE_HEIGHT - 100);
    }
}
