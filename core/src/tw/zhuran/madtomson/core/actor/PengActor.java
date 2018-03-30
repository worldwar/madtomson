package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import tw.zhuran.madtom.domain.Piece;

public class PengActor extends Actor {
    private Piece piece;

    public PengActor(Piece piece) {
        this.piece = piece;
    }
}
