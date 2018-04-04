package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.scenes.scene2d.Group;
import tw.zhuran.madtom.domain.Action;
import tw.zhuran.madtom.domain.ActionType;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtom.domain.Pieces;

public class LeftGroupActor extends Group {
    private float angleRatio = 0.7f;
    private double angle = Math.toRadians(5f);

    public LeftGroupActor(Action action, int index) {
        LeftDiscardPieceActor template = new LeftDiscardPieceActor(Pieces.HONGZHONG);

        setWidth(template.getWidth());
        setHeight(template.getHeight() * 3);

        setX((float) (0 - getWidth() * index * Math.sin(angle)));
        setY((float) (0 - getHeight() * index * Math.cos(angle)));

        int i = 0;

        LeftDiscardPieceActor secondActor = null;
        for (Piece piece : action.getGroup().getPieces()) {
            LeftDiscardPieceActor actor;
            if (action.getType() == ActionType.ANGANG) {
                actor = new LeftDiscardPieceActor(null);
            } else {
                actor = new LeftDiscardPieceActor(piece);
            }
            if (i == 1) {
                secondActor = actor;
            }

            double x = 0 - i * Math.sin(angle) * angleRatio * actor.getHeight();
            double y = 0 - i * actor.getHeight() * 0.75f;
            actor.setPosition((float)x, (float) y);
            addActor(actor);
            i++;
        }

        if (action.getType() == ActionType.GANG || action.getType() == ActionType.XUGANG || action.getType() == ActionType.ANGANG) {
            LeftDiscardPieceActor actor = new LeftDiscardPieceActor(action.getPiece());
            float x = secondActor.getX() - secondActor.getWidth() * 0.3f;
            float y = getY() - i * actor.getHeight() * 0.25f;
            actor.setPosition(x, y);
            addActor(actor);
        }
    }
}
