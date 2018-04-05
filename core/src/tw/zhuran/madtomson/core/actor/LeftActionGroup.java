package tw.zhuran.madtomson.core.actor;

import tw.zhuran.madtom.domain.Action;

public class LeftActionGroup extends IndexedGroup<Action> {
    @Override
    public void add(Action action) {
        addActor(new LeftGroupActor(action, getChildren().size));
    }
}
