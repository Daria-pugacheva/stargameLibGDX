package ru.gb.pugacheva.stargame.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class AbstractScreen implements Screen {

    protected SpriteBatch batch;

    public AbstractScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void resize(int width, int height) { // срабатывает, если меняется размер окна
        ScreenManager.getInstance().resize(width, height);
    }

    @Override
    public void pause() { // если мы приостанавливаем игру

    }

    @Override
    public void resume() { // если мы останавливаем игру

    }

    @Override
    public void hide() { // при скрытии окна (потеря фокуса)

    }

    @Override
    public void dispose() { // вызывается, когда экран уничтожается (выход из приложения)

    }
}
