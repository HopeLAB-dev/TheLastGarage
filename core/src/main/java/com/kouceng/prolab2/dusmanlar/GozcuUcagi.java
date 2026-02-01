package com.kouceng.prolab2.dusmanlar;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GozcuUcagi extends dusman {

    public GozcuUcagi() {
        super(50, 75, 0, 25, true, 5);



        setTexture(new Texture("gozcu_ucagi.png"));

        width = 85;
        height = 85;
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
    }
}
