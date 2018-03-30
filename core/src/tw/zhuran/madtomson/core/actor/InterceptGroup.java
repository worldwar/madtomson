package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.scenes.scene2d.Group;
import tw.zhuran.madtom.domain.Hand;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtom.domain.TriggerType;
import tw.zhuran.madtom.event.InterceptEvent;
import tw.zhuran.madtom.event.InterceptType;
import tw.zhuran.madtomson.core.Client;

import java.util.List;

public class InterceptGroup extends Group {
    private Piece piece;
    private TriggerType triggerType;
    private Hand hand;
    private Client client;
    private PassActor passActor;

    public InterceptGroup(Client client) {
        setVisible(false);
        this.client = client;
        passActor = new PassActor(client);
    }

    public void intercept(Piece piece, TriggerType triggerType, InterceptEvent event) {
        clearChildren();
        List<InterceptType> intercepts = event.getIntercepts();
        for (InterceptType interceptType : intercepts) {
            if (interceptType == InterceptType.WIN) {
                addActor(new WinActor());
            } else if (interceptType == InterceptType.CHI) {
                List<tw.zhuran.madtom.domain.Group> groups = client.hand().chiableSequences(piece);
                if (groups.size() != 0) {

                    addActor(new ChiActor(client, piece, groups));
                }
            } else if (interceptType == InterceptType.GANG) {
                addActor(new GangActor(piece));
            }
        }
        addActor(passActor);
        setVisible(true);
    }
}
