package ru.gb.pugacheva.stargame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.StringBuilder;
import ru.gb.pugacheva.stargame.screen.ScreenManager;
import ru.gb.pugacheva.stargame.screen.utils.Assets;

public class GameController {
    private Background background;
    private BulletController bulletController;
    private AsteroidController asteroidController;
    private ParticleController particleController;
    private PowerUpsController powerUpsController;
    private InfoController infoController;
    private Hero hero;
    private Bot bot;
    private Vector2 tempVector;
    private Stage stage;
    private boolean pause;
    private int level;
    private float roundTimer;
    private Music music;
    private Sound explosionSound;
    private StringBuilder stringBuilder;
    private int botQuantity;
    private float pauseWithoutBot;

    public int getBotQuantity() {
        return botQuantity;
    }

    public Bot getBot() {
        return bot;
    }

    public InfoController getInfoController() {
        return infoController;
    }

    public float getRoundTimer() {
        return roundTimer;
    }

    public int getLevel() {
        return level;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public Stage getStage() {
        return stage;
    }

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

    public GameController(SpriteBatch batch) {
        this.background = new Background(this);
        this.hero = new Hero(this);
//        this.bot = new Bot(this);
        this.botQuantity = 0;
        this.pauseWithoutBot=0;
        this.bulletController = new BulletController(this);
        this.particleController = new ParticleController();
        this.asteroidController = new AsteroidController(this);
        this.powerUpsController = new PowerUpsController(this);
        this.infoController = new InfoController();
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.stage.addActor(hero.getShop());
        Gdx.input.setInputProcessor(stage);
        this.tempVector = new Vector2(0.0f, 0.0f);
        this.stringBuilder = new StringBuilder();
        this.level = 1;
        this.roundTimer = 0.0f;
        this.music = Assets.getInstance().getAssetManager().get("audio/mortal.mp3");
        this.music.setLooping(true);
        this.music.play();
        this.explosionSound = Assets.getInstance().getAssetManager().get("audio/explosion.mp3");
        generateBigAsteroids(1);
    }

    private void generateBigAsteroids(int count) {
        for (int i = 0; i < count; i++) {
            asteroidController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                    MathUtils.random(-200, 200), MathUtils.random(-200, 200), 1.0f);
        }
    }

