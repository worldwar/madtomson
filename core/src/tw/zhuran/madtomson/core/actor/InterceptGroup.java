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
    private List<ActionActor> actors;

    public InterceptGroup(Client client) {
        setVisible(false);
        this.client = client;
        passActor = new PassActor(client);
        actors = new ArrayList<>();
        setX(50);
        setY(100);
    }

    public void intercept(Piece piece, TriggerType triggerType, InterceptEvent event) {
        clearChildren();
        actors.clear();
        List<InterceptType> intercepts = event.getIntercepts();
        for (InterceptType interceptType : intercepts) {
            if (interceptType == InterceptType.WIN) {
            } else if (interceptType == InterceptType.CHI) {
                List<tw.zhuran.madtom.domain.Group> groups = client.hand().chiableSequences(piece);
                if (groups.size() != 0) {
                    actors.add(new ChiActor(client, piece, groups));
                }
            } else if (interceptType == InterceptType.PENG) {
                actors.add(new PengActor(client, piece));
            } else if (interceptType == InterceptType.GANG) {
            }
        }

        actors.add(passActor);

        for (int i = 0; i < actors.size(); i++) {
            ActionActor actor = actors.get(i);
            addActor(actor);
            actor.setIndex(i);
        }
        setVisible(true);
    }
}
