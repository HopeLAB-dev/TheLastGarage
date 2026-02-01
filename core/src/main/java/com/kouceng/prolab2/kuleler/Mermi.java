package com.kouceng.prolab2.kuleler;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.kouceng.prolab2.dusmanlar.dusman;

public class Mermi {

    private Vector2 position;
    private dusman target;
    private kule owner;

    private float speed = 300f;
    private boolean active = true;
    private boolean hit = false;

    private static Texture texCivi;
    private static Texture texAnahtar;
    private static Texture texYag;


    private TextureRegion myTexture;

    private float size = 25;

    public Mermi(float x, float y, dusman target, kule owner) {
        this.position = new Vector2(x, y);
        this.target = target;
        this.owner = owner;

        if (texCivi == null) texCivi = new Texture("civimermi.png");
        if (texAnahtar == null) texAnahtar = new Texture("anahtarmermi.png");
        if (texYag == null) texYag = new Texture("yagmermi.png");

        if (owner instanceof CiviAgAtar)
            myTexture = new TextureRegion(texCivi);
        else if (owner instanceof AnahtarMakinesi)
            myTexture = new TextureRegion(texAnahtar);
        else if (owner instanceof YagSizdirici)
            myTexture = new TextureRegion(texYag);
        else
            myTexture = new TextureRegion(texCivi);
    }

    public void update(float delta) {
        if (!active || target == null) return;

        if (target.isDead()) {
            active = false;
            return;
        }

        float tx = target.getX() + 15;
        float ty = target.getY() + 15;

        float dist = position.dst(tx, ty);
        float step = speed * delta;

        if (dist <= step || dist < 20) {
            hit = true;
            active = false;
            position.set(tx, ty);
        } else {
            Vector2 dir = new Vector2(tx - position.x, ty - position.y).nor();
            position.mulAdd(dir, step);
        }
    }

    public void render(SpriteBatch batch) {
        if (!active) return;

        float tx = target.getX() + 15;
        float ty = target.getY() + 15;

        float angleDeg = (float)Math.toDegrees(Math.atan2(ty - position.y, tx - position.x));

        batch.draw(
            myTexture,
            position.x - size / 2, position.y - size / 2,
            size / 2, size / 2,
            size, size,
            1f, 1f,
            angleDeg
        );
    }

    public boolean isActive() { return active; }
    public boolean hasHit() { return hit; }
    public dusman getTarget() { return target; }
    public kule getOwner() { return owner; }

    public static void disposeTextures() {
        if (texCivi != null) texCivi.dispose();
        if (texAnahtar != null) texAnahtar.dispose();
        if (texYag != null) texYag.dispose();
    }
}
