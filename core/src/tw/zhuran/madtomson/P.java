package tw.zhuran.madtomson;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.underscore.$;
import com.github.underscore.Block;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtom.domain.Pieces;

import java.util.HashMap;
import java.util.Map;

public class P {
    public static Texture PIECES_TEXTURE;
    public static Texture BACK_TEXTURE;
    public static Map<Piece, Sprite> PIECES = new HashMap<Piece, Sprite>();
    public static TextureRegion BACK_REGION;

    public static int TOP = 0;
    public static int LEFT = 0;
    public static int ROW_MARGIN = 0;
    public static int COL_MARGIN = 0;
    public static int PIECE_WIDTH = 100;
    public static int PIECE_HEIGHT = 125;

    public static void init() {
        PIECES_TEXTURE = new Texture("mahjong.png");
        BACK_TEXTURE = new Texture("back.jpg");
        BACK_REGION = makeBack();
        $.each(Pieces.ALL, new Block<Piece>() {
            @Override
            public void apply(Piece x) {
                PIECES.put(x, makePiece(x));
            }
        });
    }

    public static Sprite makeBack() {
        return new Sprite(BACK_TEXTURE, 325, 120, 320, 400);
    }

    public static TextureRegion makeBackRegion() {
        return new TextureRegion(BACK_TEXTURE, 325, 120, 320, 400);
    }

    public static Sprite makePiece(Piece piece) {
        int row = piece.getKind().ordinal();
        int col  = piece.getIndex() - 1;
        int x = LEFT + col * (PIECE_WIDTH + COL_MARGIN);
        int y = TOP + row * (PIECE_HEIGHT+ ROW_MARGIN);
        Sprite sprite = new Sprite(PIECES_TEXTURE, x, y, PIECE_WIDTH, PIECE_HEIGHT);
        sprite.setColor(1, 1, 1, 1);
        return sprite;
    }

    public static Sprite sprite(Piece piece) {
        return PIECES.get(piece);
    }
}
