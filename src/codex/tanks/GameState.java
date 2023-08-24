/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks;

import codex.tanks.util.GameUtils;
import codex.boost.GameAppState;
import codex.j3map.J3map;
import codex.tanks.ai.AITank;
import codex.tanks.ai.BulletAlert;
import codex.tanks.ai.DefensivePoints;
import codex.tanks.ai.DirectShot;
import codex.tanks.ai.GameStartWait;
import codex.tanks.effects.BasicEmitterControl;
import codex.tanks.util.PointLightNode;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResult;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author gary
 */
public class GameState extends GameAppState {
    
    private Node scene;
    private BulletAppState bulletapp;
    private PlayerAppState player;
    private LinkedList<Tank> tanks = new LinkedList<>();
    private LinkedList<Bullet> bullets = new LinkedList<>();
    private LinkedList<CollisionShape> collisionShapes = new LinkedList<>();
    private DirectionalLight light;
    
    @Override
    protected void init(Application app) {
        
        scene = new Node("level-scene");
        bulletapp = getState(BulletAppState.class, true);
        
        Spatial floor = app.getAssetManager().loadModel("Models/floor.j3o");
        floor.setLocalTranslation(0f, -1f, 0f);
        floor.setShadowMode(RenderQueue.ShadowMode.Receive);
        Material floorMat = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        floorMat.setBoolean("UseMaterialColors", true);
        floorMat.setColor("Diffuse", ColorRGBA.Green);
        floor.setMaterial(floorMat);
        scene.attachChild(floor);
        RigidBodyControl floorPhys = new RigidBodyControl(0f);
        floor.addControl(floorPhys);
        getPhysicsSpace().add(floorPhys);
        
        Node tank = (Node)app.getAssetManager().loadModel("Models/tank.j3o");
        Material pmat = new Material(app.getAssetManager(), "Materials/tank.j3md");
        pmat.setTexture("DiffuseMap", app.getAssetManager().loadTexture(new TextureKey("Models/tankTexture.png", false)));
        pmat.setColor("MainColor", ColorRGBA.Blue);
        pmat.setColor("SecondaryColor", new ColorRGBA(.3f, .5f, 1f, 1f));
        tank.setMaterial(pmat);
        tank.setShadowMode(RenderQueue.ShadowMode.Cast);
        tank.setLocalTranslation(0f, 0f, -7f);
        scene.attachChild(tank);
        TankModel pmodel = new TankModel((J3map)app.getAssetManager().loadAsset("Properties/player.j3map"));
        Tank ptank = new Tank(tank, pmat, pmodel, 0);
        addTank(ptank);
        RigidBodyControl rigidbody = ptank.initPhysics();
        getState(BulletAppState.class).getPhysicsSpace().add(rigidbody);
        player = new PlayerAppState(this, ptank);
        getStateManager().attach(player);
        
        J3map esource = (J3map)app.getAssetManager().loadAsset("Properties/enemy.j3map");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 1; j++) {
                Node enemyTankSpatial = (Node)app.getAssetManager().loadModel("Models/tank.j3o");
                Material emat = pmat.clone();
                emat.setColor("MainColor", new ColorRGBA(.03f, .03f, .03f, 03f));
                emat.setColor("SecondaryColor", new ColorRGBA(.2f, .2f, .2f, 1f));
                enemyTankSpatial.setMaterial(emat);
                enemyTankSpatial.setShadowMode(RenderQueue.ShadowMode.Cast);
                enemyTankSpatial.setLocalTranslation(-10f+i*3, 0f, 7f+j*3);
                scene.attachChild(enemyTankSpatial);
                AITank enemy = new AITank(enemyTankSpatial, emat, new TankModel(esource.getJ3map("tank")), 1, this);
                enemy.addAlgorithm(new GameStartWait(1f));
                enemy.addAlgorithm(new BulletAlert(esource.getJ3map("bulletAlert")));
                enemy.addAlgorithm(new DefensivePoints(esource.getJ3map("defensivePoints")));
                enemy.addAlgorithm(new DirectShot(esource.getJ3map("directShot")));
                addTank(enemy);
                RigidBodyControl enemyPhys = enemy.initPhysics();
                getPhysicsSpace().add(enemyPhys);
            }
        }
        
        light = new DirectionalLight(new Vector3f(1f, -1f, 1f));
        scene.addLight(light);
        var drsr = new DirectionalLightShadowRenderer(app.getAssetManager(), 4096, 2);
        drsr.setLight(light);
        app.getViewPort().addProcessor(drsr);
        
        float s = 20f;
        makeWall(new Vector3f(-s, -.1f, 0f), new Vector3f(1f, 1f, s));
        makeWall(new Vector3f(s, 0f, 0f), new Vector3f(1f, 1f, s));
        makeWall(new Vector3f(0f, 0f, -s), new Vector3f(s, 1f, 1f));
        makeWall(new Vector3f(0f, 0f, s), new Vector3f(s, 1f, 1f));
        makeWall(new Vector3f(), new Vector3f(2f, 1f, 1f));
        makeWall(new Vector3f(-12f, 0f, 0f), new Vector3f(4f, 1f, 1f));
        makeWall(new Vector3f(12f, 0f, 0f), new Vector3f(4f, 1f, 1f));
        
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
    protected void onEnable() {
        ((SimpleApplication)getApplication()).getRootNode().attachChild(scene);
    }
    @Override
    protected void onDisable() {
        scene.removeFromParent();
    }
    @Override
    public void update(float tpf) {
        //light.setDirection(getApplication().getCamera().getDirection());
        for (Iterator<Tank> i = tanks.iterator(); i.hasNext();) {
            Tank t = i.next();
            t.update(tpf);
            if (!t.isAlive()) {
                // explode
                t.getRootSpatial().removeFromParent();
                getPhysicsSpace().remove(t.getPhysics());
                collisionShapes.remove(t);
                i.remove();
                if (t == player.getTank()) {
                    // game over
                }
            }
        }
        for (Iterator<Bullet> i = bullets.iterator(); i.hasNext();) {
            Bullet b = i.next();
            b.update(tpf);
            b.move(tpf);
            if (!b.getBulletInfo().isAlive()) {
                b.getRoot().removeFromParent();
                collisionShapes.remove(b);
                i.remove();
                continue;
            }
            CollisionResult closest = b.raycast(collisionShapes, tpf);
            if (closest != null) {
                CollisionShape h = GameUtils.fetchGeometryOwner(closest.getGeometry());
                if (h != null) {
                    h.onHit(b, closest);
                }
            }
        }
    }
    
    private void makeWall(Vector3f center) {
        makeWall(center, Vector3f.UNIT_XYZ);
    }
    private void makeWall(Vector3f center, Vector3f size) {
        Spatial wall = getApplication().getAssetManager().loadModel("Models/wall.j3o");
        wall.setShadowMode(RenderQueue.ShadowMode.Cast);
        wall.setLocalTranslation(center);
        wall.setLocalScale(size);
        Material mat = new Material(getApplication().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", new ColorRGBA(.6f, .6f, .1f, 1f));
        wall.setMaterial(mat);
        Wall w = new Wall(wall);
        addCollisionShape(w);
        scene.attachChild(wall);
        RigidBodyControl wallPhys = new RigidBodyControl(0f);
        wall.addControl(wallPhys);
        getPhysicsSpace().add(wallPhys);
    }
    
    public void addTank(Tank t) {
        tanks.add(t);
        addCollisionShape(t);
    }
    public void addBullet(BulletInfo info) {
        if (info == null) return;
        Node spatial = createBulletSpatial(info);
        Bullet b = new Bullet(spatial, info);
        scene.attachChild(spatial);        
        scene.attachChild(createBulletSmoke(b));
        bullets.add(b);
        addCollisionShape(b);
    }
    public void addMissile(BulletInfo info) {
        if (info == null) return;
        Node spatial = createBulletSpatial(info);
        Missile m = createMissile(spatial, info);
        scene.attachChild(spatial);
        scene.attachChild(createBulletSmoke(m));
        bullets.add(m);
        addCollisionShape(m);
    }
    public void addCollisionShape(CollisionShape h) {
        collisionShapes.add(h);
        GameUtils.assignGeometryOwner(h.getCollisionShape(), h);
    }
    
    private Node createBulletSpatial(BulletInfo info) {
        Node spatial = (Node)getApplication().getAssetManager().loadModel("Models/bullet.j3o");
        spatial.setShadowMode(RenderQueue.ShadowMode.Off);
        spatial.setLocalTranslation(info.start);
        spatial.setLocalScale(.2f);
        return spatial;
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
        smoke.setParticlesPerSec(b.getBulletInfo().getSpeed()*2);
        smoke.setNumParticles(10);
        var control = new BasicEmitterControl(scene, b.getEmitterNode());
        smoke.addControl(control);
        return smoke;
    }
    private Missile createMissile(Node spatial, BulletInfo info) {
        var flameMat = new Material(assetManager, "Materials/flame.j3md");
        flameMat.setTexture("ColorMap", assetManager.loadTexture(new TextureKey("Effects/flame.png", false)));
        flameMat.setFloat("Seed", 53.234f);
        flameMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        flameMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        flameMat.setTransparent(true);
        var flame = assetManager.loadModel("Effects/flame.j3o");
        flame.setLocalScale(3f, 3f, 4f);
        flame.setQueueBucket(RenderQueue.Bucket.Transparent);
        flame.setMaterial(flameMat);
        var pl = new PointLight();
        pl.setRadius(100f);
        pl.setColor(ColorRGBA.Orange);
        var light = new PointLightNode(pl, scene);
        var missile = new Missile(spatial, flameMat, info);
        missile.getEmitterNode().attachChild(flame);
        //missile.getEmitterNode().attachChild(light);
        return missile;
    }
    
    public Collection<Tank> getTanks() {
        return tanks;
    }
    public Collection<Bullet> getBullets() {
        return bullets;
    }
    public Collection<CollisionShape> getCollisionShapes() {
        return collisionShapes;
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
