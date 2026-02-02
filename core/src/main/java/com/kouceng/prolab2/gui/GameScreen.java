package com.kouceng.prolab2.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.kouceng.prolab2.Prolab2;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

// UI
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.Align;

// Dusmanalr
import com.kouceng.prolab2.dusmanlar.*;

// Kuleler
import com.kouceng.prolab2.kuleler.*;
import com.kouceng.prolab2.kuleler.Mermi;

// Log
import com.kouceng.prolab2.log.CombatLog;

import java.util.Iterator;

public class GameScreen implements Screen {

    // oyun alanları ( degiskenleri )
    final Prolab2 game;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;

    private Texture mapTexture;
    private Texture towerSlotTexture;

    private Stage uiStage;
    private ImageButton btnCivi, btnAnahtar, btnYag, btnWaveStart;

    private Texture texBtnCivi, texBtnAnahtar, texBtnYag;
    private Texture texBtnCiviDisabled, texBtnAnahtarDisabled, texBtnYagDisabled;
    private Texture texWaveStart;
    private Texture ghostCivi, ghostAnahtar, ghostYag;
    private Texture panelTexture;
    private Texture texWin;
    private Texture texLose;
    private Texture texMenuBtn;

    private int scrap = 200;
    private int garageHp = 100;
    private int wave = 0;
    private int totalEnemiesKilled = 0; // Istatistik

    private boolean isGameOver = false;
    private boolean isPaused = false;
    private boolean isWaveActive = false;
    private float gameSpeed = 2f;

    private Array<dusman> enemies;
    private Array<kule> towers;
    private Array<Mermi> projectiles;
    private Array<Vector2> path;
    private Array<Vector2> towerSpots;
    private Array<dusman> spawnQueue;

    private long lastSpawn = 0;
    private final int SPAWN_INTERVAL = 1200;

    private String selectedTower = null;
    private Vector2 ghostSnap = null;
    private float ghostRange = 120;

    // Tooltip icin
    private String tooltipText = null;

    // Tutorial (Tanitim) Penceresi icin
    private boolean isTutorialOpen = false;
    private String tutTitle = "";
    private String tutBody = "";
    private Texture tutImg = null;

    // Tutorial gorselleri (Cache)
    private Texture texMotorcu, texKamyon, texUcak;

    // constructor
    public GameScreen(Prolab2 game) {
        this.game = game;

        enemies = new Array<>();
        towers = new Array<>();
        projectiles = new Array<>();
        path = new Array<>();
        towerSpots = new Array<>();
        spawnQueue = new Array<>();

        CombatLog.resetLog();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        shapeRenderer = new ShapeRenderer();

        // texture yuklemeleri
        mapTexture = new Texture("map1.jpg");
        towerSlotTexture = new Texture("slot.png");
        panelTexture = new Texture("panel.png");
        texWin = new Texture("kazandiniz.png");
        texLose = new Texture("kaybettiniz.png");
        texMenuBtn = new Texture("ana_menu.png");
        texWaveStart = new Texture("wave_baslat.png");

        ghostCivi = new Texture("civi.png");
        ghostAnahtar = new Texture("anahtar.png");
        ghostYag = new Texture("yag.png");

        texBtnCivi = new Texture("civi_aktif.png");
        texBtnAnahtar = new Texture("anahtar_aktif.png");
        texBtnYag = new Texture("yag_aktif.png");

        texBtnCiviDisabled = new Texture("civi_pasif.png");
        texBtnAnahtarDisabled = new Texture("anahtar_pasif.png");
        texBtnYagDisabled = new Texture("yag_pasif.png");

        // Tutorial Textures
        texMotorcu = new Texture("motorlu_capulcu.png");
        texKamyon = new Texture("zirhli_kamyon.png");
        texUcak = new Texture("gozcu_ucagi.png");

        uiStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(uiStage);

        initButtons();
        initPath();
        initTowerSpots();
    }

