package tw.zhuran.madtomson.core.actor;

import tw.zhuran.madtom.domain.Group;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtomson.core.Client;

import java.util.List;

public class ChiActor extends ActionActor {
    private Piece piece;
    private List<Group> groups;

    public ChiActor(Client client, Piece piece, List<Group> groups) {
        super(client, "Chi");
        setBounds(50, 200, 50, 20);
        this.piece = piece;
        this.groups = groups;
    }
}
