package tw.zhuran.madtomson.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.collect.Lists;
import tw.zhuran.madtom.domain.*;
import tw.zhuran.madtom.event.*;
import tw.zhuran.madtom.server.EventPacket;
import tw.zhuran.madtom.server.Packets;
import tw.zhuran.madtom.server.packet.*;
import tw.zhuran.madtom.util.F;
import tw.zhuran.madtom.util.ReverseNaturalTurner;
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
    private ClientState clientState;
    private Connector connector;
    private Stage stage;
    private InterceptGroup interceptGroup;
    private LeftDumbTrunk leftTrunk;
    private SelfDumbTrunk selfTrunk;
    private Trunk trunk;
    private Map<Integer, DumbTrunk> dumbTrunks;

    public Client() {
        clientState = ClientState.INIT;
        dumbTrunks = new HashMap<>();
        connector = new Connector();
        connector.setClient(this);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        leftTrunk = new LeftDumbTrunk(this, stage);
        selfTrunk = new SelfDumbTrunk(this, stage);
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

    public void draw() {
        stage.setDebugAll(true);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void genericGang(Piece piece) {
        if (piece.equals(Pieces.HONGZHONG)) {
            hongzhongGang();
        } else if (piece.equals(wildcard)) {
            laiziGang();
        } else if (trunk.xugangable(piece)) {
            xugang(piece);
        } else if (trunk.angangable(piece)) {
            angang(piece);
        }
    }

    public List<Piece> genericGangablePieces() {
        List<Piece> pieces = Lists.newArrayList();
        if (trunk.hongzhongGangable()) {
            pieces.add(Pieces.HONGZHONG);
        }
        if (trunk.laiziGangable()) {
            pieces.add(wildcard);
        }
        pieces.addAll(trunk.xugangablePieces());
        pieces.addAll(trunk.getHand().angangablePieces());
        return pieces;
    }

    public void hongzhongGang() {
        Event action = Events.action(self, Actions.hongzhongGang());
        connector.send(Packets.event(action, self));
    }

    public void laiziGang() {
        Event action = Events.action(self, Actions.laiziGang(wildcard));
        connector.send(Packets.event(action, self));
    }

    public void xugang(Piece piece) {
        Event action = Events.action(self, Actions.xugang(piece));
        connector.send(Packets.event(action, self));
    }

    public void angang(Piece piece) {
        Event action = Events.action(self, Actions.angang(piece));
        connector.send(Packets.event(action, self));
    }

    public void sendDiscard(Piece piece) {
        clientState = ClientState.FREE;
        Event action = Events.action(self, Actions.discard(piece));
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

    private int left() {
        ReverseNaturalTurner turner = new ReverseNaturalTurner(4);
        turner.turnTo(self);
        return turner.next();
    }

    public void init(Info info) {
        dealer = info.getDealer();
        turn = info.getTurn();
        self = info.getSelf();
        wildcard = info.getWildcard();

        selfTrunk.setPlayer(self);
        leftTrunk.setPlayer(left());
        trunk = makeTrunk(info.getPieces(), info.getActions());
        dumbTrunks.put(self, selfTrunk);
        dumbTrunks.put(left(), leftTrunk);

        if (turn == self) {
            this.clientState = ClientState.ACTIVE;
        } else {
            this.clientState = ClientState.FREE;
        }
        initActors(info);
    }

    private void initActors(Info info) {
        for (DumbTrunk dumbTrunk : dumbTrunks.values()) {
            if (dumbTrunk.getPlayer() == self) {
                dumbTrunk.init(info.getPieces(), info.getActions(), wildcard);
            } else {
                Integer handCount = info.getOtherHandCounts().get(dumbTrunk.getPlayer());
                List<Action> actions = info.getOtherActions().get(dumbTrunk.getPlayer());
                dumbTrunk.init(F.repeat(Pieces.HONGZHONG, handCount), actions, wildcard);
            }
        }
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
                    handleDispatch(content);
                    return;
                }

                if (packet instanceof GangAffordEventPacket) {
                    GangAffordEventPacket gangAffordEventPacket = (GangAffordEventPacket) packet;
                    GangAffordEvent content = gangAffordEventPacket.getContent();
                    handleGangAfford(content);
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

    private DumbTrunk dumbTrunk(int player) {
        return dumbTrunks.get(player);

    }

    private void handleGangAfford(GangAffordEvent event) {
        feed(event.getPlayer(), event.getPiece());
    }

    private void handleDispatch(DispatchEvent event) {
        feed(event.getPlayer(), event.getPiece());
    }

    private void feed(int player, Piece piece) {
        DumbTrunk dumbTrunk = dumbTrunk(player);
        if (dumbTrunk != null) {
            dumbTrunk.feed(piece);
        }

        if (player == self) {
            clientState = ClientState.ACTIVE;
            trunk.feed(piece);
        }
    }

    private void handleAction(Event event) {
        Action action = event.getAction();
        piece = action.getPiece();

        int player = event.getPlayer();
        DumbTrunk dumbTrunk = dumbTrunk(player);
        if (dumbTrunk != null) {
            dumbTrunk.perform(event.getAction());
        }
        if (player == self) {
            clientState = ClientState.ACTIVE;
            switch (action.getType()) {
                case DISCARD:
                    trunk.discard(piece);
                    return;
                case CHI:
                    trunk.chi(piece, action.getGroup());
                    return;
                case PENG:
                    trunk.peng(piece);
                    return;
                case GANG:
                    trunk.gang(piece);
                    return;
                case XUGANG:
                    trunk.xugang(piece);
                    return;
                case ANGANG:
                    trunk.angang(piece);
                    return;
                case HONGZHONG_GANG:
                    trunk.hongzhongGang();
                    return;
                case LAIZI_GANG:
                    trunk.laiziGang();
                    return;
            }
        } else {
            clientState = ClientState.FREE;
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

    public void pass() {
        clientState = ClientState.FREE;
        connector.send(Packets.event(Events.pass(self), 0));
    }

    public ClientState state() {
        return clientState;
    }

    public List<Group> chiableSequences(Piece piece) {
        return trunk.getHand().chiableSequences(piece);
    }
}
