package tw.zhuran.madtomson.core.actor;

import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtomson.P;

public class RightDiscardGroup extends AbstractDiscardGroup {
    private float angleRatio = 0.7f;
    private double angle = Math.toRadians(5f);
    public RightDiscardGroup() {
        super();
        float y = (P.TABLE_HEIGHT - cols * P.SLEEP_DISCARD_WIDTH * LeftDiscardPieceActor.SCALE_Y) * 0.5f + 100;
        setY(y);
        setX(P.TABLE_WIDTH * 0.75f);
    }

    public RightDiscardGroup(int rows, int cols) {
        super();
        this.rows = rows;
        this.cols = cols;
        float y = (P.TABLE_HEIGHT - cols * P.SLEEP_DISCARD_WIDTH * LeftDiscardPieceActor.SCALE_Y) * 0.5f + 100;
        setY(y);
        setX(P.TABLE_WIDTH * 0.75f);
    }

    public void add(PieceActorBase pieceActor) {
        clearChildren();

        int size = pieceActors.size();
        int index = size;
        int row = index / cols;
        int col = index % cols;
        double x = 0 - (rows - row - 1) * pieceActor.getWidth() * 0.9 - col * Math.sin(angle) * angleRatio * pieceActor.getHeight();
        double y = 0 + col * pieceActor.getHeight() * 0.75f;
        pieceActor.setPosition((float)x, (float)y);
        pieceActors.add(pieceActor);

        addAll();
    }

    private void addAll() {
        int lastIndex = pieceActors.size() - 1;
        while (lastIndex >= 0) {
            addActor(pieceActors.get(lastIndex));
            lastIndex--;
        }
    }
    @Override
    public void add(Piece piece) {
        add(new RightDiscardPieceActor(piece));
    }
}
