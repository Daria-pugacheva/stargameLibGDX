package ru.gb.pugacheva.stargame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.gb.pugacheva.stargame.game.helpers.ObjectPool;

public class BonusController extends ObjectPool<Bonus> {

    private GameController gc;

    @Override
    protected Bonus newObject() {
        return new Bonus(gc);
    }

    public BonusController(GameController gc) {
        this.gc = gc;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Bonus b = activeList.get(i);
            b.render(batch);
        }
    }

    public void setup() {
        getActiveElement().activate();
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
