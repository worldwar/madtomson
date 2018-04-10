package tw.zhuran.madtomson.core;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.collect.Lists;
import tw.zhuran.madtom.domain.Action;
import tw.zhuran.madtom.domain.ActionType;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtom.util.F;
import tw.zhuran.madtomson.core.actor.AbstractDiscardGroup;
import tw.zhuran.madtomson.core.actor.HandGroup;
import tw.zhuran.madtomson.core.actor.IndexedGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class DumbTrunk {
    private Client client;
    private Stage stage;
    protected int player;
    protected int handCount;
    protected List<Action> actions;

    protected HandGroup handGroup;
    protected AbstractDiscardGroup discardGroup;
    protected AbstractDiscardGroup gangGroup;
    protected IndexedGroup<Action> actionGroup;

    public DumbTrunk(Client client, Stage stage) {
        this.client = client;
        this.stage = stage;
        handCount = 0;
        actions = new ArrayList<>();
    }

    public void init(List<Piece> pieces, List<Action> actions, Piece wildcard) {
        this.handCount = handCount;
        this.actions = actions;

        stage.addActor(handGroup);
        stage.addActor(discardGroup);
        stage.addActor(gangGroup);
        stage.addActor(actionGroup);

        handGroup.init(pieces, wildcard);
        for (Action action : actions) {
            addAction(action);
        }
    }

    public abstract void feed(Piece piece);

    public void remove(List<Piece> pieces) {
        handCount -= pieces.size();
        handGroup.remove(pieces);
    }

    public void remove(Piece piece) {
        remove(F.repeat(piece, 1));
    }

    public void perform(Action action) {
        if (action.getType() == ActionType.XUGANG) {
            for (Action a : actions) {
                if (a.getType() == ActionType.PENG) {
                    a.xugang();
                    remove(a.getPiece());
                    return;
                }
            }
        } else {
            addAction(action);
            remove(discardPieces(action));
        }
    }

    private void addAction(Action action) {
        actions.add(action);
        if (action.getType() == ActionType.DISCARD) {
            discardGroup.add(action.getPiece());
        } else {
            if (action.getType() == ActionType.PENG ||
                    action.getType() == ActionType.CHI ||
                    action.getType() == ActionType.GANG ||
                    action.getType() == ActionType.ANGANG) {
                actionGroup.add(action);
            } else if (action.getType() == ActionType.HONGZHONG_GANG || action.getType() == ActionType.LAIZI_GANG) {
                gangGroup.add(action.getPiece());
            }
        }
    }

    public static List<Piece> discardPieces(Action action) {
        Piece piece = action.getPiece();
        switch (action.getType()) {
            case DISCARD:
            case XUGANG:
            case HONGZHONG_GANG:
            case LAIZI_GANG:
                return F.repeat(piece, 1);
            case CHI:
                return action.getGroup().partners(piece);
            case PENG:
                return F.repeat(piece, 2);
            case GANG:
                return F.repeat(piece, 3);
            case ANGANG:
                return F.repeat(piece, 4);
        }
        return Lists.newArrayList();
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }
}
