package tw.zhuran.madtomson.core.actor;

import tw.zhuran.madtomson.P;

import java.util.ArrayList;

public class LeftDiscardGroup extends DiscardGroup {

    private float angleRatio = 0.7f;
    private double angle = Math.toRadians(5f);
    public LeftDiscardGroup() {
        pieceActors = new ArrayList<>();
        float y = (P.TABLE_HEIGHT - cols * P.SLEEP_DISCARD_WIDTH * LeftDiscardPieceActor.SCALE) / 2;
        setY(y);
        setX(P.TABLE_WIDTH * 0.1f);
    }

    @Override
    public void add(PieceActorBase pieceActor) {
        clearChildren();

        int size = pieceActors.size();
        int index = size;
        int row = index / cols;
        int col = index % cols;
        double x = getX() + (rows - row - 1) * pieceActor.getWidth() * 0.9 - col * Math.sin(angle) * angleRatio * pieceActor.getHeight();
        double y = getY() - col * pieceActor.getHeight() * 0.75f;
        pieceActor.setPosition((float)x, (float)y);
        pieceActors.add(pieceActor);

        addAll();
    }

    private void addAll() {
        int size = pieceActors.size();
        int currentRows = (size - 1) / cols + 1;
        for (int i = currentRows - 1; i >=0; i--) {
            for (int j = 0; j < cols; j++) {
                int index = i * cols + j;
                if (index >= size) {
                    break;
                }
                addActor(pieceActors.get(index));
            }
        }
    }
}
