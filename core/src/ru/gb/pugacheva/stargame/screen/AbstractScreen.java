package ru.gb.pugacheva.stargame.screen;

import com.badlogic.gdx.Screen;

public abstract class AbstractScreen implements Screen {
//    @Override
//    public void show() { //при появлении окна (появлении фокуса)
//
//    }
//
//    @Override
//    public void render(float delta) { // отрисовка
//
//    }

    @Override
    public void resize(int width, int height) { // срабатывает, если меняется размер окна

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