    // oyundaki butonlar
    private void initButtons() {

        btnCivi = new ImageButton(new TextureRegionDrawable(new TextureRegion(texBtnCivi)));
        setupButton(btnCivi, "Civi", 70, 360, 15, "Civi Ag Atar\nHasar: Orta\nHiz: Orta\nOzellik: Tek Hedef");

        btnAnahtar = new ImageButton(new TextureRegionDrawable(new TextureRegion(texBtnAnahtar)));
        setupButton(btnAnahtar, "Anahtar", 50, 540, 15,
                "Anahtar Makinesi\nHasar: Dusuk\nHiz: Cok Hizli\nOzellik: Seri Atis");

        btnYag = new ImageButton(new TextureRegionDrawable(new TextureRegion(texBtnYag)));
        setupButton(btnYag, "Yag", 75, 720, 15, "Yag Sizdirici\nHasar: Yok\nHiz: Yavas\nOzellik: Yavaslatma (Alan)");

        uiStage.addActor(btnCivi);
        uiStage.addActor(btnAnahtar);
        uiStage.addActor(btnYag);
        // wave butonu
        btnWaveStart = new ImageButton(new TextureRegionDrawable(new TextureRegion(texWaveStart)));
        btnWaveStart.setSize(260, 100);
        btnWaveStart.setPosition(1010, 28); // sağ alt köşe
        btnWaveStart.setOrigin(Align.center);
        btnWaveStart.setTransform(true);

        // mouse ile buyut
        btnWaveStart.addListener(new ClickListener() {

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (pointer == -1 && !isWaveActive) {
                    btnWaveStart.setScale(1.10f);
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (pointer == -1) {
                    btnWaveStart.setScale(1f);
                }
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isWaveActive && !isGameOver) {
                    startNextWave();
                }
            }
        });
        uiStage.addActor(btnWaveStart);
    }

    private void setupButton(ImageButton btn, String type, int cost, float x, float y, final String info) {
        btn.setSize(220, 130);
        btn.setPosition(x, y);
        btn.setOrigin(Align.center);
        btn.setTransform(true);

        btn.addListener(new ClickListener() {

            @Override
            public void enter(InputEvent event, float xx, float yy, int pointer, Actor fromActor) {
                if (pointer == -1) {
                    tooltipText = info; // Tooltip set et
                    if (scrap >= cost)
                        btn.setScale(1.15f);
                }
            }

            @Override
            public void exit(InputEvent event, float xx, float yy, int pointer, Actor toActor) {
                if (pointer == -1) {
                    tooltipText = null; // Tooltip temizle
                    btn.setScale(1f);
                }
            }

            @Override
            public void clicked(InputEvent e, float xx, float yy) {
                if (scrap >= cost) {
                    selectedTower = type;

                    if (type.equals("Civi"))
                        ghostRange = CiviAgAtar.range;
                    else if (type.equals("Anahtar"))
                        ghostRange = AnahtarMakinesi.range;
                    else if (type.equals("Yag"))
                        ghostRange = YagSizdirici.range;
                }
            }
        });
    }

    // YOL ve KULE YERLERİ
    private void initPath() {
        path.add(new Vector2(-20, 330));
        path.add(new Vector2(420, 330));
        path.add(new Vector2(420, 480));
        path.add(new Vector2(670, 480));
        path.add(new Vector2(670, 180));
        path.add(new Vector2(1130, 180));
        path.add(new Vector2(1130, 450));
    }

    private void initTowerSpots() {
        towerSpots.clear();

        towerSpots.add(new Vector2(260, 230));
        towerSpots.add(new Vector2(320, 425));
        towerSpots.add(new Vector2(550, 390));
        towerSpots.add(new Vector2(1025, 280));
        towerSpots.add(new Vector2(760, 420));
        towerSpots.add(new Vector2(550, 580));
        towerSpots.add(new Vector2(780, 280));
    }

    // render loopu ( her framede tekrar calısır ) ( main yerine )
    @Override
    public void render(float delta) {

        // girdiler
        handleInput();

        // Eger tutorial aciksa oyunu durdur ve tutorial ciz
        if (isTutorialOpen) {
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            // Arka plani cizmeye devam et ama update etme
            game.batch.setProjectionMatrix(camera.combined);
            game.batch.begin();
            game.batch.draw(mapTexture, 0, 0, 1280, 720);
            for (kule t : towers)
                t.render(game.batch);
            for (dusman e : enemies)
                e.render(game.batch);
            game.batch.end();

            drawTutorial();
            return;
        }

        updateButtonState();

        // oyun update
        if (!isPaused && !isGameOver)
            update(delta * gameSpeed);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // harita + kule konumları
        game.batch.begin();
        game.batch.draw(mapTexture, 0, 0, 1280, 720);

        for (Vector2 spot : towerSpots)
            game.batch.draw(towerSlotTexture, spot.x - 40, spot.y - 40, 80, 80);

        game.batch.end();

        // ui stage
        uiStage.act(delta);
        uiStage.draw();
        // dalga butonu
        btnWaveStart.setVisible(!isWaveActive && !isGameOver);

        // üst panel
        drawTopPanel();

        // oyun nesneleri
        drawObjects();

        // yazılar
        drawUI();

        // son ekran
        if (isGameOver) {
            drawEndScreen();
            return;
        }
    }

    private void showTutorial(String title, String body, Texture img) {
        this.tutTitle = title;
        this.tutBody = body;
        this.tutImg = img;
        this.isTutorialOpen = true;
    }

    private void drawTutorial() {
        // Karartma (Daha koyu siyah)
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.92f);
        shapeRenderer.rect(0, 0, 1280, 720);

        // Pencere Cercevesi
        float w = 600;
        float h = 400;
        float x = (1280 - w) / 2;
        float y = (720 - h) / 2;

        // Dis Cerceve (Metalik Kirmizi/Gri)
        shapeRenderer.setColor(0.5f, 0.1f, 0.1f, 1f);
        shapeRenderer.rect(x - 4, y - 4, w + 8, h + 8);

        // Ic Arka Plan (Cok Koyu Gri)
        shapeRenderer.setColor(0.1f, 0.1f, 0.1f, 1f);
        shapeRenderer.rect(x, y, w, h);

        // Buton (Devam Et) - Yesil
        float btnW = 200;
        float btnH = 60;
        float btnX = x + (w - btnW) / 2;
        float btnY = y + 30;

        shapeRenderer.setColor(0.1f, 0.6f, 0.1f, 1f);
        shapeRenderer.rect(btnX, btnY, btnW, btnH);

        // Buton Golgesi (3D efekt icin)
        shapeRenderer.setColor(0, 0.4f, 0, 1f);
        shapeRenderer.rect(btnX, btnY, btnW, 5); // Alt golge

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();

        // Resim
        if (tutImg != null) {
            game.batch.draw(tutImg, x + (w - 150) / 2, y + h - 180, 150, 150);
        }

        // Yazi - Baslik (Kirmizi)
        game.font.getData().setScale(1.5f);
        game.font.setColor(Color.RED);
        float titleW = new com.badlogic.gdx.graphics.g2d.GlyphLayout(game.font, tutTitle).width;
        game.font.draw(game.batch, tutTitle, x + (w - titleW) / 2, y + h - 20);

        // Yazi - Icerik (Beyaz)
        game.font.getData().setScale(1f);
        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, tutBody, x + 30, y + h - 200, w - 60, Align.center, true);

        // Buton Yazi (Beyaz)
        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, "ANLASILDI", btnX + 50, btnY + 42);

        game.batch.end();

        // Tiklama Kontrolu
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector3 m = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(m);

            if (m.x > btnX && m.x < btnX + btnW && m.y > btnY && m.y < btnY + btnH) {
                isTutorialOpen = false;
            }
        }
    }

    // button durumları update
    private void updateButtonState() {
        updateOne(btnCivi, 70, texBtnCivi, texBtnCiviDisabled);
        updateOne(btnAnahtar, 50, texBtnAnahtar, texBtnAnahtarDisabled);
        updateOne(btnYag, 75, texBtnYag, texBtnYagDisabled);
    }

    private void updateOne(ImageButton btn, int cost, Texture active, Texture inactive) {
        if (scrap < cost) {
            btn.setColor(Color.GRAY);
            btn.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(inactive));
        } else {
            btn.setColor(Color.WHITE);
            btn.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(active));
        }
    }

    // girdiler
    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R))
            restartGame();
        if (Gdx.input.isKeyJustPressed(Input.Keys.P))
            isPaused = !isPaused;

        if (isPaused || isGameOver)
            return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            gameSpeed += 0.5f;
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
            gameSpeed = Math.max(0.5f, gameSpeed - 0.5f);

        if (selectedTower != null && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
            placeTower();
    }

    // kule koyma
    private void placeTower() {

        if (ghostSnap == null)
            return;

        kule t = null;

        if (selectedTower.equals("Civi") && scrap >= 70)
            t = new CiviAgAtar(ghostSnap.x, ghostSnap.y);
        else if (selectedTower.equals("Anahtar") && scrap >= 50)
            t = new AnahtarMakinesi(ghostSnap.x, ghostSnap.y);
        else if (selectedTower.equals("Yag") && scrap >= 75)
            t = new YagSizdirici(ghostSnap.x, ghostSnap.y);

        if (t == null)
            return;

        scrap -= t.getCost();
        towers.add(t);

        CombatLog.waveStart(wave, t.getClass().getSimpleName(), 1);

        towerSpots.removeValue(ghostSnap, true);

        selectedTower = null;
        ghostSnap = null;
    }

    // update
    private void update(float delta) {

        if (garageHp <= 0) {
            isGameOver = true;
            return;
        }

        if (isWaveActive && spawnQueue.size > 0) {

            dusman last = enemies.size > 0 ? enemies.peek() : null;

            boolean canSpawn = true;

            // spawn delay
            if (last != null) {
                float dist = Vector2.dst(last.getX(), last.getY(), path.get(0).x, path.get(0).y);
                if (dist < 120) {
                    canSpawn = false;
                }
            }

            if (canSpawn && System.currentTimeMillis() - lastSpawn > SPAWN_INTERVAL) {

                dusman e = spawnQueue.removeIndex(0);
                e.setPath(path);
                enemies.add(e);

                CombatLog.enemySpawn(e.getID(), e.getClass().getSimpleName(),
                        e.getHp(), e.getMaxHp(), e.getArmor());

                lastSpawn = System.currentTimeMillis();
            }
        }

        updateEnemies(delta);
        updateTowers();
        updateProjectiles();

        if (isWaveActive && spawnQueue.size == 0 && enemies.size == 0) {
            CombatLog.waveEnd(wave);
            isWaveActive = false;
        }
        // kaybetme kosulu
        if (garageHp <= 0 && !isGameOver) {
            isGameOver = true;
            return;
        }
    }

    // dusman updateleri
    private void updateEnemies(float delta) {

        Iterator<dusman> it = enemies.iterator();

        while (it.hasNext()) {
            dusman e = it.next();

            e.move(delta);

            // Zombi (HP<=0) temizligi
            if (e.isDead()) {
                scrap += e.getReward();
                totalEnemiesKilled++;

                CombatLog.death(
                        e.getClass().getSimpleName() + "-" + e.getID(),
                        e.getReward(),
                        scrap);

                it.remove();
                continue;
            }

            if (e.hasReachedEnd()) {

                garageHp -= e.getDamage();

                CombatLog.reachedBase(
                        e.getClass().getSimpleName() + "-" + e.getID(),
                        garageHp,
                        e.getDamage());

                it.remove();
            }
        }
    }

    // kule updateleri
    private void updateTowers() {

        final float BASE_X = 1110;
        final float BASE_Y = 450;

        for (kule t : towers) {

            if (!t.canAttack())
                continue;

            dusman target = null;
            float best = Float.MAX_VALUE;

            for (dusman e : enemies) {

                if (!t.isInRange(e))
                    continue;

                float dist = Vector2.dst(e.getX(), e.getY(), BASE_X, BASE_Y);
                if (dist < best) {
                    best = dist;
                    target = e;
                }
            }

            if (target != null) {

                // yag dokucu ucan hedefi vuramama kontrolu
                if (t instanceof YagSizdirici && target.isFlyingEnemy())
                    continue;

                CombatLog.towerTarget(
                        t.getClass().getSimpleName(),
                        String.valueOf(t.getId()),
                        target.getClass().getSimpleName() + "-" + target.getID());

                projectiles.add(t.attack(target));
            }

        }
    }

    // mermi updateleri
    private void updateProjectiles() {

        Iterator<Mermi> it = projectiles.iterator();

        while (it.hasNext()) {

            Mermi m = it.next();
            m.update(Gdx.graphics.getDeltaTime());

            if (!m.isActive()) {

                if (m.hasHit()) {
                    dusman e = m.getTarget();

                    int before = e.getHp();
                    m.getOwner().onHit(e, enemies);
                    int after = e.getHp();

                    int net = before - after;

                    CombatLog.damageDetail(
                            m.getOwner().getClass().getSimpleName() + "-" + m.getOwner().getId(),
                            m.getOwner().getDamage(),
                            e.getArmor(),
                            net,
                            e.getHp(),
                            e.getMaxHp());

                    if (e.isDead()) {
                        scrap += e.getReward();
                        totalEnemiesKilled++; // Sayac artir

                        CombatLog.death(
                                e.getClass().getSimpleName() + "-" + e.getID(),
                                e.getReward(),
                                scrap);

                        enemies.removeValue(e, true);
                    }
                }

                it.remove();
            }
        }
    }

    // draw
    private void drawObjects() {

        game.batch.begin();
        for (kule t : towers)
            t.render(game.batch);
        for (dusman e : enemies)
            e.render(game.batch);
        for (Mermi m : projectiles)
            m.render(game.batch);
        game.batch.end();

        if (selectedTower != null)
            drawGhost();
    }

    private void drawTopPanel() {
        game.batch.begin();

        // Panel görüntüsü
        game.batch.draw(panelTexture, 20, 530, 420, 180);

        // Yazılar
        game.font.setColor(Color.GREEN);
        game.font.getData().setScale(1.1f);

        game.font.draw(game.batch, "" + garageHp, 120, 601);
        game.font.draw(game.batch, "" + scrap, 216, 599);
        game.font.draw(game.batch, "" + wave, 380, 601);

        game.font.getData().setScale(1f);

        game.batch.end();
    }

    private void drawGhost() {

        Vector3 m = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(m);

        ghostSnap = getClosestSpot(m.x, m.y);
        if (ghostSnap == null)
            return;

        float x = ghostSnap.x;
        float y = ghostSnap.y;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.25f);
        shapeRenderer.circle(x, y, ghostRange);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        Texture tex = selectedTower.equals("Civi") ? ghostCivi
                : selectedTower.equals("Anahtar") ? ghostAnahtar : ghostYag;

        game.batch.begin();
        game.batch.setColor(0.6f, 0.6f, 0.6f, 0.5f);
        game.batch.draw(tex, x - 60, y - 55, 120, 110);
        game.batch.setColor(Color.WHITE);
        game.batch.end();
    }

    private void drawEndScreen() {

        // arka plan koyu gri
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.85f);
        shapeRenderer.rect(0, 0, 1280, 720);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        game.batch.begin();

        // kazanma/kaybetme
        Texture resultTex = (garageHp > 0) ? texWin : texLose;

        float rw = resultTex.getWidth();
        float rh = resultTex.getHeight();
        float rx = (1280 - rw) / 2f;
        float ry = (720 - rh) / 2f + 100;

        game.batch.draw(resultTex, rx, ry);

        // ISTATISTIKLER
        game.font.setColor(Color.YELLOW);
        game.font.getData().setScale(1.5f);
        game.font.draw(game.batch, "Gecilen Dalga: " + wave, 1280 / 2f - 100, ry - 50, 200, Align.center, false);

        game.font.setColor(Color.CYAN);
        game.font.draw(game.batch, "Oldurulen Dusman: " + totalEnemiesKilled, 1280 / 2f - 150, ry - 100, 300,
                Align.center, false);
        game.font.getData().setScale(1f);

        // anamenu buton
        float btnW = 300;
        float btnH = 110;

        float btnX = (1280 - btnW) / 2f;
        float btnY = ry - 250; // Biraz daha asagi aldim

        // mouse hover
        Vector3 mouse = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mouse);
        float mx = mouse.x;
        float my = mouse.y;

        boolean hover = (mx > btnX && mx < btnX + btnW &&
                my > btnY && my < btnY + btnH);

        float scale = hover ? 1.10f : 1f;

        float drawW = btnW * scale;
        float drawH = btnH * scale;

        float drawX = btnX - (drawW - btnW) / 2f;
        float drawY = btnY - (drawH - btnH) / 2f;

        game.batch.draw(texMenuBtn, drawX, drawY, drawW, drawH);

        game.batch.end();

        // anamenu donus
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (hover) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        }
    }

    private Vector2 getClosestSpot(float x, float y) {
        Vector2 best = null;
        float dmin = 40;

        for (Vector2 s : towerSpots) {
            float d = Vector2.dst(x, y, s.x, s.y);
            if (d < dmin) {
                dmin = d;
                best = s;
            }
        }

        return best;
    }

    // ui
    private void drawUI() {

        game.batch.begin();

        if (selectedTower != null)
            game.font.draw(game.batch, "Secili: " + selectedTower, 1000, 670);

        if (tooltipText != null) {
            drawTooltip(game.batch, tooltipText);
        }

        game.batch.end();
    }

    private void drawTooltip(com.badlogic.gdx.graphics.g2d.SpriteBatch batch, String text) {
        float x = Gdx.input.getX() + 15;
        float y = Gdx.graphics.getHeight() - Gdx.input.getY() - 15;

        // Basit siyah arka plan (fontun olcusunu almadan sabit yapiyoruz simdilik)
        // Daha karmasik yapi icin GlyphLayout gerekir ama bu is gorur
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.8f);
        shapeRenderer.rect(x - 5, y - 90, 220, 100); // Tahmini kutu boyutu
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
        game.font.setColor(Color.WHITE);
        game.font.getData().setScale(0.8f);
        game.font.draw(batch, text, x, y);
        game.font.getData().setScale(1f);
    }

    private void restartGame() {
        scrap = 200;
        garageHp = 100;
        wave = 0;
        totalEnemiesKilled = 0;
        isWaveActive = false;
        isGameOver = false;

        enemies.clear();
        towers.clear();
        projectiles.clear();
        spawnQueue.clear();

        initTowerSpots();

        CombatLog.resetLog();
    }

    private void startNextWave() {

        if (isWaveActive)
            return;

        wave++;
        spawnQueue.clear();

        CombatLog.waveStart(wave, "Dalga " + wave, 0);

        // 1. Tanitim: Motorcu
        if (wave == 1) {
            showTutorial("YENI DUSMAN: MOTORCU",
                    "Hizli ve sayica coklar.\nEn temel birim.\nZirhlari yok, Civi Agi ile yavaslatilabilirler.",
                    texMotorcu);
            spawnMultiple(new MotorluCapulcu(), 3);
        }
        // 3. Tanitim: Kamyon
        else if (wave == 3) {
            showTutorial("YENI DUSMAN: ZIRHLI KAMYON",
                    "Yavas ama cok dayanikli.\nZirhi sayesinde hafif saldirilardan az etkilenir.\nYuksek hasarli kuleler kur!",
                    texKamyon);
            spawnQueue.add(new ZirhliKamyon());
            spawnMultiple(new MotorluCapulcu(), 2);
        }
        // 7. Tanitim: Ucak
        else if (wave == 7) {
            showTutorial("YENI DUSMAN: GOZCU UCAGI",
                    "Havadan gelir ve yolu takip eder.\nYag Sizdirici ve Mayinlar ona islemez!\nHava savunmasi sart.",
                    texUcak);
            spawnQueue.add(new GozcuUcagi());
            spawnMultiple(new MotorluCapulcu(), 3);
        }
        // Her 5. Dalga: BOSS
        else if (wave % 5 == 0) {
            dusman boss;
            // 5. dalgada motorcu boss, sonrakilerde kamyon boss
            if (wave == 5)
                boss = new MotorluCapulcu();
            else
                boss = new ZirhliKamyon();

            boss.makeBoss();
            spawnQueue.add(boss);

            // Boss yanina koruma (Butce: dalga*10)
            fillWaveWithEnemies(wave * 10);
        }
        // NORMAL DALGALAR (2, 4, 6, 8...)
        else {
            // Normal dalga butcesi
            int budget = wave * 15 + 20;
            fillWaveWithEnemies(budget);
        }

        isWaveActive = true;
    }

    // Butceye gore o an acilmis dusmanlardan rastgele ekler
    private void fillWaveWithEnemies(int budget) {
        java.util.Random rand = new java.util.Random();

        while (budget > 0) {
            // Hangi dusmanlar acik?
            boolean kamyonAcik = (wave >= 3);
            boolean ucakAcik = (wave >= 7);

            int r = rand.nextInt(100);

            // Secim mantigi (Pahali birimler daha az gelir)
            if (ucakAcik && r < 20) { // %20 Ucak (Varsa)
                spawnQueue.add(new GozcuUcagi());
                budget -= 15;
            } else if (kamyonAcik && r < 55) { // %35 Kamyon
                spawnQueue.add(new ZirhliKamyon());
                budget -= 20;
            } else { // Geri kalan Motorcu
                spawnQueue.add(new MotorluCapulcu());
                budget -= 10;
            }
        }
    }

    private void spawnMultiple(dusman sample, int count) {
        for (int i = 0; i < count; i++) {
            if (sample instanceof MotorluCapulcu)
                spawnQueue.add(new MotorluCapulcu());
            else if (sample instanceof ZirhliKamyon)
                spawnQueue.add(new ZirhliKamyon());
            else if (sample instanceof GozcuUcagi)
                spawnQueue.add(new GozcuUcagi());
        }
    }

    @Override
    public void dispose() {
        mapTexture.dispose();
        towerSlotTexture.dispose();
        uiStage.dispose();
        shapeRenderer.dispose();
        panelTexture.dispose();

        ghostCivi.dispose();
        ghostAnahtar.dispose();
        ghostYag.dispose();
        texWin.dispose();
        texLose.dispose();
        texBtnCivi.dispose();
        texBtnAnahtar.dispose();
        texBtnYag.dispose();
        texMenuBtn.dispose();

        if (texMotorcu != null)
            texMotorcu.dispose();
        if (texKamyon != null)
            texKamyon.dispose();
        if (texUcak != null)
            texUcak.dispose();
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int w, int h) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}
