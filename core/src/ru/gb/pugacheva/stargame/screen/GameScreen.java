package ru.gb.pugacheva.stargame.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.gb.pugacheva.stargame.game.GameController;
import ru.gb.pugacheva.stargame.game.WorldRenderer;
import ru.gb.pugacheva.stargame.screen.utils.Assets;

public class GameScreen extends AbstractScreen {
    private GameController gameController;
    private WorldRenderer worldRenderer;

    public GameScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        Assets.getInstance().loadAssets(ScreenManager.ScreenType.GAME);
        this.gameController = new GameController(batch);
        this.worldRenderer = new WorldRenderer(gameController, batch);
    }

    @Override
    public void render(float delta) {
        gameController.update(delta);
        worldRenderer.render();
    }

    @Override
    public void dispose(){
        gameController.dispose();
    }
}
