package ru.gb.pugacheva.stargame.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import ru.gb.pugacheva.stargame.screen.utils.Assets;

public class LoadingScreen extends AbstractScreen {
    private Texture texture;

    public LoadingScreen(SpriteBatch batch) {
        super(batch);
        Pixmap pixmap = new Pixmap(ScreenManager.SCREEN_WIDTH, 20, Pixmap.Format.RGB888); // рисунок заданного размера, каждому цвету которого выделяется по 8 бит - у нас зеленая полоска в ширину экрана и высотой 20
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        this.texture = new Texture(pixmap);
        pixmap.dispose();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1); //очистка экрана черным цветом
        if (Assets.getInstance().getAssetManager().update()) { //условие, что все ресурсы загрузились. можно узнать у этого AssetsManager процент выполнения методом getProgress
            Assets.getInstance().makeLinks();
            ScreenManager.getInstance().goToTarget(); //target -  Это целевой экран, куда мы хотим перейти
            //при переходе от одного экрана к другому всегда будем показывать экран загрузки этот
        }
        batch.begin(); // если загрузка ресурсов еще не завершинась, то отрисовываем текстуру зеленой полоски
        batch.draw(texture, 0, 0, ScreenManager.SCREEN_WIDTH *
                Assets.getInstance().getAssetManager().getProgress(), 20); //getProgress - он от 0 до 1
        batch.end();
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}

