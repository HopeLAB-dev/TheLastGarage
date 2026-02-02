package com.kouceng.prolab2.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kouceng.prolab2.Prolab2;

public class MainMenuScreen implements Screen {

    final Prolab2 game;
    private Stage stage;

    // Görseller
    private Texture background;
    private Texture logo;
    private Texture isim1, isim2;

    private Texture btnPlayTex, btnPlayHoverTex;
    private Texture btnExitTex, btnExitHoverTex;

    // Fade-in animasyonu
    private float fadeAlpha = 0f;
    private float fadeSpeed = 0.015f;

    public MainMenuScreen(final Prolab2 game) {
        this.game = game;

        // Stage ve Viewport kurulumu
        stage = new Stage(new FitViewport(1280, 720));
        Gdx.input.setInputProcessor(stage);

        // Görselleri yükle
        background = new Texture("menu_bg.png");
        logo = new Texture("logo.png");
        isim1 = new Texture("isim1.png");
        isim2 = new Texture("isim2.png");

        btnPlayTex = new Texture("baslat.png");
        btnPlayHoverTex = new Texture("baslat_hover.png");
        btnExitTex = new Texture("cikis.png");
        btnExitHoverTex = new Texture("cikis_hover.png");

        initButtons();
    }

    private void initButtons() {
        // Buton Stilleri
        TextureRegionDrawable playDrawable = new TextureRegionDrawable(new TextureRegion(btnPlayTex));
        TextureRegionDrawable playHoverDrawable = new TextureRegionDrawable(new TextureRegion(btnPlayHoverTex));
        ImageButton.ImageButtonStyle playStyle = new ImageButton.ImageButtonStyle();
        playStyle.up = playDrawable;
        playStyle.over = playHoverDrawable;
        playStyle.down = playHoverDrawable; // Mobilde basılı tutarken de hover efekti görünsün

        ImageButton btnPlay = new ImageButton(playStyle);
        btnPlay.setSize(300, 120);
        // Konum hesaplama: (1280 - 300) / 2 = 490
        btnPlay.setPosition(490, 330);

        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });

        TextureRegionDrawable exitDrawable = new TextureRegionDrawable(new TextureRegion(btnExitTex));
        TextureRegionDrawable exitHoverDrawable = new TextureRegionDrawable(new TextureRegion(btnExitHoverTex));
        ImageButton.ImageButtonStyle exitStyle = new ImageButton.ImageButtonStyle();
        exitStyle.up = exitDrawable;
        exitStyle.over = exitHoverDrawable;
        exitStyle.down = exitHoverDrawable;

        ImageButton btnExit = new ImageButton(exitStyle);
        btnExit.setSize(300, 120);
        btnExit.setPosition(490, 200);

        btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        stage.addActor(btnPlay);
        stage.addActor(btnExit);
    }

    @Override
    public void render(float delta) {

        // fadein efekti
        if (fadeAlpha < 1f) fadeAlpha += fadeSpeed;

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getViewport().apply();
        
        // Background ve sabit görselleri çizmek için batch'i başlatıyoruz
        // Stage kendi batch'ini kullanır ama biz arkaplanı stage'in arkasına çizmek istiyoruz
        game.batch.setProjectionMatrix(stage.getViewport().getCamera().combined);
        game.batch.begin();

        //arka plan
        game.batch.setColor(1, 1, 1, fadeAlpha);
        game.batch.draw(background, 0, 0, 1280, 720);

        // logo ve isimler
        if (logo != null) {
            float logoWidth = 700;
            float logoHeight = 200;
            float logoX = (1280 - logoWidth) / 2;
            float logoY = 450;
            game.batch.draw(logo, logoX, logoY, logoWidth, logoHeight);
        }
        if (isim1 != null) {
            float isim1W = 100;
            float isim1H = 50;
            float isim1X = ((1280 - isim1W) / 2) - 60;
            float isim1Y = 150;
            game.batch.draw(isim1, isim1X, isim1Y, isim1W, isim1H);
        }
        if (isim2 != null) {
            float isim2W = 130;
            float isim2H = 50;
            float isim2X = ((1280 - isim2W) / 2) + 60;
            float isim2Y = 150;
            game.batch.draw(isim2, isim2X, isim2Y, isim2W, isim2H);
        }

        game.batch.end();

        // Stage'i çiz (Butonlar burada)
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
        if (logo != null) logo.dispose();
        btnPlayTex.dispose();
        btnPlayHoverTex.dispose();
        btnExitTex.dispose();
        btnExitHoverTex.dispose();
        isim1.dispose();
        isim2.dispose();
    }

    @Override public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
