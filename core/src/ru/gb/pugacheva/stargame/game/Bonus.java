package ru.gb.pugacheva.stargame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.gb.pugacheva.stargame.game.helpers.Poolable;
import ru.gb.pugacheva.stargame.screen.ScreenManager;
import ru.gb.pugacheva.stargame.screen.utils.Assets;

public class Bonus implements Poolable {
    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private float scale;
    private boolean active;

    public TextureRegion getTexture() {
        return texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public Bonus(GameController gc) {
        this.gc = gc;
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.position = new Vector2(0.0f, 0.0f);
        this.scale = 0.5f;
        this.active = false;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 16, position.y - 16, 32, 32,
                64, 64, scale, scale, 0);

    }

    public void deactivate() {
        active = false;
    }

    public void activate() {
        this.position.set(MathUtils.random(32, ScreenManager.SCREEN_WIDTH - 32), ScreenManager.SCREEN_HEIGHT);
        this.active = true;
    }

    public void update(float dt) {
        position.y -= 120 * dt;
        if (position.y < -16) {
            deactivate();
        }
    }
}
