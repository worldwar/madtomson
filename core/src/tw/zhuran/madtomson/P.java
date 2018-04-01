package tw.zhuran.madtomson;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    public static Texture ACTIONS_TEXTURE;
    public static Map<Piece, Sprite> PIECES = new HashMap<Piece, Sprite>();
    public static Map<String, Sprite> ACTION_SPRITES = new HashMap<String, Sprite>();
    public static TextureRegion BACK_REGION;
    public static BitmapFont BITMAP_FONT;

    public static int TOP = 0;
    public static int LEFT = 0;
    public static int ROW_MARGIN = 0;
    public static int COL_MARGIN = 0;
    public static int PIECE_WIDTH = 100;
    public static int PIECE_HEIGHT = 140;
    public static int ACTION_WIDTH = 300;
    public static int ACTION_HEIGHT = 280;

    public static void init() {
        PIECES_TEXTURE = new Texture("mahjong.png");
        BACK_TEXTURE = new Texture("back.jpg");
        ACTIONS_TEXTURE = new Texture("actions.png");
        BITMAP_FONT = new BitmapFont();
        BACK_REGION = makeBack();
        $.each(Pieces.ALL, new Block<Piece>() {
            @Override
            public void apply(Piece x) {
                PIECES.put(x, makePiece(x));
            }
        });

        ACTION_SPRITES.put("chi", makeAction(0));
        ACTION_SPRITES.put("peng", makeAction(1));
        ACTION_SPRITES.put("gang", makeAction(2));
        ACTION_SPRITES.put("win", makeAction(3));
        ACTION_SPRITES.put("pass", makeAction(4));
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

    public static Sprite makeAction(int index) {
        return new Sprite(ACTIONS_TEXTURE, index * ACTION_WIDTH, 0, ACTION_WIDTH, ACTION_HEIGHT);
    }

    public static Sprite sprite(Piece piece) {
        return PIECES.get(piece);
    }


    public static Sprite action(String action) {
        return ACTION_SPRITES.get(action);
    }

    public static Sprite chi() {
        return ACTION_SPRITES.get("chi");
    }

    public static Sprite peng() {
        return ACTION_SPRITES.get("peng");
    }

    public static Sprite gang() {
        return ACTION_SPRITES.get("gang");
    }

    public static Sprite win() {
        return ACTION_SPRITES.get("win");
    }

    public static Sprite pass() {
        return ACTION_SPRITES.get("pass");
    }
}
