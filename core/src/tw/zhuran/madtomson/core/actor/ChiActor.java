package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import tw.zhuran.madtom.domain.Actions;
import tw.zhuran.madtom.domain.Group;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtomson.core.Client;

import java.util.ArrayList;
import java.util.List;

public class ChiActor extends ActionActor {
    private Piece piece;
    private List<Group> groups;
    private List<Actor> chiOptionActors;

    public ChiActor(final Client client, final Piece piece, final List<Group> groups) {
        super(client, "Chi");
        setBounds(50, 200, 50, 20);
        this.piece = piece;
        this.groups = groups;
        initOptions();
        final ChiActor that = this;
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (groups.size() == 1) {
                    client.sendChi(piece, groups.get(0));
                    getParent().setVisible(false);
                } else {
                    that.showOptions();
                }
                return true;
            }
        });
    }

    private void initOptions() {
        chiOptionActors = new ArrayList<>(groups.size());
        if (groups.size() == 1) {
            return;
        }
        int i = 0;
        for (Group group : groups) {
            ChiOptionActor chiOptionActor = new ChiOptionActor(this, Actions.chi(piece, group), i);
            chiOptionActor.setVisible(false);
            chiOptionActor.getParent();
            chiOptionActors.add(chiOptionActor);
            i++;
        }
    }

    private void showOptions() {
        setOptionsVisible(true);
    }

    private void hideGroups() {
        setOptionsVisible(false);
    }

    private void setOptionsVisible(boolean visible) {
        for (Actor actor : chiOptionActors) {
            actor.setVisible(visible);
        }
    }

    @Override
    protected List<Actor> children() {
        return chiOptionActors;
    }

    public Client client() {
        return client;
    }

    public void hide() {
        hideGroups();
        getParent().setVisible(false);
    }
}
