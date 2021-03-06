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
import com.badlogic.gdx.utils.ScreenUtils;
import ru.gb.pugacheva.stargame.game.Background;
import ru.gb.pugacheva.stargame.screen.utils.Assets;


public class MenuScreen extends AbstractScreen {
    private Background background;
    private BitmapFont font72;
    private BitmapFont font24;
    private Stage stage;
    private Music music;

    public MenuScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        this.background = new Background(null);
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");

        Gdx.input.setInputProcessor(stage); // мы говорим, что stage перехватывает все происходящие события ( input)

        Skin skin = new Skin(); //создаем скины
        skin.addRegions(Assets.getInstance().getAtlas()); // в скины добавляем атлас

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(); //создаем стиль кнопки, на которую можно тект писать
        textButtonStyle.up = skin.getDrawable("simpleButton"); // выбираем, как будет выглядеть кнопка (из атласа текстур)
        textButtonStyle.font = font24; // выбираем шрифт для кнопки
        skin.add("simpleSkin", textButtonStyle); //добавляем скин для этой кнопки под неким именем

        Button btnNewGame = new TextButton("New Game", textButtonStyle); // делаем одну кнопку с созданным стилем
        Button btnExitGame = new TextButton("Exit Game", textButtonStyle); //делавем вторую кнопку с созданным стилем
        btnNewGame.setPosition(320, 140); //располагаем кнопки примерно посредине
        btnExitGame.setPosition(320, 50);

        btnNewGame.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });

        btnExitGame.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        stage.addActor(btnNewGame);
        stage.addActor(btnExitGame);
        skin.dispose();

        this.music = Assets.getInstance().getAssetManager().get("audio/music.mp3");
        this.music.setLooping(true);
        this.music.play();
    }

    public void update(float dt) {
        background.update(dt);
        stage.act(dt);
    }

    @Override
    public void render(float delta) {
        update(delta);
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1);
        batch.begin();
        background.render(batch);
        font72.draw(batch, "Star Game 2021", 0, 300, ScreenManager.SCREEN_WIDTH, 1, false); //haling 1 - это выравнивание строки по центру
        batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
    }
}

