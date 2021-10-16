package ru.gb.pugacheva.stargame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
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

public class Hero extends Ship {
    public enum Skill {
        HP_MAX(20), HP(20), WEAPON(100), MAGNET(50);

        int cost;

        Skill(int cost) {
            this.cost = cost;
        }
    }

    private int score;
    private int scoreView;
    private int money;
    private StringBuilder stringBuilder;
    private Shop shop;
    private int critical;

    public int getCritical() {
        return critical;
    }

    public Shop getShop() {
        return shop;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int amount) {
        this.score += amount;
    }

    public int getMoney() {
        return money;
    }

    public boolean isMoneyEnough(int amount) {
        return money >= amount;
    }

    public void decreaseMoney(int amount) {
        money -= amount;
    }

    public void setPause(boolean pause) {
        gc.setPause(pause);
    }

    public Hero(GameController gc) {
        super(gc, 100, 480, 270);
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.enginePower = 500.0f;
        this.money = 100;
        this.critical = 5;
        this.ownerType = OwnerType.PLAYER;
        this.shop = new Shop(this);
        this.stringBuilder = new StringBuilder();
        createWeapons();
        this.weaponNum = 0;
        this.currentWeapon = weapons[weaponNum];
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        stringBuilder.clear();
        stringBuilder.append("SCORE: ").append(scoreView).append("\n");
        stringBuilder.append("HP: ").append(hp).append(" / ").append(hpMax).append("\n");
        stringBuilder.append("MONEY: ").append(money).append("\n");
        stringBuilder.append("BULLETS: ").append(currentWeapon.getCurrentBulletQuantity()).append("/")
                .append(currentWeapon.getMaxBulletQuantity()).append("\n");
        stringBuilder.append("MAGNETIC: ").append((int) magneticField.radius).append("\n");
        font.draw(batch, stringBuilder, 20, ScreenManager.SCREEN_HEIGHT - 20);
    }

    public void consume(PowerUp p) {
        switch (p.getType()) {
            case MEDKIT:
                int oldHp = hp;
                hp += p.getPower();
                if (hp > hpMax) {
                    hp = hpMax;
                }
                stringBuilder.clear();
                stringBuilder.append("HP +").append(hp - oldHp);
                gc.getInfoController().setup(p.getPosition().x, p.getPosition().y, stringBuilder.toString(), Color.GREEN);
                break;
            case MONEY:
                stringBuilder.clear();
                stringBuilder.append("MONEY +").append(p.getPower());
                gc.getInfoController().setup(p.getPosition().x, p.getPosition().y, stringBuilder.toString(), Color.YELLOW);
                money += p.getPower();
                break;
            case AMMOS:
                int count = currentWeapon.addAmmos(p.getPower());
                stringBuilder.clear();
                stringBuilder.append("AMMOS +").append(count);
                gc.getInfoController().setup(p.getPosition().x, p.getPosition().y, stringBuilder.toString(), Color.ORANGE);
                break;
        }
    }

    public boolean upgrade(Skill skill) {
        switch (skill) {
            case HP_MAX:
                hpMax += 10;
                return true;
            case HP:
                if (hp < hpMax) {
                    hp += 10;
                    if (hp > hpMax) {
                        hp = hpMax;
                    }
                    return true;
                }
            case WEAPON:
                if (weaponNum < weapons.length - 1) {
                    weaponNum++;
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
        super.update(dt);
        updateScore(dt);

        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            tryToFire();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            rotate(180.0f, dt);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            rotate(-180.0f, dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            accelerate(dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            brake(dt);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            setPause(true);
            shop.setVisible(true);
        }


        if (velocity.len() > 50.0f) {
            float bx = position.x + MathUtils.cosDeg(angle + 180) * 20;
            float by = position.y + MathUtils.sinDeg(angle + 180) * 20;
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
    }

    private void updateScore(float dt) {
        if (scoreView < score) {
            scoreView += 1000 * dt;
            if (scoreView > score) {
                scoreView = score;
            }
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



