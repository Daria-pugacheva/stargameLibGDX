package ru.gb.pugacheva.stargame.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.gb.pugacheva.stargame.game.helpers.Poolable;
import ru.gb.pugacheva.stargame.screen.ScreenManager;

public class Asteroid implements Poolable {

    private Vector2 position;
    private float angel;
    private boolean active;


    public Asteroid() {
        this.position = new Vector2(-128, ScreenManager.SCREEN_HEIGHT / 2f); //особо не влияет стартовая позиция, при обновлениях меняется.
        this.angel = 0.0f;
        this.active = true;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getAngel() {
        return angel;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void update(float dt) {
        position.x += MathUtils.cosDeg(angel) * 120.0f * dt;
        position.y += MathUtils.sinDeg(angel) * 120.0f * dt;
        if (position.x > ScreenManager.SCREEN_WIDTH + 128
                || position.y > ScreenManager.SCREEN_HEIGHT + 128
                || position.y < ScreenManager.SCREEN_HEIGHT + -128) {
            position.x = -128;
            position.y = MathUtils.random(128, ScreenManager.SCREEN_HEIGHT - 128);
            angel = MathUtils.random(-20, 20);
        }
    }


}

