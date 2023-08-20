/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks;

import codex.j3map.J3map;
import codex.tanks.ai.AITank;
import codex.tanks.ai.BulletAlert;
import codex.tanks.ai.DefensivePoints;
import codex.tanks.ai.DirectShot;
import codex.tanks.ai.GameStartWait;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResult;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author gary
 */
public class GameState extends BaseAppState {
    
    private Node scene;
    private BulletAppState bulletapp;
    private PlayerAppState player;
    private LinkedList<Tank> tanks = new LinkedList<>();
    private LinkedList<Bullet> bullets = new LinkedList<>();
    private LinkedList<CollisionShape> collisionShapes = new LinkedList<>();
    
    @Override
    protected void initialize(Application app) {
        
        scene = new Node("level-scene");
        bulletapp = getState(BulletAppState.class, true);
        
        Spatial floor = app.getAssetManager().loadModel("Models/floor.j3o");
        floor.setLocalTranslation(0f, -1f, 0f);
        Material floorMat = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        floorMat.setBoolean("UseMaterialColors", true);
        floorMat.setColor("Diffuse", ColorRGBA.Green);
        floor.setMaterial(floorMat);
        scene.attachChild(floor);
        RigidBodyControl floorPhys = new RigidBodyControl(0f);
        floor.addControl(floorPhys);
        getPhysicsSpace().add(floorPhys);
        
        Node tank = (Node)app.getAssetManager().loadModel("Models/tank.j3o");
        tank.setLocalTranslation(0f, 0f, -7f);
        scene.attachChild(tank);
        TankModel pmodel = new TankModel((J3map)app.getAssetManager().loadAsset("Properties/player.j3map"));
        Tank ptank = new Tank(tank, pmodel, 0);
        addTank(ptank);
        RigidBodyControl rigidbody = ptank.initPhysics();
        getState(BulletAppState.class).getPhysicsSpace().add(rigidbody);
        player = new PlayerAppState(this, ptank);
        getStateManager().attach(player);
        
        J3map esource = (J3map)app.getAssetManager().loadAsset("Properties/enemy.j3map");
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 1; j++) {
                Node enemyTankSpatial = (Node)app.getAssetManager().loadModel("Models/tank.j3o");
                enemyTankSpatial.setLocalTranslation(-10f+i*3, 0f, 7f);
                scene.attachChild(enemyTankSpatial);
                AITank enemy = new AITank(enemyTankSpatial, new TankModel(esource.getJ3map("tank")), 1, this);
                enemy.addAlgorithm(new GameStartWait(1f));
                enemy.addAlgorithm(new BulletAlert(esource.getJ3map("bulletAlert")));
                enemy.addAlgorithm(new DefensivePoints(esource.getJ3map("defensivePoints")));
                enemy.addAlgorithm(new DirectShot(esource.getJ3map("directShot")));
                addTank(enemy);
                RigidBodyControl enemyPhys = enemy.initPhysics();
                getPhysicsSpace().add(enemyPhys);
            }
        }
        
        DirectionalLight light = new DirectionalLight(new Vector3f(1f, -1f, 1f));
        scene.addLight(light);
        
        float s = 20f;
        makeWall(new Vector3f(-s, 0f, 0f), new Vector3f(1f, 1f, s));
        makeWall(new Vector3f(s, 0f, 0f), new Vector3f(1f, 1f, s));
        makeWall(new Vector3f(0f, 0f, -s), new Vector3f(s, 1f, 1f));
        makeWall(new Vector3f(0f, 0f, s), new Vector3f(s, 1f, 1f));
        makeWall(new Vector3f(), new Vector3f(2f, 1f, 1f));
        makeWall(new Vector3f(-12f, 0f, 0f), new Vector3f(4f, 1f, 1f));
        makeWall(new Vector3f(12f, 0f, 0f), new Vector3f(4f, 1f, 1f));
        
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
        wall.setLocalTranslation(center);
        wall.setLocalScale(size);
        Material mat = new Material(getApplication().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", ColorRGBA.Blue);
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
        Node spatial = (Node)getApplication().getAssetManager().loadModel("Models/bullet.j3o");
        spatial.setLocalTranslation(info.start);
        spatial.setLocalScale(.2f);
//        ParticleEmitter debrisEffect = new ParticleEmitter("Debris", ParticleMesh.Type.Triangle, 10);
//        Material smoke = new Material(getApplication().getAssetManager(), "Common/MatDefs/Misc/Particle.j3md");
//        smoke.setTexture("Texture", getApplication().getAssetManager().loadTexture("Effects/Explosion/Debris.png"));
//        debrisEffect.setMaterial(smoke);
//        debrisEffect.setImagesX(3); debrisEffect.setImagesY(3); // 3x3 texture animation
//        debrisEffect.setRotateSpeed(4);
//        debrisEffect.setSelectRandomImage(true);
//        debrisEffect.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 4, 0));
//        debrisEffect.setStartColor(new ColorRGBA(1f, 1f, 1f, 1f));
//        debrisEffect.setGravity(0f,6f,0f);
//        debrisEffect.getParticleInfluencer().setVelocityVariation(.60f);
//        ((Node)spatial.getChild("emitter")).attachChild(debrisEffect);
//        debrisEffect.emitAllParticles();
        Bullet b = new Bullet(spatial, info);
        scene.attachChild(spatial);
        bullets.add(b);
        addCollisionShape(b);
    }
    public void addCollisionShape(CollisionShape h) {
        collisionShapes.add(h);
        GameUtils.assignGeometryOwner(h.getCollisionShape(), h);
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