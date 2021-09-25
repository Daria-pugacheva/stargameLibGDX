package ru.gb.pugacheva.stargame.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import ru.gb.pugacheva.stargame.screen.ScreenManager;

public class Hero {
    private GameController gc;
    private Texture texture;
    private Vector2 position;
    private Vector2 velocity;
//    private Vector2 lastDisplacement;
    private float angel;
    private float enginePower;
    private float fireTimer;

//    public Vector2 getLastDisplacement() {
//        return lastDisplacement;
//    }


    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Hero(GameController gc) {
        this.gc = gc;
        this.texture = new Texture("ship.png");
        this.position = new Vector2(480,270);
        //this.lastDisplacement = new Vector2(0,0);
        this.velocity = new Vector2(0,0); // вектор скорости изначально нулевой - не двигаемся
        this.angel = 0.0f;
        this.enginePower = 500.0f;
    }

    public void render (SpriteBatch batch){
        batch.draw(texture,position.x - 32, position.y-32,32,32,64,64,1,1,
                angel,0,0,64,64,false,false);

    }

    public void update (float dt){
        fireTimer += dt;

        if(Gdx.input.isKeyPressed(Input.Keys.P)){
            if(fireTimer > 0.2){
                fireTimer=0.0f;
                gc.getBulletController().setup(position.x, position.y,
                        MathUtils.cosDeg(angel)* 500 + velocity.x, MathUtils.sinDeg(angel)*500 + velocity.y);
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            angel += 180.0f * dt;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            angel -= 180.0f * dt;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            velocity.x += MathUtils.cosDeg(angel) * enginePower * dt;
            velocity.y += MathUtils.sinDeg(angel) * enginePower * dt;
//            position.x += MathUtils.cosDeg(angel) * 240.0f * dt;
//            position.y += MathUtils.sinDeg(angel) * 240.0f * dt;
            //lastDisplacement.set(MathUtils.cosDeg(angel) * 240.0f * dt,MathUtils.sinDeg(angel) * 240.0f * dt);
//        }else if(Gdx.input.isKeyPressed(Input.Keys.S)) {
//            position.x += MathUtils.cosDeg(angel - 180) * 120.0f * dt;
//            position.y += MathUtils.sinDeg(angel - 180) * 120.0f * dt;
//            lastDisplacement.set(MathUtils.cosDeg(angel - 180) * 120.0f * dt, MathUtils.sinDeg(angel - 180) * 120.0f * dt);
//        }
    }else if(Gdx.input.isKeyPressed(Input.Keys.S)) {
        velocity.x += MathUtils.cosDeg(angel - 180) * enginePower/2 * dt;
        velocity.y += MathUtils.sinDeg(angel - 180) * enginePower/2 * dt;
       // lastDisplacement.set(MathUtils.cosDeg(angel - 180) * 120.0f * dt, MathUtils.sinDeg(angel - 180) * 120.0f * dt);
    }
//        else {
//            lastDisplacement.set(0,0);
//        }

//        position.x += velocity.x * dt;
//        position.y += velocity.y *dt;
        position.mulAdd(velocity,dt); // Добавляя вектор скорости к позиции, получаем движение
        float stopKoef = 1.0f -1.0f*dt;
        if(stopKoef<0){
            stopKoef = 0;
        }
        velocity.scl(stopKoef);

        if(position.x < 32f){
            position.x = 32f;
            velocity.x*=-1; // отскок в обратную сторону
        }
        if (position.x > ScreenManager.SCREEN_WIDTH  -32f ){
            position.x = ScreenManager.SCREEN_WIDTH  -32f;
            velocity.x*=-1;
        }
        if(position.y < 32f){
            position.y = 32f;
            velocity.y*=-1;
        }
        if(position.y > ScreenManager.SCREEN_HEIGHT - 32f){
            position.y = ScreenManager.SCREEN_HEIGHT - 32f;
            velocity.y*=-1;
        }

    }

}
