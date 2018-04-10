package tw.zhuran.madtomson.core.actor;

import com.github.underscore.$;
import com.github.underscore.Optional;
import com.github.underscore.Predicate;
import tw.zhuran.madtom.domain.*;
import tw.zhuran.madtomson.core.Client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HandActor extends HandGroup {
    private Client client;
    private List<PieceActor> pieceActors;
    private PieceActor feedActor;
    private int start;
    private Piece wildcard;

    public HandActor(Client client) {
        this.client = client;
        pieceActors = new ArrayList<>();
    }

    @Override
    public void init(List<Piece> pieces, Piece wildcard) {
        this.wildcard = wildcard;
        pieceActors.clear();
        for (Piece piece : pieces) {
            PieceActor pieceActor = newPieceActor(piece);
            pieceActors.add(pieceActor);
        }
        Collections.sort(pieceActors);
        update();
    }

    public PieceActor newPieceActor(Piece piece) {
        PieceActor pieceActor = new PieceActor(client, piece, 0);
        pieceActor.setWildcard(wildcard.equals(piece));
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

    @Override
    public void add(Piece piece) {
        feed(piece);
    }

    public void feed(Piece piece) {
        feedActor = newPieceActor(piece);
        feedActor.setIndex(15);
    }

    @Override
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

    @Override
    public void remove(List<Piece> pieces) {
        for (Piece piece : pieces) {
            remove(piece);
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
}
