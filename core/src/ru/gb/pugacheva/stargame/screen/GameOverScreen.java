package ru.gb.pugacheva.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.gb.pugacheva.stargame.screen.utils.Assets;


public class GameOverScreen extends AbstractScreen {
    private BitmapFont font72;
    private Stage stage;
    private BitmapFont font24;

    public GameOverScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");

        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);

        Button btnMenu = new TextButton("New Game", textButtonStyle);
        btnMenu.setPosition(320, 80);

        btnMenu.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });

        stage.addActor(btnMenu);
        skin.dispose();
    }

    public void update(float dt) {
        stage.act(dt);
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(1.0f, 0.0f, 0.0f, 1);
        batch.begin();
        font72.draw(batch, "GAME OVER", 0, 400, ScreenManager.SCREEN_WIDTH, 1, false);
        font24.draw(batch, "Your last result: \nSCORE: "
                        + ScreenManager.getInstance().getGameScreen().getGameController().getHero().getScore()
                        + "\nMONEY: " + ScreenManager.getInstance().getGameScreen().getGameController().getHero().getMoney(),
                0, 300, ScreenManager.SCREEN_WIDTH, 1, false);
        batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {

    }
}

