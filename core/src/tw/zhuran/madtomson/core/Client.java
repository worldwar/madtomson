package tw.zhuran.madtomson.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.math.Affine2;
import com.google.common.collect.Lists;
import tw.zhuran.madtom.domain.*;
import tw.zhuran.madtom.event.*;
import tw.zhuran.madtom.server.EventPacket;
import tw.zhuran.madtom.server.Packets;
import tw.zhuran.madtom.server.packet.*;
import tw.zhuran.madtom.util.F;
import tw.zhuran.madtom.util.ReverseNaturalTurner;
import tw.zhuran.madtomson.P;
import tw.zhuran.madtomson.core.actor.*;

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
    private HandActor handActor;
    private SelfDiscardGroup selfDiscardGroup;
    private SelfDiscardGroup gangGroup;
    private InterceptGroup interceptGroup;
    private DumbTrunk leftTrunk;

    public Client() {
        clientState = ClientState.INIT;
        trunks = new HashMap<Integer, Trunk>();
        connector = new Connector();
        connector.setClient(this);
        handActor = new HandActor(this);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        selfDiscardGroup = new SelfDiscardGroup();
        gangGroup = new SelfDiscardGroup(4, 2);
        gangGroup.setX(150);
        gangGroup.setY(50);
        interceptGroup = new InterceptGroup(this);
        leftTrunk = new LeftDumbTrunk(stage);

        stage.addActor(interceptGroup);
        stage.addActor(handActor);
        stage.addActor(selfDiscardGroup);
        stage.addActor(gangGroup);
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
        stage.setDebugAll(true);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void tryDiscard(final PieceActor pieceActor) {
        if (clientState == ClientState.ACTIVE) {
            final Piece piece = pieceActor.getPiece();
            if (trunks.get(self).getHand().all().contains(piece)) {
                handActor.remove(piece);
                discard(piece);
            }
        }
    }

    public void discard(Piece piece) {
        List<Piece> pieces = info.getPieces();
        Trunk trunk = trunks.get(self);
        trunk.discard(piece);
        pieces.remove(piece);
        selfDiscardGroup.add(new DiscardPieceActor(piece));
        clientState = ClientState.FREE;
        Event action = Events.action(self, Actions.discard(piece));
        connector.send(Packets.event(action, self));
    }

    public void gang(Piece piece) {
        if (piece.equals(Pieces.HONGZHONG)) {
            hongzhongGang();
        } else if (piece.equals(wildcard)) {
            laiziGang();
        } else if (trunk().xugangable(piece)) {
            xugang(piece);
        } else if (trunk().angangable(piece)) {
            angang(piece);
        }
    }

    public List<Piece> genericGangablePieces() {
        List<Piece> pieces = Lists.newArrayList();
        if (trunk().hongzhongGangable()) {
            pieces.add(Pieces.HONGZHONG);
        }
        if (trunk().laiziGangable()) {
            pieces.add(wildcard);
        }
        pieces.addAll(trunk().xugangablePieces());
        pieces.addAll(trunk().getHand().angangablePieces());
        return pieces;
    }

    private void hongzhongGang() {
        Event action = Events.action(self, Actions.hongzhongGang());
        connector.send(Packets.event(action, self));
    }

    private void laiziGang() {
        Event action = Events.action(self, Actions.laiziGang(wildcard));
        connector.send(Packets.event(action, self));
    }

    private void xugang(Piece piece) {
        Event action = Events.action(self, Actions.xugang(piece));
        connector.send(Packets.event(action, self));
    }

    private void angang(Piece piece) {
        Event action = Events.action(self, Actions.angang(piece));
        connector.send(Packets.event(action, self));
    }

    public void sendChi(Piece piece, Group group) {
        clientState = ClientState.FREE;
        Event action = Events.action(self, Actions.chi(piece, group));
        connector.send(Packets.event(action, self));
    }

    public void sendPeng() {
        clientState = ClientState.FREE;
        Event action = Events.action(self, Actions.peng(piece));
        connector.send(Packets.event(action, self));
    }

    public void sendGang() {
        clientState = ClientState.FREE;
        Event action = Events.action(self, Actions.gang(piece));
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
        handActor.init();
        for (Action action : trunk().getActions()) {
            if (action.getType() == ActionType.DISCARD) {
                selfDiscardGroup.add(new DiscardPieceActor(action.getPiece()));
            }
        }
        Integer leftHandCount = info.getOtherHandCounts().get(left());
        List<Action> leftActions = info.getOtherActions().get(left());
        leftTrunk.init(leftHandCount, leftActions);
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
                    clientState = ClientState.INTERCEPT;
                    interceptGroup.intercept(piece, TriggerType.CAPTURE, content);
                    return;
                }

                if (packet instanceof CommandPacket) {
                    CommandPacket commandPacket = (CommandPacket) packet;
                    CommandEvent content = commandPacket.getContent();
                    interceptGroup.command(content);
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
        } else {
            if (event.getPlayer() == left()) {
                leftTrunk.feed();
            }
        }
    }

    private void handleAction(Event event) {
        Action action = event.getAction();
        piece = action.getPiece();

        if (event.getPlayer() == self) {
            handleSelfAction(event);
        } else {
            handleOtherAction(event);
        }
    }

    private void handleOtherAction(Event event) {
        if (state() == ClientState.INTERCEPT) {
            clientState = ClientState.FREE;
        }

        if (event.getPlayer() == left()) {
            leftTrunk.perform(event.getAction());
        }
    }

    private void handleSelfAction(Event event) {
        Trunk trunk = trunk();
        Action action = event.getAction();
        switch (action.getType()) {
            case CHI:
                handActor.performAction(action);
                trunk.chi(action.getPiece(), action.getGroup());
                clientState = ClientState.ACTIVE;
                break;
            case PENG:
                handActor.performAction(action);
                trunk.peng(action.getPiece());
                clientState = ClientState.ACTIVE;
                break;
            case GANG:
                handActor.performAction(action);
                trunk.gang(action.getPiece());
                clientState = ClientState.ACTIVE;
                break;
            case HONGZHONG_GANG:
                trunk.hongzhongGang();
                handActor.performAction(action);
                gangGroup.add(Pieces.HONGZHONG);
                break;
            case LAIZI_GANG:
                trunk.laiziGang();
                handActor.performAction(action);
                gangGroup.add(wildcard);
                break;
            case XUGANG:
                trunk.xugang(action.getPiece());
                handActor.performAction(action);
                break;
            case ANGANG:
                trunk.angang(action.getPiece());
                handActor.performAction(action);
                break;
        }
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
        trunk().feed(piece);
        handActor.feed(piece);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void pass() {
        clientState = ClientState.FREE;
        connector.send(Packets.event(Events.pass(self), 0));
    }

    public Trunk trunk() {
        return trunks.get(self);
    }

    public Hand hand() {
        return trunk().getHand();
    }

    public ClientState state() {
        return clientState;
    }
}
