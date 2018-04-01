package tw.zhuran.madtomson;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import tw.zhuran.madtomson.core.Client;

public class MadGame extends ApplicationAdapter {
	SpriteBatch batch;
	Client client;

	@Override
	public void create () {
		P.init();

		batch = new SpriteBatch();
		client = new Client();
		client.start();
		client.ready();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0.25f, 0.25f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		client.draw();
	}

	private void draw(SpriteBatch batch, Client client) {
		client.draw(batch);
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
