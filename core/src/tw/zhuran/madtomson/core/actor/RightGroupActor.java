package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.scenes.scene2d.Group;
import tw.zhuran.madtom.domain.Action;
import tw.zhuran.madtom.domain.ActionType;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtom.domain.Pieces;

import java.util.List;

public class RightGroupActor extends Group {
    private float angleRatio = 0.7f;
    private double angle = Math.toRadians(5f);
    public RightGroupActor(Action action, int index) {
        RightDiscardPieceActor template = new RightDiscardPieceActor(Pieces.HONGZHONG);

        setWidth(template.getWidth());
        setHeight(template.getHeight() * 3);

        setX((float) (0 - getWidth() * index * Math.sin(angle)));
        setY((float) (0 + getHeight() * index * Math.cos(angle)));

        int i = 0;

        PieceActorBase secondActor = null;
        if (action.getType() == ActionType.ANGANG) {
            angle = Math.toRadians(15f);
        }

        List<Piece> pieces = action.getGroup().getPieces();
        for (i = pieces.size() - 1; i >= 0; i--) {
            PieceActorBase actor;
            if (action.getType() == ActionType.ANGANG) {
                actor = new RightBackSleepPieceActor();
            } else {
                actor = new RightDiscardPieceActor(pieces.get(i));
            }
            if (i == 1) {
                secondActor = actor;
            }

            double x = getX() - i * Math.sin(angle) * angleRatio * actor.getHeight();
            double y = i * actor.getHeight() * 0.75f;
            actor.setPosition((float)x, (float) y);
            addActor(actor);
        }

        if (action.getType() == ActionType.GANG || action.getType() == ActionType.XUGANG || action.getType() == ActionType.ANGANG) {
            PieceActorBase actor;
            float leftDeltaX;
            if (action.getType() == ActionType.ANGANG) {
                actor = new RightBackSleepPieceActor();
                leftDeltaX = 0.15f;
            } else {
                actor = new RightDiscardPieceActor(action.getPiece());
                leftDeltaX = 0.05f;
            }
            float x = secondActor.getX() + secondActor.getWidth() * leftDeltaX;
            float y = secondActor.getY() + secondActor.getHeight() * 0.25f;
            actor.setPosition(x, y);
            addActor(actor);
        }
    }
}
