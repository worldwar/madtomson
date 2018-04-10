package tw.zhuran.madtomson.core.actor;

import tw.zhuran.madtom.domain.Action;

public class SelfActionGroup extends IndexedGroup<Action> {
    @Override
    public void add(Action action) {
        addActor(new GroupActor(action, getChildren().size));
    }
}
