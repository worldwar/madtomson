package tw.zhuran.madtomson.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import tw.zhuran.madtom.domain.*;
import tw.zhuran.madtom.event.Event;
import tw.zhuran.madtom.event.Events;
import tw.zhuran.madtom.event.Info;
import tw.zhuran.madtom.server.Packets;
import tw.zhuran.madtom.server.packet.InfoPacket;
import tw.zhuran.madtom.server.packet.MadPacket;
import tw.zhuran.madtom.util.F;
import tw.zhuran.madtomson.P;
import tw.zhuran.madtomson.core.actor.PieceActor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client {
    private int self;
    private Piece piece;
    private Piece wildcard;
    private int turn;
    private int dealer;
    private Map<Integer, Trunk> trunks;
    private ClientState clientState;
    private Info info;
    private Connector connector;
    private Stage stage;
    private List<PieceActor> handActors;

    public Client() {
        clientState = ClientState.INIT;
        trunks = new HashMap<Integer, Trunk>();
        connector = new Connector();
        connector.setClient(this);
        handActors = new ArrayList<>();
    }

    public void start() {
        try {
            connector.connect("localhost", 32211);
        } catch (InterruptedException e) {
        }
    }

    public void ready() {
        connector.send(Packets.ready());
    }

    public void draw(SpriteBatch batch) {
        batch.disableBlending();
        drawHand(batch);
        batch.enableBlending();
    }

    public void draw() {
        stage.draw();
    }

    public void tryDiscard(PieceActor pieceActor) {
        if (clientState == ClientState.ACTIVE) {
            Piece piece = pieceActor.getPiece();
            if (info.getPieces().contains(piece)) {
                discard(piece);
                pieceActor.remove();
            }
        }
    }

    public void discard(Piece piece) {
        List<Piece> pieces = info.getPieces();
        pieces.remove(piece);
        Event action = Events.action(self, Actions.discard(piece));
        connector.send(Packets.event(action, self));
    }

    private void drawHand(SpriteBatch batch) {
        if (info != null) {
            List<Piece> pieces = info.getPieces();
            int index = 0;
            for (Piece piece : pieces) {
                drawHandPiece(batch, piece, index);
                index++;
            }
        }
    }

    private void drawHandPiece(SpriteBatch batch, Piece piece, int index) {
        Sprite sprite = P.sprite(piece);
        float scale = 0.7f;
        sprite.setPosition(sprite.getWidth() * index * scale, 0);
        sprite.setColor(Color.WHITE);
        sprite.setScale(scale);
        sprite.draw(batch);
    }

    public void init(Info info) {
        dealer = info.getDealer();
        turn = info.getTurn();
        self = info.getSelf();
        wildcard = info.getWildcard();
        Trunk trunk = makeTrunk(info.getPieces(), info.getActions());
        trunks.put(self, trunk);

        Map<Integer, Integer> otherHandCounts = info.getOtherHandCounts();
        Map<Integer, List<Action>> otherActions = info.getOtherActions();
        for (Map.Entry<Integer, Integer> otherHandCount : otherHandCounts.entrySet()) {
            int handCount = otherHandCount.getValue();
            int player = otherHandCount.getKey();
            List<Action> actions = otherActions.get(player);
            Trunk otherTrunk = makeTrunk(player, handCount, actions);
            trunks.put(player, otherTrunk);
        }

        if (turn == self) {
            this.clientState = ClientState.ACTIVE;
        } else {
            this.clientState = ClientState.FREE;
        }
        this.info = info;

        initActors();
    }

    private void initActors() {
        int i = 0;
        for (Piece piece : info.getPieces()) {
            PieceActor pieceActor = new PieceActor(this, piece, i);
            handActors.add(pieceActor);
            stage.addActor(pieceActor);
            i++;
        }
    }

    private Trunk makeTrunk(int player, int handCount, List<Action> actions) {
        Trunk trunk = new Trunk(player);
        trunk.init(F.repeat(Pieces.HONGZHONG, handCount));
        trunk.setWildcard(wildcard);
        trunk.setActions(actions);
        return trunk;
    }

    private Trunk makeTrunk(List<Piece> pieces, List<Action> actions) {
        Trunk trunk = new Trunk(self);
        trunk.init(pieces);
        trunk.setWildcard(wildcard);
        trunk.setActions(actions);
        return trunk;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public void handle(MadPacket packet) {
        switch (packet.getType()) {
            case INFO:
                InfoPacket infoPacket = (InfoPacket) packet;
                Info info = infoPacket.getContent();
                init(info);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
