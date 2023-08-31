/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks;

import codex.j3map.J3map;
import codex.tanks.ai.Algorithm;
import codex.tanks.components.*;
import codex.tanks.factory.SpatialFactory;
import codex.tanks.systems.VisualState;
import codex.tanks.util.ESAppState;
import codex.tanks.util.GameUtils;
import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.DirectionalLightShadowRenderer;

/**
 *
 * @author gary
 */
public class GameState extends ESAppState {
    
    private BulletAppState bulletapp;
    private PlayerAppState player;
    private DirectionalLight light;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        
        bulletapp = getState(BulletAppState.class, true);
        
        var floor = ed.createEntity();
        ed.setComponents(floor,
            new Visual(SpatialFactory.FLOOR),
            new Physics(0f),
            new EntityTransform().setTranslation(0f, -1f, 0f),
            new CollisionShape(null),
            new ContactReaction(ContactReaction.RICOCHET)
        );
        
        J3map playerSource = (J3map)app.getAssetManager().loadAsset("Properties/player.j3map");        
        var plr = ed.createEntity();
        ed.setComponents(plr,
            new GameObject("tank"),
            new Visual(SpatialFactory.TANK),
            new Physics(),
            new EntityTransform().setTranslation(-7f, 0f, -7f),
            new TransformMode(-1, 0, 0),
            new CollisionShape("hitbox"),
            new ContactReaction(ContactReaction.DIE),
            new Team(0),
            new Alive()
        );
        Tank.applyProperties(ed, plr, playerSource);
        player = new PlayerAppState(plr);
        getStateManager().attach(player);
        
        J3map[] enemySources = {
            (J3map)assetManager.loadAsset("Properties/purple.j3map"),
            (J3map)assetManager.loadAsset("Properties/grey.j3map"),
            (J3map)assetManager.loadAsset("Properties/light-green.j3map"),
            (J3map)assetManager.loadAsset("Properties/black.j3map"),
        };
        for (int i = 0; i < 4; i++) {
            var src = enemySources[FastMath.nextRandomInt(0, enemySources.length-1)];
            //var src = enemySources[2];
            var enemy = ed.createEntity();
            ed.setComponents(enemy,
                new GameObject("tank"),
                new Visual(SpatialFactory.TANK),
                new Physics(),
                new EntityTransform().setTranslation(5f+i*3, 0f, 7f),
                new TransformMode(-1, 0, 0),
                new CollisionShape("hitbox"),
                new ContactReaction(ContactReaction.DIE),
                new Team(1),
                new Alive()
            );
            Tank.applyProperties(ed, enemy, src.getJ3map("tank"));
            Algorithm.applyProperties(ed, enemy, src);
        }
        
        float r = 20f;
        createWall(new Vector3f(-r, 0f, 0f), 0f, new Vector3f(1f, 1f, r));
        createWall(new Vector3f(r, 0f, 0f), 0f, new Vector3f(1f, 1f, r));
        createWall(new Vector3f(0f, 0f, -r), 0f, new Vector3f(r, 1f, 1f));
        createWall(new Vector3f(0f, 0f, r), 0f, new Vector3f(r, 1f, 1f));
        createWall(new Vector3f(0f, 0f, 0f), FastMath.PI/4, new Vector3f(3f, 1f, 3f));        
        createWall(new Vector3f(12f, 0f, 0f), 0f, new Vector3f(4f, 1f, 1f));   
        createWall(new Vector3f(-12f, 0f, 0f), 0f, new Vector3f(4f, 1f, 1f));   
        createWall(new Vector3f(0f, 0f, 12f), 0f, new Vector3f(1f, 1f, 4f));
        createWall(new Vector3f(0f, 0f, -12f), 0f, new Vector3f(1f, 1f, 4f));
        //createWall(new Vector3f(0f, 0f, 0f), 0f, new Vector3f(20f, 1f, 1f));
        
        final float brightness = 0.1f;
        light = new DirectionalLight(new Vector3f(1f, -1f, 1f), new ColorRGBA(brightness, brightness, brightness, 1f));
        rootNode.addLight(light);
        var drsr = new DirectionalLightShadowRenderer(app.getAssetManager(), 4096, 2);
        drsr.setLight(light);
        //app.getViewPort().addProcessor(drsr);
        
        var fpp = new FilterPostProcessor(app.getAssetManager());
        var ssao = new SSAOFilter();
        ssao.setIntensity(5f);
        fpp.addFilter(ssao);
        var bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
        bloom.setBlurScale(1f);
        fpp.addFilter(bloom);
        app.getViewPort().addProcessor(fpp);
        
        /*var envCam = getState(EnvironmentCamera.class, true);
        envCam.setPosition(new Vector3f(0f, 20f, 0f));
        envCam.setBackGroundColor(ColorRGBA.White);
        LightProbeFactory.makeProbe(envCam, rootNode, new JobProgressAdapter<>() {
            @Override
            public void done(LightProbe result) {
                result.getArea().setRadius(100f);
                rootNode.addLight(result);
            }
        });*/
        
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
            new GameObject("wall"),
            new Visual(),
            new Physics(0f),
            new EntityTransform()
                .setTranslation(location)
                .setRotation(angle, Vector3f.UNIT_Y),
            new TransformMode(1, 1, 0),
            new CollisionShape(),
            new ContactReaction(ContactReaction.RICOCHET));
        var mesh = new Box(size.x, size.y, size.z);
        mesh.scaleTextureCoordinates(new Vector2f(size.x, size.z));
        var geometry = GameUtils.createDebugGeometry(assetManager, ColorRGBA.DarkGray, size);
        geometry.setShadowMode(RenderQueue.ShadowMode.Cast);
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
