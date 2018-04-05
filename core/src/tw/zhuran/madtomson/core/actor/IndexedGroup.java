package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.scenes.scene2d.Group;

public abstract class IndexedGroup<T> extends Group {
    public abstract void add(T t);
}
