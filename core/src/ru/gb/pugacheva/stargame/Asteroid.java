package ru.gb.pugacheva.stargame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Asteroid {

    private Texture texture;
    private Vector2 position;
    private float angel;

    public Asteroid() {
        this.texture = new Texture("asteroid.png");
        this.position = new Vector2(-128,ScreenManager.SCREEN_HEIGHT/2f); //особо не влияет стартовая позиция, при обновлениях меняется.
        this.angel = 0.0f;
    }

    public void render (SpriteBatch batch){
        batch.draw(texture,position.x - 128, position.y-128,128,128,256,256,1,1,
                angel,0,0,256,256,false,false);
    }

    public void update (float dt) {
        position.x += MathUtils.cosDeg(angel) * 120.0f * dt;
        position.y += MathUtils.sinDeg(angel) * 120.0f * dt;
        if(position.x > ScreenManager.SCREEN_WIDTH + 128
                || position.y > ScreenManager.SCREEN_HEIGHT + 128
                || position.y < ScreenManager.SCREEN_HEIGHT + -128){
            position.x = -128;
            position.y = MathUtils.random(128,ScreenManager.SCREEN_HEIGHT-128);
            angel = MathUtils.random(-20,20);
        }
    }

    }

