/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks;

import codex.tanks.weapons.Tank;
import codex.j3map.J3map;
import codex.tanks.blueprints.ContactMethods;
import codex.tanks.components.*;
import codex.tanks.dungeon.RoomIndex;
import codex.tanks.systems.VisualState;
import codex.tanks.es.ESAppState;
import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.post.ssao.SSAOFilter;

/**
 *
 * @author gary
 */
public class GameState extends ESAppState {
    
    private BulletAppState bulletapp;
    private PlayerAppState player;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        
        bulletapp = getState(BulletAppState.class, true);
        
//        var floor = ed.createEntity();
//        ed.setComponents(floor,
//            new Visual(SpatialFactory.FLOOR),
//            new RigidBody(0f),
//            new EntityTransform().setTranslation(0f, -1f, 0f),
//            new CollisionShape(null),
//            new ContactResponse(ContactMethods.RICOCHET)
//        );
        
        var plr = factory.getEntityFactory().createTank(new Vector3f(-7f, 0f, -7f), 0, new PropertySource("Properties/player.j3map"));
        ed.setComponents(plr, new Player(0), new RoomIndex(0, 0), new RoomCondition(RoomCondition.ACTIVE));
        player = new PlayerAppState(plr);
        getStateManager().attach(player);
        
//        J3map enemySources = (J3map)assetManager.loadAsset("Properties/AI.j3map");
//        J3map index = enemySources.getJ3map("index");
//        for (int a = -1; a < 2; a += 2) {
//            for (int j = 0; j < 4; j++) {
//                var src = index.getString(""+FastMath.rand.nextInt(index.getOrderedPropertyList().size()));
//                for (int i = 0; i < 4; i++) {
//                    factory.getEntityFactory().createAITank(new Vector3f(-a*7f-i*3*a, 0f, 12f-j*3), 1, new PropertySource("Properties/AI.j3map", src));
//                }
//            }
//        }
        
        float r = 20f;
//        createWall(new Vector3f(-r, 0f, 0f), 0f, new Vector3f(1f, 1f, r));
//        createWall(new Vector3f(r, 0f, 0f), 0f, new Vector3f(1f, 1f, r));
//        createWall(new Vector3f(0f, 0f, -r), 0f, new Vector3f(r, 1f, 1f));
//        createWall(new Vector3f(0f, 0f, r), 0f, new Vector3f(r, 1f, 1f));
//        createWall(new Vector3f(0f, 0f, 0f), FastMath.PI/4, new Vector3f(3f, 1f, 3f));        
//        createWall(new Vector3f(12f, 0f, 0f), 0f, new Vector3f(4f, 1f, 1f));   
//        createWall(new Vector3f(-12f, 0f, 0f), 0f, new Vector3f(4f, 1f, 1f));
//        createWall(new Vector3f(0f, 0f, 12f), 0f, new Vector3f(1f, 1f, 4f));
//        createWall(new Vector3f(0f, 0f, -12f), 0f, new Vector3f(1f, 1f, 4f));
        //createWall(new Vector3f(0f, 0f, 0f), 0f, new Vector3f(20f, 1f, 1f));
        
        float radius = 50f;
        addLight(new Vector3f(r-2, 2f, r-2), ColorRGBA.White, radius);
        addLight(new Vector3f(-r+2, 2f, r-2), ColorRGBA.White, radius);
        addLight(new Vector3f(r+2, 2f, -r+2), ColorRGBA.White, radius);
        addLight(new Vector3f(-r+2, 2f, -r+2), ColorRGBA.White, radius);
        addLight(new Vector3f(0f, 2f, 0f), ColorRGBA.White, radius);
        
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
            new RigidBody(0f),
            new EntityTransform()
                .setTranslation(location)
                .setRotation(angle, Vector3f.UNIT_Y)
                .setScale(1f),
            new TransformMode(1, 1, 0),
            new CollisionShape(),
            new ContactResponse(ContactMethods.RICOCHET));
        var spatial = factory.getSpatialFactory().createWall(size);
        getState(VisualState.class).link(wall, spatial);
    }
    private void addLight(Vector3f location, ColorRGBA color, float radius) {
        rootNode.addLight(new PointLight(location, color, radius));
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
