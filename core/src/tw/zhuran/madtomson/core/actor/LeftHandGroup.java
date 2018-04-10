package tw.zhuran.madtomson.core.actor;

import tw.zhuran.madtom.domain.Piece;

import java.util.List;

public class LeftHandGroup extends HandGroup {
    private BackGroup backGroup;

    @Override
    public void init(List<Piece> pieces, Piece wildcard) {
        backGroup = new BackGroup("left-stand");
        backGroup.add(pieces.size());
        addActor(backGroup);
    }

    @Override
    public void add(Piece piece) {
        backGroup.add(1);
    }

    @Override
    public void remove(Piece piece) {
        backGroup.remove(1);
    }

    @Override
    public void remove(List<Piece> pieces) {
        backGroup.remove(pieces.size());
    }
}
