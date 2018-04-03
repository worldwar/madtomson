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
    public static Texture LEFT_PIECES_TEXTURE;
    public static Texture RIGHT_PIECES_TEXTURE;
    public static Texture SHOW_SELF_PIECES_TEXTURE;
    public static Texture BACK_TEXTURE;
    public static Texture BACKS_TEXTURE;
    public static Texture ACTIONS_TEXTURE;
    public static Map<Piece, Sprite> PIECES = new HashMap<Piece, Sprite>();
    public static Map<Piece, Sprite> SHOW_SELF_PIECES = new HashMap<Piece, Sprite>();
    public static Map<Piece, Sprite> LEFT_DISCARD_PIECES = new HashMap<Piece, Sprite>();
    public static Map<Piece, Sprite> RIGHT_DISCARD_PIECES = new HashMap<Piece, Sprite>();
    public static Map<String, Sprite> ACTION_SPRITES = new HashMap<String, Sprite>();
    public static Map<String, Sprite> BACK_SPRITES = new HashMap<String, Sprite>();
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
    public static int TABLE_WIDTH = 1200;
    public static int TABLE_HEIGHT = 800;

    public static int BACK_STAND_HEIGHT = 56;
    public static int BACK_STAND_WIDTH = 42;

    public static int BACK_SLEEP_HEIGHT = 42;
    public static int BACK_SLEEP_WIDTH = 58;

    public static int BACK_BOTTOM_SLEEP_HEIGHT = 62;
    public static int BACK_BOTTOM_SLEEP_WIDTH = 44;

    public static int BACK_TOP_STAND_HEIGHT = 62;
    public static int BACK_TOP_STAND_WIDTH = 44;

    public static int SLEEP_DISCARD_WIDTH = 37;
    public static int SLEEP_DISCARD_HEIGHT = 51;

    public static void init() {
        PIECES_TEXTURE = makeTexture("mahjong.png");
        LEFT_PIECES_TEXTURE = makeTexture("left-pieces.png");
        RIGHT_PIECES_TEXTURE = makeTexture("right-pieces.png");
        BACK_TEXTURE = makeTexture("back.jpg");
        BACKS_TEXTURE = makeTexture("backs.png");
        ACTIONS_TEXTURE = makeTexture("actions.png");
        SHOW_SELF_PIECES_TEXTURE = makeTexture("show-self-pieces.png");
        BITMAP_FONT = new BitmapFont();
        BACK_REGION = makeBack();
        $.each(Pieces.ALL, new Block<Piece>() {
            @Override
            public void apply(Piece x) {
                PIECES.put(x, makePiece(x));
                SHOW_SELF_PIECES.put(x, makeShowSelfPiece(x));
                LEFT_DISCARD_PIECES.put(x, makeLeftPiece(x));
                RIGHT_DISCARD_PIECES.put(x, makeRightPiece(x));
            }
        });

        ACTION_SPRITES.put("chi", makeAction(0));
        ACTION_SPRITES.put("peng", makeAction(1));
        ACTION_SPRITES.put("gang", makeAction(2));
        ACTION_SPRITES.put("win", makeAction(3));
        ACTION_SPRITES.put("pass", makeAction(4));

        BACK_SPRITES.put("left-stand", new Sprite(BACKS_TEXTURE, BACK_STAND_WIDTH, 0, BACK_STAND_WIDTH, BACK_STAND_HEIGHT));
        BACK_SPRITES.put("right-stand", new Sprite(BACKS_TEXTURE, 0, 0, BACK_STAND_WIDTH, BACK_STAND_HEIGHT));
        BACK_SPRITES.put("left-sleep", new Sprite(BACKS_TEXTURE, BACK_STAND_WIDTH * 2, 0, BACK_SLEEP_WIDTH, BACK_SLEEP_HEIGHT));
        BACK_SPRITES.put("right-sleep", new Sprite(BACKS_TEXTURE, BACK_STAND_WIDTH * 2 + BACK_SLEEP_WIDTH, 0, BACK_SLEEP_WIDTH, BACK_SLEEP_HEIGHT));
        BACK_SPRITES.put("bottom-sleep", new Sprite(BACKS_TEXTURE, 0, BACK_STAND_HEIGHT, BACK_BOTTOM_SLEEP_WIDTH, BACK_BOTTOM_SLEEP_HEIGHT));
        BACK_SPRITES.put("top-stand", new Sprite(BACKS_TEXTURE, BACK_STAND_WIDTH, BACK_STAND_HEIGHT, BACK_BOTTOM_SLEEP_WIDTH, BACK_BOTTOM_SLEEP_HEIGHT));

        for (Sprite sprite : BACK_SPRITES.values()) {
            sprite.setOrigin(0, 0);
        }
    }

    public static Texture makeTexture(String filename) {
        Texture texture = new Texture(filename);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return texture;
    }

    public static Sprite makeBack() {
        return new Sprite(BACK_TEXTURE, 325, 120, 320, 400);
    }

    public static TextureRegion makeBackRegion() {
        return new TextureRegion(BACK_TEXTURE, 325, 120, 320, 400);
    }

    public static Sprite makePiece(Texture t, Piece piece, int width, int height) {
        int row = piece.getKind().ordinal();
        int col  = piece.getIndex() - 1;
        int x = col * width;
        int y = row * height;
        Sprite sprite = new Sprite(t, x, y, width, height);
        sprite.setOrigin(0, 0);
        return sprite;
    }


    public static Sprite makePiece(Piece piece) {
        return makePiece(PIECES_TEXTURE, piece, PIECE_WIDTH, PIECE_HEIGHT);
    }

    public static Sprite makeShowSelfPiece(Piece piece) {
        return makePiece(SHOW_SELF_PIECES_TEXTURE, piece, PIECE_WIDTH, PIECE_HEIGHT);
    }

    public static Sprite makeLeftPiece(Piece piece) {
        Sprite sprite = makePiece(LEFT_PIECES_TEXTURE, piece, SLEEP_DISCARD_WIDTH, SLEEP_DISCARD_HEIGHT);
        sprite.setRotation(-90);
        return sprite;
    }

    public static Sprite makeRightPiece(Piece piece) {
        Sprite sprite = makePiece(RIGHT_PIECES_TEXTURE, piece, SLEEP_DISCARD_WIDTH, SLEEP_DISCARD_HEIGHT);
        sprite.setRotation(90);
        return sprite;
    }

    public static Sprite makeAction(int index) {
        return new Sprite(ACTIONS_TEXTURE, index * ACTION_WIDTH, 0, ACTION_WIDTH, ACTION_HEIGHT);
    }

    public static Sprite sprite(Piece piece) {
        return PIECES.get(piece);
    }

    public static Sprite showSelfSprite(Piece piece) {
        return SHOW_SELF_PIECES.get(piece);
    }

    public static Sprite leftSprite(Piece piece) {
        return LEFT_DISCARD_PIECES.get(piece);
    }

    public static Sprite rightSprite(Piece piece) {
        return RIGHT_DISCARD_PIECES.get(piece);
    }

    public static Sprite action(String action) {
        return ACTION_SPRITES.get(action);
    }

    public static Sprite back(String name) {
        return BACK_SPRITES.get(name);
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
