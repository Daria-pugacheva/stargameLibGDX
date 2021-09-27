package ru.gb.pugacheva.stargame.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.StringBuilder;
import ru.gb.pugacheva.stargame.screen.ScreenManager;
import ru.gb.pugacheva.stargame.screen.utils.Assets;

public class WorldRenderer {
    private GameController gc;
    private SpriteBatch batch;
    private BitmapFont font32;
    private StringBuilder scoreStringBuilder;
    private StringBuilder hpStringBuilder;

    public WorldRenderer(GameController gc, SpriteBatch batch) {
        this.gc = gc;
        this.batch = batch;
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf", BitmapFont.class);
        this.scoreStringBuilder = new StringBuilder();
        this.hpStringBuilder = new StringBuilder();
    }

    public void render() {
        ScreenUtils.clear(0, 0.2f, 0.5f, 1);
        batch.begin();
        gc.getBackground().render(batch);
        gc.getAsteroidController().render(batch);
        gc.getHero().render(batch);
        gc.getBulletController().render(batch);
        scoreStringBuilder.clear();
        hpStringBuilder.clear();
        scoreStringBuilder.append("SCORE: ").append(gc.getHero().getScoreView());
        font32.draw(batch, scoreStringBuilder, 20, ScreenManager.SCREEN_HEIGHT - 20);
        hpStringBuilder.append("HP: ").append(gc.getHero().getHp()); //не делала плавно, т.к. урон разный от разных астероидов (так нагляднее)
        font32.draw(batch, hpStringBuilder, 20, ScreenManager.SCREEN_HEIGHT - 60);

        batch.end();
    }
}
