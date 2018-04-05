package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.github.underscore.$;
import com.github.underscore.Optional;
import com.github.underscore.Predicate;
import tw.zhuran.madtom.domain.Action;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtom.domain.Trunk;
import tw.zhuran.madtom.util.F;
import tw.zhuran.madtomson.core.Client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HandActor extends Group {
    private Client client;
    private Trunk trunk;
    private List<GroupActor> groupActors;
    private List<PieceActor> pieceActors;
    private PieceActor feedActor;
    private int start;

    public HandActor(Client client) {
        this.client = client;
        groupActors = new ArrayList<>();
        pieceActors = new ArrayList<>();
    }

    public void init() {
        pieceActors.clear();
        trunk = client.trunk();
        for (Piece piece : trunk.getHand().all()) {
            PieceActor pieceActor = newPieceActor(piece);
            pieceActors.add(pieceActor);
        }
        Collections.sort(pieceActors);
        update();
        for (Action action : trunk.getActions()) {
            addGroup(action);
        }
    }

    public PieceActor newPieceActor(Piece piece) {
        PieceActor pieceActor = new PieceActor(client, piece, 0);
        pieceActor.setWildcard(trunk.getHand().getWildcard().equals(piece));
        addActor(pieceActor);
        return pieceActor;
    }

    public void update() {
        if (pieceActors.size() == 14 && feedActor == null) {
            feedActor = pieceActors.remove(13);
            feedActor.setIndex(15);
        }
        start = 14 - pieceActors.size();

        int i = 0;
        for (PieceActor actor : pieceActors) {
            actor.setIndex(start + i);
            i++;
        }
    }

    private PieceActor findPieceActor(final Piece piece) {
        Optional<PieceActor> pieceActorOptional = $.find(pieceActors, new Predicate<PieceActor>() {
            @Override
            public Boolean apply(PieceActor arg) {
                return arg.getPiece().equals(piece);
            }
        });
        return pieceActorOptional.orNull();
    }

    public void feed(Piece piece) {
        feedActor = newPieceActor(piece);
        feedActor.setIndex(15);
    }

    public void remove(Piece piece) {
        if (feedActor != null && piece.equals(feedActor.getPiece())) {
            removeFeedActor();
            return;
        }
        PieceActor pieceActor = findPieceActor(piece);
        if (pieceActor != null) {
            pieceActors.remove(pieceActor);
            pieceActor.remove();

            if (feedActor != null) {
                insertPieceActor(feedActor);
                feedActor = null;
            }
            update();
        }
    }

    public void remove(List<Piece> pieces) {
        for (Piece piece : pieces) {
            remove(piece);
        }
    }

    public void performAction(Action action) {
        remove(partners(action));
        addGroup(action);
    }

    private void addGroup(Action action) {
        switch (action.getType()) {
            case CHI: case PENG: case GANG: case ANGANG:
                GroupActor groupActor = new GroupActor(action, groupActors.size());
                groupActors.add(groupActor);
                addActor(groupActor);
        }
    }

    public void insertPieceActor(PieceActor pieceActor) {
        pieceActors.add(pieceActor);
        Collections.sort(pieceActors);
    }

    public void removeFeedActor() {
        if (feedActor != null) {
            feedActor.remove();
            feedActor = null;
        }
    }

    public List<Piece> partners(Action action) {
        switch (action.getType()) {
            case CHI:
            case PENG:
                return action.getGroup().partners(action.getPiece());
            case GANG:
                return F.repeat(action.getPiece(), 3);
            case ANGANG:
                return F.repeat(action.getPiece(), 4);
            case HONGZHONG_GANG:
            case XUGANG:
            case LAIZI_GANG:
                return F.repeat(action.getPiece(), 1);
        }
        return new ArrayList<>();
    }
}
