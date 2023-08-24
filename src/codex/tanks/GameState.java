/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks;

import codex.j3map.J3map;
import codex.tanks.components.Alive;
import codex.tanks.components.Brain;
import codex.tanks.components.BulletReaction;
import codex.tanks.components.CollisionShape;
import codex.tanks.components.EntityTransform;
import codex.tanks.components.Physics;
import codex.tanks.components.Team;
import codex.tanks.components.TransformMode;
import codex.tanks.factory.AIFactory;
import codex.tanks.factory.ModelFactory;
import codex.tanks.systems.Visual;
import codex.tanks.systems.VisualState;
import codex.tanks.util.ESAppState;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
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
                new Visual(ModelFactory.TANK),
                new Physics(),
                new EntityTransform().setTranslation(0f, 0f, -7f),
                new TransformMode(-1, 0, 0),
                new CollisionShape("hitbox"),
                BulletReaction.DIE,
                new Team(0),
                new Alive());
        Tank.applyProperties(ed, plr, playerSource);
        player = new PlayerAppState(plr);
        getStateManager().attach(player);
        
        for (int i = 0; i < 0; i++) {
            var enemy = ed.createEntity();
            ed.setComponents(enemy,
                    new Visual(ModelFactory.TANK),
                    new Physics(),
                    new EntityTransform().setTranslation(i*3, 0f, 7f),
                    new TransformMode(-1, 0, 0),
                    new CollisionShape("hitbox"),
                    new Team(1),
                    new Brain(AIFactory.BLACK));
            Tank.applyProperties(ed, plr, playerSource);
        }
        
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
    
    private ParticleEmitter createBulletSmoke(Bullet b) {
        var smoke = new ParticleEmitter("bullet-smoke", ParticleMesh.Type.Triangle, 10);
//        var mat = new Material(getApplication().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
//        mat.setTexture("Texture", getApplication().getAssetManager().loadTexture("Effects/Smoke.png"));
//        smoke.setMaterial(mat);
//        smoke.setImagesX(15); smoke.setImagesY(1);
//        smoke.setSelectRandomImage(true);
//        smoke.setHighLife(.5f);
//        smoke.setLowLife(smoke.getHighLife());
//        smoke.setStartSize(.2f);
//        smoke.setEndSize(.7f);
//        smoke.setStartColor(new ColorRGBA(.1f, .1f, .1f, 1f));
//        smoke.setEndColor(new ColorRGBA(.1f, .1f, .1f, 0f));
//        smoke.setGravity(0f, 0f, 0f);
//        smoke.setParticlesPerSec(b.getBulletInfo().getSpeed()*2);
//        smoke.setNumParticles(10);
//        var control = new BasicEmitterControl(scene, b.getEmitterNode());
//        smoke.addControl(control);
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
