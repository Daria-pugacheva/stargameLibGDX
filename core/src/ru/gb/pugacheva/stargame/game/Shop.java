package ru.gb.pugacheva.stargame.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import ru.gb.pugacheva.stargame.screen.utils.Assets;

public class Shop extends Group {
    private Hero hero;
    private BitmapFont font24;

    public Shop(final Hero hero) {
        this.hero = hero;
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");

        Pixmap pixmap = new Pixmap(260, 260, Pixmap.Format.RGB888);
        pixmap.setColor(Color.rgb888(0.0f, 0.0f, 0.5f));
        pixmap.fill();

        Image image = new Image(new Texture(pixmap));
        this.addActor(image);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("shortButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);


        final TextButton btnWp = new TextButton("Weapon", textButtonStyle);

        btnWp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(Hero.Skill.WEAPON.cost)) {
                    if (hero.upgrade(Hero.Skill.WEAPON)) {
                        hero.decreaseMoney(Hero.Skill.WEAPON.cost);
                    }
                }
            }
        });

        btnWp.setTransform(true);
        btnWp.setScale(0.7f);
        btnWp.setPosition(20, 80);
        this.addActor(btnWp);


        final TextButton btnHp = new TextButton("HP", textButtonStyle);
        btnHp.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(Hero.Skill.HP.cost)) {
                    if (hero.upgrade(Hero.Skill.HP)) {
                        hero.decreaseMoney(Hero.Skill.HP.cost);
                    }
                }
            }
        });

        btnHp.setTransform(true);
        btnHp.setScale(0.7f);
        btnHp.setPosition(20, 140);
        this.addActor(btnHp);


        final TextButton btnHpMax = new TextButton("HP MAX", textButtonStyle);
        btnHpMax.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (hero.isMoneyEnough(Hero.Skill.HP_MAX.cost)) {
                    if (hero.upgrade(Hero.Skill.HP_MAX)) {
                        hero.decreaseMoney(Hero.Skill.HP_MAX.cost);
                    }
                }
            }
        });

        btnHpMax.setTransform(true);
        btnHpMax.setScale(0.7f);
        btnHpMax.setPosition(20, 200);
        this.addActor(btnHpMax);


        final TextButton btnClose = new TextButton("X", textButtonStyle);
        final Shop thisShop = this;

        btnClose.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                thisShop.setVisible(false);
            }
        });

        btnClose.setTransform(true);
        btnClose.setScale(0.4f);
        btnClose.setPosition(220, 220);
        this.addActor(btnClose);

        this.setPosition(20, 20);
        this.setVisible(false);
        skin.dispose();
    }
}
