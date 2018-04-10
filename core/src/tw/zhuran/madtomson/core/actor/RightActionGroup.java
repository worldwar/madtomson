package tw.zhuran.madtomson.core.actor;

import tw.zhuran.madtom.domain.Action;

public class RightActionGroup extends IndexedGroup<Action> {
    @Override
    public void add(Action action) {
        addActor(new RightGroupActor(action, getChildren().size));
    }
}
