package ru.gb.pugacheva.stargame.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import ru.gb.pugacheva.stargame.screen.utils.Assets;

public class Weapon {
    public enum WeaponType{
        LASER, GREENLASER;
    }

    private GameController gc;
    private Ship ship;
    private WeaponType weaponType;
   // private String title;
    private float firePeriod;
    private int damage; //урон, который наносит это оружие противнику
    private float bulletSpeed;
    private int maxBulletQuantity;
    private int currentBulletQuantity;
    private Vector3[] slots; //в параметрах х - это расстояние от центра,
    // У - это угол-направление, в котором отсчитываем направление,
    //Z - это напраление стрельбы из точки (указывается угол)
    private Sound shootSound;

//    public String getTitle() {
//        return title;
//    }


    public WeaponType getWeaponType() {
        return weaponType;
    }

    public int getDamage() {
        return damage;
    }

    public float getFirePeriod() {
        return firePeriod;
    }

    public int getCurrentBulletQuantity() {
        return currentBulletQuantity;
    }

    public int getMaxBulletQuantity() {
        return maxBulletQuantity;
    }

    public Weapon(GameController gc, Ship ship, WeaponType weaponType,
                  float firePeriod, int damage, float bulletSpeed,
                  int maxBulletQuantity, Vector3[] slots) {
        this.gc = gc;
        this.ship = ship;
        this.weaponType = weaponType;
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

                x = ship.getPosition().x + MathUtils.cosDeg(ship.getAngle() + slots[i].y) * slots[i].x;
                y = ship.getPosition().y + MathUtils.sinDeg(ship.getAngle() + slots[i].y) * slots[i].x;

                vx = ship.getVelocity().x + bulletSpeed * MathUtils.cosDeg(ship.getAngle() + slots[i].z);
                vy = ship.getVelocity().x + bulletSpeed * MathUtils.sinDeg(ship.getAngle() + slots[i].z);

                gc.getBulletController().setup(ship, x, y, vx, vy);

            }
        }
    }

    public int addAmmos(int amount) {
        int old = currentBulletQuantity;
        currentBulletQuantity += amount;
        if (currentBulletQuantity > maxBulletQuantity) {
            currentBulletQuantity = maxBulletQuantity;
        }
        return currentBulletQuantity - old;
    }
}
