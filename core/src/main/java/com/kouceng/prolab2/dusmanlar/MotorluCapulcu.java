package com.kouceng.prolab2.dusmanlar;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MotorluCapulcu extends dusman {

    public MotorluCapulcu() {
        super(50, 50, 0, 15, false, 5);



        setTexture(new Texture("motorlu_capulcu.png"));

        width = 60;
        height = 75;

    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch); // Base class (rotation + hp bar + tint)
    }
}
