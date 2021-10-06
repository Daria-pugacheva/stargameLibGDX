package ru.gb.pugacheva.stargame.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.gb.pugacheva.stargame.screen.ScreenManager;

public class GameController {
    private Background background;
    private BulletController bulletController;
    private AsteroidController asteroidController;
    private ParticleController particleController;
    private PowerUpsController powerUpsController;
    private Hero hero;
    private Vector2 tempVector;

    public ParticleController getParticleController() {
        return particleController;
    }

    public Hero getHero() {
        return hero;
    }

    public Background getBackground() {
        return background;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public PowerUpsController getPowerUpsController() {
        return powerUpsController;
    }

    public GameController() {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.bulletController = new BulletController(this);
        this.particleController = new ParticleController();
        this.asteroidController = new AsteroidController(this);
        this.powerUpsController = new PowerUpsController(this);
        this.tempVector = new Vector2(0.0f, 0.0f);
        for (int i = 0; i < 3; i++) {
            asteroidController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                    MathUtils.random(-200, 200), MathUtils.random(-200, 200), 1.0f);
        }

    }

    public void update(float dt) {
        background.update(dt);
        hero.update(dt);
        asteroidController.update(dt);
        bulletController.update(dt);
        powerUpsController.update(dt);
        particleController.update(dt);
        checkCollisions();
    }

    public void checkCollisions() {
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid a = asteroidController.getActiveList().get(i);
            if (a.getHitArea().overlaps(hero.getHitarea())) {
                float distance = a.getPosition().dst(hero.getPosition()); //расстояение между центрами астероида и корабля
                float halfOverlen = ((a.getHitArea().radius + hero.getHitarea().radius) - distance) / 2;
                tempVector.set(hero.getPosition()).sub(a.getPosition()).nor();
                hero.getPosition().mulAdd(tempVector, halfOverlen);
                a.getPosition().mulAdd(tempVector, -halfOverlen);

                float sumScl = hero.getHitarea().radius * 2 + a.getHitArea().radius; // общая "сила" столкновения

                hero.getVelocity().mulAdd(tempVector, 200.0f * a.getHitArea().radius / sumScl);
                a.getVelocity().mulAdd(tempVector, -200.0f * hero.getHitarea().radius / sumScl);

                if (a.takeDamage(2)) {
                    hero.addScore(a.getHpMax() * 20);
                }
                hero.takeDamage(2);
            }

        }

        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition())) {
                    particleController.setup(
                            b.getPosition().x + MathUtils.random(-4, 4),
                            b.getPosition().y + MathUtils.random(-4, 4),
                            b.getVelocity().x * -0.3f + MathUtils.random(-30, 30),
                            b.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                            0.2f,
                            2.5f, 1.7f,
                            1.0f, 1.0f, 1.0f, 1.0f,
                            0.0f, 0.0f, 1.0f, 0.0f
                    );
                    b.deactivate();
                    if (a.takeDamage(1)) {
                        hero.addScore(a.getHpMax() * 100);
                        for (int k = 0; k < 3; k++) {
                            powerUpsController.setup(a.getPosition().x, a.getPosition().y, a.getScale() / 4.0f);
                        }
                    }
                    break;
                }
            }
        }

        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUp p = powerUpsController.getActiveList().get(i);
            if (hero.getHitarea().contains(p.getPosition())) {
                hero.consume(p);
                particleController.getEffectBuilder().takePowerUpEffect(p.getPosition().x, p.getPosition().y);

                p.deactivate();
            }
        }
    }

}
