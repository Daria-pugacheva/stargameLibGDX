package ru.gb.pugacheva.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.StringBuilder;
import ru.gb.pugacheva.stargame.game.Background;
import ru.gb.pugacheva.stargame.game.Hero;
import ru.gb.pugacheva.stargame.screen.utils.Assets;


public class GameOverScreen extends AbstractScreen {
    private Background background;
    private BitmapFont font72;
    private BitmapFont font48;
    private BitmapFont font24;
    private Hero defeatedHero;
    private StringBuilder stringBuilder;
    private Music music;

    public void setDefeatedHero(Hero defeatedHero) {
        this.defeatedHero = defeatedHero;
    }

    public GameOverScreen(SpriteBatch batch) {
        super(batch);
        this.stringBuilder = new StringBuilder();
    }

    @Override
    public void show() {
        this.background = new Background(null);
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        this.font48 = Assets.getInstance().getAssetManager().get("fonts/font48.ttf");
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");

        this.music = Assets.getInstance().getAssetManager().get("audio/gameover.mp3");
        this.music.setLooping(true);
        this.music.play();
    }

    public void update(float dt) {
        background.update(dt);
        if(Gdx.input.justTouched()){
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1);
        batch.begin();
        background.render(batch);
        font72.draw(batch, "GAME OVER", 0, 400, ScreenManager.SCREEN_WIDTH, Align.center, false);
        stringBuilder.clear();
        stringBuilder.append("Hero score: ").append(defeatedHero.getScore()).append("\n");
        stringBuilder.append("Money: ").append(defeatedHero.getMoney()).append("\n");
        font48.draw(batch,stringBuilder,0 , 200,ScreenManager.SCREEN_WIDTH, Align.center,false);
        font24.draw(batch,"Tap screen to return menu...",0 , 40,ScreenManager.SCREEN_WIDTH, Align.center,false);
        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
    }
}

