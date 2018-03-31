package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.scenes.scene2d.Group;
import tw.zhuran.madtom.domain.Hand;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtom.domain.TriggerType;
import tw.zhuran.madtom.event.InterceptEvent;
import tw.zhuran.madtom.event.InterceptType;
import tw.zhuran.madtomson.core.Client;

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
    private List<ActionActor> actors;

    public InterceptGroup(Client client) {
        setVisible(false);
        setX(50);
        setY(120);
        this.client = client;
        passActor = new PassActor(client);
        chiActor = new ChiActor(client);
        pengActor = new PengActor(client);

        addActor(pengActor);
        addActor(chiActor);
        addActor(passActor);
        actors = new ArrayList<>();
    }

    public void intercept(Piece piece, TriggerType triggerType, InterceptEvent event) {
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

    private void hideActors() {
        chiActor.setVisible(false);
        pengActor.setVisible(false);
        passActor.setVisible(false);
    }
}
