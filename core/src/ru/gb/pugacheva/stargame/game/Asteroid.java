package ru.gb.pugacheva.stargame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.gb.pugacheva.stargame.game.helpers.Poolable;
import ru.gb.pugacheva.stargame.screen.ScreenManager;
import ru.gb.pugacheva.stargame.screen.utils.Assets;

public class Asteroid implements Poolable {
    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private int hp;
    private int hpMax;
    private float angel; // угол показа изображения
    private float rotationSpeed; // скорость вращения астероида
    private float scale;
    private boolean active;
    private Circle hitArea;

    private final float BASE_SIZE = 256.0f;
    private final float BASE_RADIUS = BASE_SIZE / 2;

    public int getHpMax() {
        return hpMax;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getScale() {
        return scale;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public Asteroid(GameController gc) {
        this.gc = gc;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.hitArea = new Circle(0, 0, 0);
        this.active = false;
        this.texture = Assets.getInstance().getAtlas().findRegion("asteroid");
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 128, position.y - 128, 128, 128,
                256, 256, scale, scale, angel);

    }

    public void deactivate() {
        active = false;
    }

    public void activate(float x, float y, float vx, float vy, float scale) {
        this.position.set(x, y);
        this.velocity.set(vx, vy);
        this.hpMax = (int) ((5 +gc.getLevel()*2) * scale);
        this.hp = hpMax;
        this.angel = MathUtils.random(0.0f, 360.0f);
        this.rotationSpeed = MathUtils.random(-180.0f, 180.0f);
        this.hitArea.setPosition(position);
        this.scale = scale;
        this.active = true;
        this.hitArea.setRadius(BASE_RADIUS * scale * 0.9f); // заменить, чтобы изменялся от размера астероида
    }

    public boolean takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            deactivate();
            if (scale > 0.3f) {
                gc.getAsteroidController().setup(position.x, position.y, MathUtils.random(-200, 200),
                        MathUtils.random(-200, 200), scale - 0.2f);
                gc.getAsteroidController().setup(position.x, position.y, MathUtils.random(-200, 200),
                        MathUtils.random(-200, 200), scale - 0.2f);
                gc.getAsteroidController().setup(position.x, position.y, MathUtils.random(-200, 200),
                        MathUtils.random(-200, 200), scale - 0.2f);
            }
            return true;
        } else {
            return false;
        }
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        angel += rotationSpeed * dt;
        if (position.x < -hitArea.radius) {
            position.x = ScreenManager.SCREEN_WIDTH + hitArea.radius;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH + hitArea.radius) {
            position.x = -hitArea.radius;
        }
        if (position.y < -hitArea.radius) {
            position.y = ScreenManager.SCREEN_HEIGHT + hitArea.radius;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT + hitArea.radius) {
            position.y = -hitArea.radius;
        }
        hitArea.setPosition(position);
    }
}


