package ru.gb.pugacheva.stargame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.StringBuilder;
import ru.gb.pugacheva.stargame.screen.ScreenManager;
import ru.gb.pugacheva.stargame.screen.utils.Assets;

public class Hero {
    public enum Skill {
        HP_MAX(20), HP(20), WEAPON(100), MAGNET (50);

        int cost;

        Skill(int cost) {
            this.cost = cost;
        }
    }

    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private float angel;
    private float enginePower;
    private float fireTimer;
    private int score;
    private int scoreView;
    private int hp;
    private int hpMax;
    private int money;
    private StringBuilder stringBuilder;
    private Circle hitarea;
    private Circle magneticField;
    private Weapon currentWeapon;
    private Shop shop;
    private Weapon[] weapons;
    private int weaponNum;


    public Circle getMagneticField() {
        return magneticField;
    }

    public Shop getShop() {
        return shop;
    }

    public Circle getHitarea() {
        return hitarea;
    }

    public float getAngel() {
        return angel;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int amount) {
        this.score += amount;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getMoney() {
        return money;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public boolean isMoneyEnough(int amount) {
        return money >= amount;
    }

    public void decreaseMoney(int amount) {
        money -= amount;
    }

    public void setPause(boolean pause){
        gc.setPause(pause);
    }

    public Hero(GameController gc) {
        this.gc = gc;
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.position = new Vector2(480, 270);
        this.velocity = new Vector2(0, 0); // вектор скорости изначально нулевой - не двигаемся
        this.angel = 0.0f;
        this.enginePower = 500.0f;
        this.hpMax = 1000;
        this.hp = hpMax;
        this.money = 1000;
        this.shop = new Shop(this);
        this.stringBuilder = new StringBuilder();
        this.hitarea = new Circle(position, 26); // радиус чуть меньше, чем у героя, чтобы урон был только про попадании по видимой части корабля, а не по пустым пикселям
        this.magneticField = new Circle(position,100);
        createWeapons();
        this.weaponNum = 4;
        this.currentWeapon = weapons [weaponNum];
    }


    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, 1, 1,
                angel);

    }

    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        stringBuilder.clear();
        stringBuilder.append("SCORE: ").append(scoreView).append("\n");
        stringBuilder.append("HP: ").append(hp).append(" / ").append(hpMax).append("\n");
        stringBuilder.append("MONEY: ").append(money).append("\n");
        stringBuilder.append("BULLETS: ").append(currentWeapon.getCurrentBulletQuantity()).append("/")
                .append(currentWeapon.getMaxBulletQuantity()).append("\n");
        stringBuilder.append("MAGNETIC: ").append((int)magneticField.radius).append("\n");
        font.draw(batch, stringBuilder, 20, ScreenManager.SCREEN_HEIGHT - 20);
    }

    public void takeDamage(int amount) {
        hp -= amount;
    }

    public void consume(PowerUp p) {
        switch (p.getType()) {
            case MEDKIT:
                hp += p.getPower();
                if(hp > hpMax){
                    hp = hpMax;
                }
                break;
            case MONEY:
                money += p.getPower();
                break;
            case AMMOS:
                currentWeapon.addAmmos(p.getPower());
                break;
        }
    }

    public boolean upgrade(Skill skill) {
        switch (skill) {
            case HP_MAX:
                hpMax += 10;
                return true;
            case HP:
                if(hp < hpMax){
                    hp += 10;
                    if(hp > hpMax){
                        hp = hpMax;
                    }
                    return true;
                }
            case WEAPON:
                if(weaponNum <weapons.length -1){
                    weaponNum ++;
                    currentWeapon = weapons[weaponNum];
                    return true;
                }
            case MAGNET:
                magneticField.radius += 10;
                return true;
        }
        return false;
    }

    public void update(float dt) {
        fireTimer += dt;
        updateScore(dt);

        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            tryToFire();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angel += 180.0f * dt;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angel -= 180.0f * dt;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.x += MathUtils.cosDeg(angel) * enginePower * dt;
            velocity.y += MathUtils.sinDeg(angel) * enginePower * dt;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.x += MathUtils.cosDeg(angel - 180) * enginePower / 2 * dt;
            velocity.y += MathUtils.sinDeg(angel - 180) * enginePower / 2 * dt;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            setPause(true);
            shop.setVisible(true);
        }

        position.mulAdd(velocity, dt); // Добавляя вектор скорости к позиции, получаем движение
        hitarea.setPosition(position); // с изменением позици меняется и позиция зоны урона
        magneticField.setPosition(position);
        float stopKoef = 1.0f - 1.0f * dt;
        if (stopKoef < 0) {
            stopKoef = 0;
        }
        velocity.scl(stopKoef);
        if (velocity.len() > 50.0f) {
            float bx = position.x + MathUtils.cosDeg(angel + 180) * 20;
            float by = position.y + MathUtils.sinDeg(angel + 180) * 20;
            for (int i = 0; i < 2; i++) {
                gc.getParticleController().setup(
                        bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * -0.3f + MathUtils.random(-20, 20), velocity.y * -0.3f + MathUtils.random(-20, 20),
                        0.5f,
                        1.2f, 0.2f,
                        1.0f, 0.3f, 0.0f, 1.0f,
                        1.0f, 1.0f, 1.0f, 1.0f
                );
            }
        }
        checkSpaceBorders();
    }

    private void updateScore(float dt) {
        if (scoreView < score) {
            scoreView += 1000 * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }
    }

    private void checkSpaceBorders() {
        if (position.x < 32f) {
            position.x = 32f;
            velocity.x *= -1; // отскок в обратную сторону
        }
        if (position.x > ScreenManager.SCREEN_WIDTH - 32f) {
            position.x = ScreenManager.SCREEN_WIDTH - 32f;
            velocity.x *= -1;
        }
        if (position.y < 32f) {
            position.y = 32f;
            velocity.y *= -1;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT - 32f) {
            position.y = ScreenManager.SCREEN_HEIGHT - 32f;
            velocity.y *= -1;
        }
    }

    private void tryToFire() {
        if (fireTimer > 0.2) {
            fireTimer = 0.0f;
            currentWeapon.fire();
        }
    }

    private void createWeapons() {
        weapons = new Weapon[]{
                new Weapon(
                        gc, this, "Laser", 0.2f, 1, 600, 300,
                        new Vector3[]{
                                new Vector3(28, 90, 0),
                                new Vector3(28, -90, 0)
                        }),
                new Weapon(
                        gc, this, "Laser", 0.2f, 1, 600, 300,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 20),
                                new Vector3(28, -90, -20)
                        }),
                new Weapon(
                        gc, this, "Laser", 0.2f, 1, 600, 500,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 10),
                                new Vector3(28, 90, 20),
                                new Vector3(28, -90, -10),
                                new Vector3(28, -90, -20)
                        }),
                new Weapon(
                        gc, this, "Laser", 0.2f, 1, 600, 300,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 20),
                                new Vector3(28, -90, -20)
                        }),
                new Weapon(
                        gc, this, "Laser", 0.1f, 2, 600, 1000,
                        new Vector3[]{
                                new Vector3(28, 0, 0),
                                new Vector3(28, 90, 20),
                                new Vector3(28, -90, -20)
                        })
        };
    }

}



