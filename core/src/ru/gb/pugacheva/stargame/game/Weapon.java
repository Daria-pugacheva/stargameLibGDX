package ru.gb.pugacheva.stargame.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import ru.gb.pugacheva.stargame.screen.utils.Assets;

public class Weapon {
    private GameController gc;
    private Hero hero;
    private String title;
    private float firePeriod;
    private int damage; //урон, который наносит это оружие противнику
    private float bulletSpeed;
    private int maxBulletQuantity;
    private int currentBulletQuantity;
    private Vector3[] slots; //в параметрах х - это расстояние от центра,
    // У - это угол-направление, в котором отсчитываем направление,
    //Z - это напраление стрельбы из точки (указывается угол)
    private Sound shootSound;


    public float getFirePeriod() {
        return firePeriod;
    }

    public int getCurrentBulletQuantity() {
        return currentBulletQuantity;
    }

    public int getMaxBulletQuantity() {
        return maxBulletQuantity;
    }

    public Weapon(GameController gc, Hero hero, String title,
                  float firePeriod, int damage, float bulletSpeed,
                  int maxBulletQuantity, Vector3[] slots) {
        this.gc = gc;
        this.hero = hero;
        this.title = title;
        this.firePeriod = firePeriod;
        this.damage = damage;
        this.bulletSpeed = bulletSpeed;
        this.maxBulletQuantity = maxBulletQuantity;
        this.currentBulletQuantity = maxBulletQuantity;
        this.slots = slots;
        this.shootSound = Assets.getInstance().getAssetManager().get("audio/shoot.mp3");
    }

    public void fire() {
        if (currentBulletQuantity > 0) {
            currentBulletQuantity--;
            shootSound.play();

            for (int i = 0; i < slots.length; i++) {
                float x, y, vx, vy;

                x = hero.getPosition().x + MathUtils.cosDeg(hero.getAngel() + slots[i].y) * slots[i].x;
                y = hero.getPosition().y + MathUtils.sinDeg(hero.getAngel() + slots[i].y) * slots[i].x;

                vx = hero.getVelocity().x + bulletSpeed * MathUtils.cosDeg(hero.getAngel() + slots[i].z);
                vy = hero.getVelocity().x + bulletSpeed * MathUtils.sinDeg(hero.getAngel() + slots[i].z);

                gc.getBulletController().setup(x, y, vx, vy);

            }
        }
    }

    public void addAmmos(int amount) {
        currentBulletQuantity += amount;
        if (currentBulletQuantity > maxBulletQuantity) {
            currentBulletQuantity = maxBulletQuantity;
        }
    }
}
