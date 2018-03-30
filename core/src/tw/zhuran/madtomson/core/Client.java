package tw.zhuran.madtomson.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.underscore.$;
import com.github.underscore.Predicate;
import com.badlogic.gdx.math.Affine2;
import tw.zhuran.madtom.domain.*;
import tw.zhuran.madtom.event.*;
import tw.zhuran.madtom.server.EventPacket;
import tw.zhuran.madtom.server.Packets;
import tw.zhuran.madtom.server.packet.*;
import tw.zhuran.madtom.util.F;
import tw.zhuran.madtom.util.ReverseNaturalTurner;
import tw.zhuran.madtomson.P;
import tw.zhuran.madtomson.core.actor.InterceptGroup;
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
    private Piece feedPiece;
    private PieceActor feedPieceActor;
    private InterceptGroup interceptGroup;

    public Client() {
        clientState = ClientState.INIT;
        trunks = new HashMap<Integer, Trunk>();
        connector = new Connector();
        connector.setClient(this);
        handActors = new ArrayList<>();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        interceptGroup = new InterceptGroup(this);
        stage.addActor(interceptGroup);
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
        drawLeft(batch);
        if (info != null) {
            drawHand(batch);
            drawLeft(batch);
        }
        batch.enableBlending();
    }

    public void draw() {
        stage.draw();
    }

    public void tryDiscard(final PieceActor pieceActor) {
        if (clientState == ClientState.ACTIVE) {
            final Piece piece = pieceActor.getPiece();
            if (trunks.get(self).getHand().all().contains(piece)) {
                discard(piece);
                int index = $.findIndex(handActors, new Predicate<PieceActor>() {
                    @Override
                    public Boolean apply(PieceActor arg) {
                        return pieceActor.getIndex() == arg.getIndex();
                    }
                });
                handActors.remove(index);
                feedPiece = null;
                feedPieceActor = null;
                pieceActor.remove();
                arrange();
            }
        }
    }

    public void discard(Piece piece) {
        List<Piece> pieces = info.getPieces();
        Trunk trunk = trunks.get(self);
        trunk.discard(piece);
        pieces.remove(piece);
        Event action = Events.action(self, Actions.discard(piece));
        connector.send(Packets.event(action, self));
    }

    private void drawLeft(SpriteBatch batch) {
        for (int i = 0; i < 14; i++) {
            drawLeftHand(batch, i);
        }
    }

    private void drawLeftHand(SpriteBatch batch, int i) {
        Affine2 transform = new Affine2();
        transform.translate(100 + i * 350 * 0.015f, i * 250 * 0.12f + 200);
        transform.shear(-0.3f, 5.0f);
        transform.scale(0.015f, 0.1f);
        batch.draw(P.BACK_REGION, 325, 400, transform);
    }

    private int left() {
        ReverseNaturalTurner turner = new ReverseNaturalTurner(4);
        turner.turnTo(self);
        return turner.next();
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
        trunk.setWildcard(wildcard);
        trunk.setActions(actions);
        for (Piece piece : pieces) {
            trunk.feed(piece);
        }
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
                break;
            case EVENT:
                if (packet instanceof DispatchEventPacket) {
                    DispatchEventPacket dispatchEventPacket = (DispatchEventPacket) packet;
                    DispatchEvent content = dispatchEventPacket.getContent();
                    handDispatch(content);
                    return;
                }

                if (packet instanceof GangAffordEventPacket) {
                    GangAffordEventPacket gangAffordEventPacket = (GangAffordEventPacket) packet;
                    GangAffordEvent content = gangAffordEventPacket.getContent();
                    handGangAfford(content);
                    return;
                }

                if (packet instanceof InterceptPacket) {
                    InterceptPacket interceptPacket = (InterceptPacket) packet;
                    InterceptEvent content = interceptPacket.getContent();
                    interceptGroup.intercept(piece, TriggerType.CAPTURE, content);
                    return;
                }

                EventPacket eventPacket = (EventPacket) packet;
                Event content = eventPacket.getContent();
                handleEvent(content);
                EventType eventType = content.getEventType();
                if (eventType == EventType.ACTION) {
                    handleAction(content);
                }
        }
    }

    private void handGangAfford(GangAffordEvent event) {
        if (event.getPlayer() == self) {
            feed(event.getPiece());
        }
    }

    private void handDispatch(DispatchEvent event) {
        if (event.getPlayer() == self) {
            clientState = ClientState.ACTIVE;
            feed(event.getPiece());
        }
    }

    private void handleAction(Event event) {
        Action action = event.getAction();
        piece = action.getPiece();
    }

    private void handleEvent(Event event) {
        EventType eventType = event.getEventType();
        switch (eventType) {
            case DISPATCH:
                DispatchEvent dispatchEvent = (DispatchEvent) event;
                if (self == event.getPlayer()) {
                }
        }
    }

    private void feed(Piece piece) {
        this.feedPiece = piece;
        feedPieceActor = new PieceActor(this, piece, 14);
        stage.addActor(feedPieceActor);

        int i = insertLocation(feedPiece);
        handActors.add(i, feedPieceActor);
    }

    public void insertIntoHand() {
        if (feedPiece != null) {
            int i = insertLocation(feedPiece);
            handActors.add(i, feedPieceActor);
            feedPiece = null;
            feedPieceActor = null;
        }
        arrange();
    }

    private void arrange() {
        for (int i = 0; i < handActors.size(); i++) {
            handActors.get(i).setIndex(i);
        }
    }

    private int insertLocation(final Piece piece) {
        Trunk trunk = trunks.get(self);
        trunk.feed(piece);
        Hand hand = trunk.getHand();
        List<Piece> pieces = hand.all();
        int index = $.findIndex(pieces, new Predicate<Piece>() {
            @Override
            public Boolean apply(Piece p) {
                return p.equals(piece);
            }
        });
        info.getPieces().add(index, piece);
        return index;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void pass() {
        connector.send(Packets.event(Events.pass(self), 0));
    }

    public Trunk trunk() {
        return trunks.get(self);
    }

    public Hand hand() {
        return trunk().getHand();
    }
}
