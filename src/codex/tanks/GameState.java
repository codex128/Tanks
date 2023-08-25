/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks;

import codex.j3map.J3map;
import codex.tanks.ai.AvoidBullets;
import codex.tanks.ai.BasicShooting;
import codex.tanks.ai.DefensivePoints;
import codex.tanks.ai.DirectAim;
import codex.tanks.components.*;
import codex.tanks.factory.ModelFactory;
import codex.tanks.systems.VisualState;
import codex.tanks.util.ESAppState;
import codex.tanks.util.GameUtils;
import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowRenderer;

/**
 *
 * @author gary
 */
public class GameState extends ESAppState {
    
    private BulletAppState bulletapp;
    private PlayerAppState player;
    //private LinkedList<Tank> tanks = new LinkedList<>();
    //private LinkedList<Bullet> bullets = new LinkedList<>();
    //private LinkedList<CollisionShape> collisionShapes = new LinkedList<>();
    private DirectionalLight light;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        
        bulletapp = getState(BulletAppState.class, true);
        
        Spatial floor = app.getAssetManager().loadModel("Models/floor.j3o");
        floor.setLocalTranslation(0f, -1f, 0f);
        floor.setShadowMode(RenderQueue.ShadowMode.Receive);
        Material floorMat = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        floorMat.setBoolean("UseMaterialColors", true);
        floorMat.setColor("Diffuse", ColorRGBA.Green);
        floor.setMaterial(floorMat);
        rootNode.attachChild(floor);
        RigidBodyControl floorPhys = new RigidBodyControl(0f);
        floor.addControl(floorPhys);
        getPhysicsSpace().add(floorPhys);
        
//        Node tank = (Node)app.getAssetManager().loadModel("Models/tank.j3o");
//        Material pmat = new Material(app.getAssetManager(), "Materials/tank.j3md");
//        pmat.setTexture("DiffuseMap", app.getAssetManager().loadTexture(new TextureKey("Models/tankTexture.png", false)));
//        pmat.setColor("MainColor", ColorRGBA.Blue);
//        pmat.setColor("SecondaryColor", new ColorRGBA(.3f, .5f, 1f, 1f));
//        tank.setMaterial(pmat);
//        tank.setShadowMode(RenderQueue.ShadowMode.Cast);
        J3map playerSource = (J3map)app.getAssetManager().loadAsset("Properties/player.j3map");
//        tank.setLocalTranslation(0f, 0f, -7f);
//        scene.attachChild(tank);
//        TankModel pmodel = new TankModel((J3map)app.getAssetManager().loadAsset("Properties/player.j3map"));
//        Tank ptank = new Tank(tank, pmat, pmodel, 0);
//        addTank(ptank);
//        RigidBodyControl rigidbody = ptank.initPhysics();
//        getState(BulletAppState.class).getPhysicsSpace().add(rigidbody);
//        player = new PlayerAppState(this, ptank);
//        getStateManager().attach(player);
        
        var plr = ed.createEntity();
        ed.setComponents(plr,
            new GameObject("tank"),
            new Visual(ModelFactory.TANK),
            new Physics(),
            new EntityTransform().setTranslation(0f, 0f, -7f),
            new TransformMode(-1, 0, 0),
            new CollisionShape("hitbox"),
            new ContactReaction(ContactReaction.DIE),
            new Team(0),
            new Alive()
        );
        Tank.applyProperties(ed, plr, playerSource);
        player = new PlayerAppState(plr);
        getStateManager().attach(player);
        
