package ru.gb.pugacheva.stargame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.gb.pugacheva.stargame.game.helpers.ObjectPool;

public class BotController extends ObjectPool<Bot> {
    private GameController gc;
    private boolean isSingleBotActive;
    private  Bot currentBot;

    public Bot getCurrentBot() {
        return currentBot;
    }

    public boolean isSingleBotActive() {
        return isSingleBotActive;
    }

    public void setSingleBotActive(boolean singleBotActive) {
        isSingleBotActive = singleBotActive;
    }

    @Override
    protected Bot newObject() {
        return new Bot(gc);
    }

    public BotController(GameController gc) {
        this.gc = gc;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            Bot b = activeList.get(i);
            b.render(batch);
        }
    }

    public void setup() {
        currentBot = getActiveElement();
        currentBot.activate();
        setSingleBotActive(true);
    }

    public void update(float dt, Hero hero) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt, hero);
        }
        checkPool();
    }

}
