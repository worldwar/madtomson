package tw.zhuran.madtomson.core.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtomson.core.Client;

import java.util.ArrayList;
import java.util.List;

public class GangActor extends ActionActor {
    private List<Piece> pieces = new ArrayList<>();
    private GangOptionGroup group;

    public GangActor(final Client client) {
        super(client, "Gang");
        group = new GangOptionGroup(this);
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                client.sendGang();
                getParent().setVisible(false);
                reset();
                return true;
            }
        });
    }

    public void initIntercept() {
        clearListeners();
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                client.sendGang();
                getParent().setVisible(false);
                reset();
                return true;
            }
        });
    }

    public void initOptions(final List<Piece> pieces) {
        getParent().addActor(group);
        group.setVisible(false);
        this.pieces = pieces;
        clearOptions();
        if (pieces.size() > 1) {
            for (Piece piece : pieces) {
                group.add(piece);
            }
        }

        final GangActor that = this;
        clearListeners();
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (pieces.size() == 1) {
                    client.genericGang(pieces.get(0));
                    getParent().setVisible(false);
                } else {
                    that.showOptions();
                }
                return true;
            }
        });
    }

    private void reset() {
        clearOptions();
        clearListeners();
    }

    private void clearOptions() {
        group.clearChildren();
    }

    private void showOptions() {
        setOptionsVisible(true);
    }

    private void hideOptions() {
        setOptionsVisible(false);
    }

    private void setOptionsVisible(boolean visible) {
        group.setVisible(visible);

        for (Actor actor : group.getChildren()) {
            actor.setVisible(true);
        }
    }

    public Client client() {
        return client;
    }

    public void hide() {
        hideOptions();
        getParent().setVisible(false);
    }
}
