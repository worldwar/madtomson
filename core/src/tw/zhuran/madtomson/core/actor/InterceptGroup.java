package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.scenes.scene2d.Group;
import tw.zhuran.madtom.domain.Hand;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtom.domain.TriggerType;
import tw.zhuran.madtom.event.CommandEvent;
import tw.zhuran.madtom.event.InterceptEvent;
import tw.zhuran.madtom.event.InterceptType;
import tw.zhuran.madtomson.core.Client;
import tw.zhuran.madtomson.core.ClientState;

import java.util.ArrayList;
import java.util.List;

public class InterceptGroup extends Group {
    private Piece piece;
    private TriggerType triggerType;
    private Hand hand;
    private Client client;
    private PassActor passActor;
    private ChiActor chiActor;
    private PengActor pengActor;
    private GangActor gangActor;
    private List<ActionActor> actors;
    private boolean intercept;
    private boolean command;

    public InterceptGroup(Client client) {
        setVisible(false);
        setX(50);
        setY(120);
        this.client = client;
        passActor = new PassActor(client);
        chiActor = new ChiActor(client);
        pengActor = new PengActor(client);
        gangActor = new GangActor(client);

        addActor(gangActor);
        addActor(pengActor);
        addActor(chiActor);
        addActor(passActor);
        actors = new ArrayList<>();
    }

    @Override
    public void act(float delta) {
        if (client.state() != ClientState.INTERCEPT && intercept) {
            setVisible(false);
        }
        if (client.state() != ClientState.ACTIVE && command) {
            setVisible(false);
        }
    }

    public void intercept(Piece piece, TriggerType triggerType, InterceptEvent event) {
        intercept = true;
        command = false;
        hideActors();
        actors.clear();
        List<InterceptType> intercepts = event.getIntercepts();
        for (InterceptType interceptType : intercepts) {
            if (interceptType == InterceptType.WIN) {
            } else if (interceptType == InterceptType.CHI) {
                List<tw.zhuran.madtom.domain.Group> groups = client.hand().chiableSequences(piece);
                if (groups.size() != 0) {
                    chiActor.initOptions(piece, groups);
                    actors.add(chiActor);
                }
            } else if (interceptType == InterceptType.PENG) {
                actors.add(pengActor);
            } else if (interceptType == InterceptType.GANG) {
                gangActor.initIntercept();
                actors.add(gangActor);
            }
        }

        actors.add(passActor);

        for (int i = 0; i < actors.size(); i++) {
            ActionActor actor = actors.get(i);
            actor.setIndex(i);
            actor.setVisible(true);
        }
        setVisible(true);
    }

    public void command(CommandEvent event) {
        intercept = false;
        command = true;
        hideActors();
        actors.clear();
        List<InterceptType> intercepts = event.getIntercepts();
        for (InterceptType interceptType : intercepts) {
            if (interceptType == InterceptType.WIN) {
            } else if (interceptType == InterceptType.GANG) {

                List<Piece> pieces = client.genericGangablePieces();
                if (pieces.size() != 0) {
                    gangActor.initOptions(pieces);
                    actors.add(gangActor);
                }
            }
        }
        for (int i = 0; i < actors.size(); i++) {
            ActionActor actor = actors.get(i);
            actor.setIndex(i);
            actor.setVisible(true);
        }
        setVisible(true);
    }

    private void hideActors() {
        gangActor.setVisible(false);
        chiActor.setVisible(false);
        pengActor.setVisible(false);
        passActor.setVisible(false);
    }
}
