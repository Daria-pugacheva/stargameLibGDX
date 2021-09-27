package ru.gb.pugacheva.stargame.game;

import com.badlogic.gdx.math.MathUtils;
import ru.gb.pugacheva.stargame.screen.ScreenManager;

public class GameController {
    private Background background;
    private BulletController bulletController;
    private AsteroidController asteroidController;
    private Hero hero;

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

    public GameController() {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.bulletController = new BulletController();
        this.asteroidController = new AsteroidController(this);
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
        checkBulletCollisions(); // решила не объединять все проверки в один метод. Вроде, читабельнее, когда два отдельных
        checkHeroCollisions();
    }

    public void checkBulletCollisions() {
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition())) {
                    b.deactivate();
                    if (a.takeDamage(1)) {
                        hero.addScore(a.getHpMax() * 100);
                    }
                    break;
                }
            }
        }
    }

    public void checkHeroCollisions() {
            for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
                Asteroid a = asteroidController.getActiveList().get(i);
                if (a.getHitArea().overlaps(hero.getHitArea())) {
                    int damageSize = Math.round(a.getHitArea().radius/(a.getBASE_RADIUS()/a.getMAX_CHAIN_OF_ASTEROIDS()));
                    hero.takeDamage(damageSize); // Максимум - это кол-во астероидов в цепочке дробления, минимум 1, шаг - 1 (каждый следующий меньший по величине астеройд наносит ущерб меньше на 1)
                    a.deactivate();
            }
        }
    }

}
