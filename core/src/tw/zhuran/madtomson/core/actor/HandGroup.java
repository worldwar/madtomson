package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.scenes.scene2d.Group;
import tw.zhuran.madtom.domain.Piece;

import java.util.List;

public abstract class HandGroup extends Group {
    public abstract void init(List<Piece> pieces, Piece wildcard);
    public abstract void add(Piece piece);
    public abstract void remove(Piece piece);
    public abstract void remove(List<Piece> pieces);
}
