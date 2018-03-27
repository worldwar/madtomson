package tw.zhuran.madtomson;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import tw.zhuran.madtom.domain.Piece;
import tw.zhuran.madtom.domain.Pieces;
import tw.zhuran.madtomson.core.Client;

public class MadGame extends ApplicationAdapter {
	SpriteBatch batch;
	Client client;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.disableBlending();
		draw(batch, client);
		batch.end();
	}

	private void draw(SpriteBatch batch, Client client) {
		Piece piece = Pieces.ERTONG;
		batch.setColor(1, 1, 1, 1);
		P.sprite(piece).draw(batch);
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
