package ru.gb.pugacheva.stargame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.gb.pugacheva.stargame.game.helpers.Poolable;
import ru.gb.pugacheva.stargame.screen.ScreenManager;
import ru.gb.pugacheva.stargame.screen.utils.Assets;

public class Bot implements Poolable {
    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private int hp;
    private int hpMax;
    private float scale;
    private boolean active;
    private Circle hitArea;

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

    public Bot(GameController gc) {
        this.gc = gc;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.hitArea = new Circle(0, 0, 0);
        this.active = false;
        this.texture = Assets.getInstance().getAtlas().findRegion("nlo");
        this.scale = 1.0f;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x -60, position.y - 39, 60, 39,
                121, 78, scale, scale, 0);
    }

    public void deactivate() {
        active = false;
    }

    public void activate() {
        this.position.set(0, ScreenManager.HALF_SCREEN_HEIGHT); //вылетает слева с середины
        this.velocity.set(20,20);
        this.hpMax = 40;
        this.hp = hpMax;
        this.hitArea.setPosition(position);
        this.active = true;
        this.hitArea.setRadius(120.0f);
    }

    public boolean takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            deactivate();
            return true;
        } else {
            return false;
        }
    }

    public void update(float dt,Hero hero) {
        position.mulAdd(velocity, dt*4);
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


