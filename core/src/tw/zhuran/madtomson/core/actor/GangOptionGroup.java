package tw.zhuran.madtomson.core.actor;

import tw.zhuran.madtom.domain.Piece;

public class GangOptionGroup extends IndexedGroup<Piece> {
    private GangActor gangActor;

    public GangOptionGroup(GangActor gangActor) {
        this.gangActor = gangActor;
    }

    @Override
    public void add(Piece piece) {
        GangOptionActor actor = new GangOptionActor(gangActor, piece, getChildren().size);
        actor.setVisible(false);
        addActor(actor);
    }
}