        J3map enemySource = (J3map)assetManager.loadAsset("Properties/enemy.j3map");
        for (int i = 0; i < 2; i++) {
            var enemy = ed.createEntity();
            ed.setComponents(enemy,
                new GameObject("tank"),
                new Visual(ModelFactory.TANK),
                new Physics(),
                new EntityTransform().setTranslation(i*3, 0f, 7f),
                new TransformMode(-1, 0, 0),
                new CollisionShape("hitbox"),
                new ContactReaction(ContactReaction.DIE),
                new Team(1),
                new Alive(),
                new Brain(
                    new AvoidBullets(5f),
                    //new Lookout(FastMath.PI*0.01f),
                    new DirectAim(),
                    new BasicShooting(0f, .01f),
                    //new Wander(2.0f, 0.02f, 2f, .3f)
                    new DefensivePoints(10f, .25f)
                )
            );
            Tank.applyProperties(ed, enemy, enemySource.getJ3map("tank"));
        }
        
        float r = 20f;
        createWall(new Vector3f(-r, 0f, 0f), 0f, new Vector3f(1f, 1f, r));
        createWall(new Vector3f(r, 0f, 0f), 0f, new Vector3f(1f, 1f, r));
        createWall(new Vector3f(0f, 0f, -r), 0f, new Vector3f(r, 1f, 1f));
        createWall(new Vector3f(0f, 0f, r), 0f, new Vector3f(r, 1f, 1f));
        createWall(new Vector3f(0f, 0f, 0f), FastMath.PI/4, new Vector3f(3f, 1f, 3f));
        
        light = new DirectionalLight(new Vector3f(1f, -1f, 1f));
        rootNode.addLight(light);
        var drsr = new DirectionalLightShadowRenderer(app.getAssetManager(), 4096, 2);
        drsr.setLight(light);
        app.getViewPort().addProcessor(drsr);
        
        var fpp = new FilterPostProcessor(app.getAssetManager());
        var ssao = new SSAOFilter();
        ssao.setIntensity(5f);
        fpp.addFilter(ssao);
        var bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
        fpp.addFilter(bloom);
        app.getViewPort().addProcessor(fpp);
        
    }
    @Override
    protected void cleanup(Application app) {
        getStateManager().detach(player);
        //getStateManager().detach(enemy);
    }
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    
    private void createWall(Vector3f location, float angle, Vector3f size) {
        var wall = ed.createEntity();
        ed.setComponents(wall,
            new Visual(),
            new Physics(0f),
            new EntityTransform()
                .setTranslation(location)
                .setRotation(angle, Vector3f.UNIT_Y),
            new TransformMode(1, 1, 0),
            new CollisionShape(),
            new ContactReaction(ContactReaction.RICOCHET));
        var geometry = GameUtils.createDebugGeometry(assetManager, new ColorRGBA(.7f, .6f, .2f, 1f), size);
        geometry.setLocalScale(size);
        getState(VisualState.class).link(wall, geometry, true);
    }
    private ParticleEmitter createBulletSmoke(Bullet b) {
        var smoke = new ParticleEmitter("bullet-smoke", ParticleMesh.Type.Triangle, 10);
        var mat = new Material(getApplication().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", getApplication().getAssetManager().loadTexture("Effects/Smoke.png"));
        smoke.setMaterial(mat);
        smoke.setImagesX(15); smoke.setImagesY(1);
        smoke.setSelectRandomImage(true);
        smoke.setHighLife(.5f);
        smoke.setLowLife(smoke.getHighLife());
        smoke.setStartSize(.2f);
        smoke.setEndSize(.7f);
        smoke.setStartColor(new ColorRGBA(.1f, .1f, .1f, 1f));
        smoke.setEndColor(new ColorRGBA(.1f, .1f, .1f, 0f));
        smoke.setGravity(0f, 0f, 0f);
        //smoke.setParticlesPerSec(b.getBulletInfo().getSpeed()*2);
        smoke.setNumParticles(10);
        return smoke;
    }
    
    public PlayerAppState getPlayerState() {
        return player;
    }
    public PhysicsSpace getPhysicsSpace() {
        return bulletapp.getPhysicsSpace();
    }
    
    public boolean isPlayerTank(Tank t) {
        return player.getTank() == t;
    }
    
}
