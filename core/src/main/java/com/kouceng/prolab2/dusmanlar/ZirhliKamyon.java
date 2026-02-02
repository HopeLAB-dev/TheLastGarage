package com.kouceng.prolab2.dusmanlar;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Random;

public class ZirhliKamyon extends dusman {

    public ZirhliKamyon() {
        super(75, 35, 0, 25, false, 30);

        // Rastgele zırh
        Random rand = new Random();
        this.armor = 50 + rand.nextInt(51);

        setTexture(new Texture("zirhli_kamyon.png"));

        width = 80;
        height = 120;
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }
}
