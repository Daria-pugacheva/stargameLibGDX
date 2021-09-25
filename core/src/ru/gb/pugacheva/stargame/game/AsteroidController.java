package ru.gb.pugacheva.stargame.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.gb.pugacheva.stargame.game.helpers.ObjectPool;

public class AsteroidController extends ObjectPool<Asteroid> {
    private Texture asteroidTexture;

    @Override
    protected Asteroid newObject() {
        return new Asteroid();
    }

    public AsteroidController() {
        this.asteroidTexture = new Texture("asteroid.png");
        activeList.add(new Asteroid());
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Asteroid a = activeList.get(i); //задел на то, что может быть несколько астероидов, а не один, как сейчас
            batch.draw(asteroidTexture, a.getPosition().x - 128, a.getPosition().y - 128, 128, 128, 256, 256, 1, 1,
                    a.getAngel(), 0, 0, 256, 256, false, false);
        }
    }

    public void fillActiveList() {
        getActiveList().add(newObject());
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        //  checkPool(); пока не надо
    }

}