    public void update(float dt) {
        if (pause) {
            return;
        }
        pauseWithoutBot+=dt;
        roundTimer += dt;
        background.update(dt);
        hero.update(dt);
        if (hero.getHpMax() > 120 && botQuantity == 0 && pauseWithoutBot > 15.0f) {
            this.bot = new Bot(this);
            botQuantity = 1;
        }
//        if (bot.isAlive()) {
//            bot.update(dt);
//        }
        if (botQuantity == 1) {
            bot.update(dt);
        }
        asteroidController.update(dt);
        bulletController.update(dt);
        powerUpsController.update(dt);
        particleController.update(dt);
        infoController.update(dt);
        checkCollisions();
        if (!hero.isAlive()) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAMEOVER, hero);
        }
        if (asteroidController.getActiveList().size() == 0) {
            level++;
            generateBigAsteroids(level <= 3 ? level : 3); //до 3-го уровня кол-во астероидов на старте равно уровню, потом все время по три астероида генерим
            roundTimer = 0.0f;
        }
        stage.act(dt);
    }

    public void checkCollisions() {
        checkAsteroidHeroCollisions();

        if (botQuantity == 1) {
            checkAsteroidBotCollisions();
            checkCrossDamageFromBulletsOfBotAndHero();
        }
        checkAsteroidBulletCollisions();

//        if(botQuantity>0){
//          checkCrossDamageFromBulletsOfBotAndHero();
//        }
        checkHeroPowerUpsCollisions();


    }

    private void checkAsteroidHeroCollisions() {
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid a = asteroidController.getActiveList().get(i);
            if (a.getHitArea().overlaps(hero.getHitArea())) {
                float dst = a.getPosition().dst(hero.getPosition());
                float halfOverLen = (a.getHitArea().radius + hero.getHitArea().radius - dst) / 2.0f;
                tempVector.set(hero.getPosition()).sub(a.getPosition()).nor();
                hero.getPosition().mulAdd(tempVector, halfOverLen);
                a.getPosition().mulAdd(tempVector, -halfOverLen);

                float sumScl = hero.getHitArea().radius * 2 + a.getHitArea().radius;

                hero.getVelocity().mulAdd(tempVector, 200.0f * a.getHitArea().radius / sumScl);
                a.getVelocity().mulAdd(tempVector, -200.0f * hero.getHitArea().radius / sumScl);

                hero.takeDamage(level * 2);
                stringBuilder.clear();
                stringBuilder.append("HP -").append(level * 2);
                infoController.setup(hero.getPosition().x, hero.getPosition().y, stringBuilder.toString(), Color.RED);

                if (a.takeDamage(2)) {
                    hero.addScore(a.getHpMax() * 20);
                    continue;
                }
            }
        }
    }

    private void checkAsteroidBotCollisions() {
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid a = asteroidController.getActiveList().get(i);
            if (a.getHitArea().overlaps(bot.getHitArea())) {
                float dst = a.getPosition().dst(bot.getPosition());
                float halfOverLen = (a.getHitArea().radius + bot.getHitArea().radius - dst) / 2.0f;
                tempVector.set(bot.getPosition()).sub(a.getPosition()).nor();
                bot.getPosition().mulAdd(tempVector, halfOverLen);
                a.getPosition().mulAdd(tempVector, -halfOverLen);

                float sumScl = bot.getHitArea().radius * 2 + a.getHitArea().radius;

                bot.getVelocity().mulAdd(tempVector, 200.0f * a.getHitArea().radius / sumScl);
                a.getVelocity().mulAdd(tempVector, -200.0f * bot.getHitArea().radius / sumScl);

                a.takeDamage(2);

            }
        }
    }

    private void checkAsteroidBulletCollisions() {
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition())) {
                    particleController.getEffectBuilder()
                            .bulletCollideWithAsteroid(b.getPosition(), b.getVelocity());

                    b.deactivate();
                    int damage = hero.getCurrentWeapon().getDamage();
                    if (MathUtils.random(0, 100) < hero.getCritical()) {
                        damage *= 3;
                        stringBuilder.clear();
                        stringBuilder.append("-").append(damage);
                        infoController.setup(a.getPosition().x, a.getPosition().y, stringBuilder.toString(), Color.WHITE);
                    }
                    if (a.takeDamage(damage)) {
                        if (b.getOwner().getOwnerType() == OwnerType.PLAYER) {
                            hero.addScore(a.getHpMax() * 100);
                            for (int k = 0; k < 3; k++) {
                                powerUpsController.setup(a.getPosition().x, a.getPosition().y, a.getScale() / 4.0f);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    private void checkCrossDamageFromBulletsOfBotAndHero() {
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            if (b.getOwner().getOwnerType() == OwnerType.PLAYER && bot.isAlive()) {
                if (bot.getHitArea().contains(b.getPosition())) {
                    bot.takeDamage(hero.currentWeapon.getDamage());
                    if (!bot.isAlive()) {
                        particleController.getEffectBuilder().killBotEffect(bot.getPosition().x, b.getPosition().y);
                        explosionSound.play();
                        botQuantity=0;
                        pauseWithoutBot=0;
                    }
                    b.deactivate();
                }
            }

            if (b.getOwner().getOwnerType() == OwnerType.BOT) {
                if (hero.getHitArea().contains(b.getPosition())) {
                    hero.takeDamage(bot.currentWeapon.getDamage());
                    b.deactivate();
                }
            }
        }
    }

    private void checkHeroPowerUpsCollisions() {
        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUp p = powerUpsController.getActiveList().get(i);
            if (hero.getMagneticField().contains(p.getPosition())) {
                tempVector.set(hero.getPosition()).sub(p.getPosition()).nor();
                p.getVelocity().mulAdd(tempVector, 200.0f);
            }

            if (hero.getHitArea().contains(p.getPosition())) {
                hero.consume(p);
                particleController.getEffectBuilder().takePowerUpEffect(
                        p.getPosition().x, p.getPosition().y, p.getType());
                p.deactivate();
            }
        }
    }


    public void dispose() {
        background.dispose();
    }
}
