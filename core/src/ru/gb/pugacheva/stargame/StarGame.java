package ru.gb.pugacheva.stargame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.gb.pugacheva.stargame.game.Background;
import ru.gb.pugacheva.stargame.game.GameController;
import ru.gb.pugacheva.stargame.game.Hero;
import ru.gb.pugacheva.stargame.screen.GameScreen;

//public class StarGame extends ApplicationAdapter {
	public class StarGame extends Game {
	private SpriteBatch batch;
	private GameScreen gameScreen;
//	private Background background;
//	private Hero hero;
//	private Asteroid asteroid; // перетащить в GameController

//	public Hero getHero() {
//		return hero;
//	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		gameScreen = new GameScreen(batch);
		setScreen(gameScreen); //  в качестве экрана настроили наш экран
//		background = new Background(this);
//		hero = new Hero();
		//asteroid = new Asteroid();// убрать в GameController
	}

	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();
		getScreen().render(dt);
		//update(dt);
//		ScreenUtils.clear(0, 0.2f, 0.5f, 1);
//		batch.begin();
//		background.render(batch);
//		hero.render(batch);
//		asteroid.render(batch);
//		batch.end();
	}

//	public void update (float dt){
//		background.update(dt);
//		hero.update(dt);
//		asteroid.update(dt);
//	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
