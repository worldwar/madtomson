package tw.zhuran.madtomson.core;

import com.badlogic.gdx.scenes.scene2d.Stage;
import tw.zhuran.madtom.domain.Action;
import tw.zhuran.madtom.domain.ActionType;
import tw.zhuran.madtomson.core.actor.AbstractDiscardGroup;
import tw.zhuran.madtomson.core.actor.BackGroup;
import tw.zhuran.madtomson.core.actor.IndexedGroup;

import java.util.ArrayList;
import java.util.List;

public class DumbTrunk {
    private Stage stage;
    private int handCount;
    private List<Action> actions;

    protected BackGroup handGroup;
    protected AbstractDiscardGroup discardGroup;
    protected AbstractDiscardGroup gangGroup;
    protected IndexedGroup<Action> actionGroup;

    public DumbTrunk(Stage stage) {
        this.stage = stage;
        handCount = 0;
        actions = new ArrayList<>();
    }

    public void init(int handCount, List<Action> actions) {
        this.handCount = handCount;
        this.actions = actions;

        stage.addActor(handGroup);
        stage.addActor(discardGroup);
        stage.addActor(gangGroup);
        stage.addActor(actionGroup);

        handGroup.add(handCount);
        for (Action action : actions) {
            addAction(action);
        }
    }

    public void feed() {
        handCount++;
        handGroup.increment();
    }

    public void remove(int delta) {
        handCount -= delta;
        handGroup.remove(delta);
    }

    public void perform(Action action) {
        if (action.getType() == ActionType.XUGANG) {
            for (Action a : actions) {
                if (a.getType() == ActionType.PENG) {
                    a.xugang();
                    remove(1);
                    return;
                }
            }
        } else {
            addAction(action);
            int count = discardCount(action.getType());
            remove(count);
        }
    }

    private void addAction(Action action) {
        actions.add(action);
        if (action.getType() == ActionType.DISCARD) {
            discardGroup.add(action.getPiece());
        } else {
            if (action.getType() == ActionType.PENG ||
                    action.getType() == ActionType.CHI ||
                    action.getType() == ActionType.GANG ||
                    action.getType() == ActionType.ANGANG) {
                actionGroup.add(action);
            } else if (action.getType() == ActionType.HONGZHONG_GANG || action.getType() == ActionType.LAIZI_GANG) {
                gangGroup.add(action.getPiece());
            }
        }
    }

    public static int discardCount(ActionType type) {
        switch (type) {
            case DISCARD:
            case XUGANG:
            case HONGZHONG_GANG:
            case LAIZI_GANG:
                return 1;
            case CHI:
            case PENG:
                return 2;
            case GANG:
                return 3;
            case ANGANG:
                return 4;
        }
        return 0;
    }
}
