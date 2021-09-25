package ru.gb.pugacheva.stargame.game;

public class GameController {
    private Background background;
    private BulletController bulletController;
    private Hero hero;

    public Hero getHero() {
        return hero;
    }

    public Background getBackground() {
        return background;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public GameController() {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.bulletController = new BulletController();
    }

    public void update (float dt){
        background.update(dt);
        hero.update(dt);
        bulletController.update(dt);
    }

}
