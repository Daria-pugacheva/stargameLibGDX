package ru.gb.pugacheva.stargame.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ru.gb.pugacheva.stargame.StarGame;
import ru.gb.pugacheva.stargame.screen.utils.Assets;

public class ScreenManager {
    public enum ScreenType {
        GAME, MENU, GAMEOVER
    }

    public static final int SCREEN_WIDTH = Gdx.graphics.getWidth();//960
    public static final int HALF_SCREEN_WIDTH = SCREEN_WIDTH / 2;
    public static final int SCREEN_HEIGHT = Gdx.graphics.getHeight();//540
    public static final int HALF_SCREEN_HEIGHT = SCREEN_HEIGHT / 2;


    private StarGame game;
    private SpriteBatch batch;
    private LoadingScreen loadingScreen;
    private GameScreen gameScreen;
    private MenuScreen menuScreen;
    private GameOverScreen gameOverScreen;
    private Screen targetScreen;
    private Viewport viewport; // руководит отображением в случае изменения праметров размера экрана (радные варианты - растягивать, масштабировать и т.п.)

    private static ScreenManager ourInstance = new ScreenManager();

    public static ScreenManager getInstance() {
        return ourInstance;
    }

    public Viewport getViewport() {
        return viewport;
    }

    private ScreenManager() {
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public void init(StarGame game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
        this.viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT); // это тот вариант, когда при изменении размеров экрана будет сохраняться масштаб (лишние пиксели по краям будут черными полосами) - масштабируется, но сохраняет пропорции
        this.gameScreen = new GameScreen(batch);
        this.menuScreen = new MenuScreen(batch);
        this.loadingScreen = new LoadingScreen(batch);
        this.gameOverScreen = new GameOverScreen(batch);
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        viewport.apply();
    }

    public void changeScreen(ScreenType type) {
        Screen screen = game.getScreen();
        Assets.getInstance().clear();
        if (screen != null) {
            screen.dispose();
        }

        game.setScreen(loadingScreen);
        switch (type) {
            case GAME:
                targetScreen = gameScreen;
                Assets.getInstance().loadAssets(ScreenType.GAME);
                break;
            case MENU:
                targetScreen = menuScreen;
                Assets.getInstance().loadAssets(ScreenType.MENU);
                break;
            case GAMEOVER:
                targetScreen = gameOverScreen;
                Assets.getInstance().loadAssets(ScreenType.GAMEOVER);
                break;
        }
    }

    public void goToTarget() {
        game.setScreen(targetScreen);
    }


}
