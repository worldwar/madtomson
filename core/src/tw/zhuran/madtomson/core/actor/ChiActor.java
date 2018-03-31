package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.github.underscore.$;
import com.github.underscore.Block;
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

    public ChiActor(final Client client) {
        super(client, "Chi");
        chiOptionActors = new ArrayList<>();
    }

    public void initOptions(final Piece piece, final List<Group> groups) {
        this.piece = piece;
        this.groups = groups;
        clearOptions();
        chiOptionActors = new ArrayList<>(groups.size());
        if (groups.size() > 1) {
            int i = 0;
            for (Group group : groups) {
                ChiOptionActor chiOptionActor = new ChiOptionActor(this, Actions.chi(piece, group), i);
                chiOptionActor.setVisible(false);
                chiOptionActors.add(chiOptionActor);
                i++;
            }
            for (Actor actor : chiOptionActors) {
                getParent().addActor(actor);
            }
        }

        final ChiActor that = this;
        clearListeners();
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

    private void clearOptions() {
        $.each(chiOptionActors, new Block<Actor>() {
            @Override
            public void apply(Actor x) {
                ChiActor.this.getParent().removeActor(x);
            }
        });
        chiOptionActors.clear();
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
