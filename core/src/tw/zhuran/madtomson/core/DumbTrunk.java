package tw.zhuran.madtomson.core;

import tw.zhuran.madtom.domain.Action;
import tw.zhuran.madtom.domain.ActionType;

import java.util.ArrayList;
import java.util.List;

public class DumbTrunk {
    private int handCount;
    private List<Action> actions;

    public DumbTrunk() {
        handCount = 0;
        actions = new ArrayList<>();
    }

    public void init(int handCount, List<Action> actions) {
        this.handCount = handCount;
        this.actions = actions;
    }

    public void feed() {
        handCount++;
    }


    public void remove(int delta) {
        handCount -= delta;
    }

    public void perform(Action action) {
        if (action.getType() == ActionType.XUGANG) {
            for (Action a : actions) {
                if (a.getType() == ActionType.PENG) {
                    a.xugang();
                    return;
                }
            }
        } else {
            actions.add(action);
            int count = discardCount(action.getType());
            remove(count);
        }
    }

    public int discardCount(ActionType type) {
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
