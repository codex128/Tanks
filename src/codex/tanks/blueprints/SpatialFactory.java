/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.tanks.blueprints;

import codex.boost.scene.UserDataIterator;
import codex.tanks.components.ColorScheme;
import codex.tanks.components.Points;
import codex.tanks.effects.ColorDistanceInfluencer;
import codex.tanks.util.GameUtils;
import codex.tanks.util.PathMesh;
import com.epagagames.particles.Emitter;
import com.epagagames.particles.emittershapes.EmitterSphere;
import com.epagagames.particles.influencers.*;
import com.epagagames.particles.valuetypes.*;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import java.util.LinkedList;

/**
 *
 * @author codex
 */
public class SpatialFactory {
    
    public static final UserDataIterator[] BASIC_PREPROCESSORS = {
        new UserDataIterator<>("CullHint", String.class) {
            @Override
            public void accept(Spatial spatial, String userdata) {
                spatial.setCullHint(Spatial.CullHint.valueOf(userdata));
            }
        }
    };
    
    public static final String
        NODE = "node",
        TANK = "tank",
        BULLET = "bullet",
        MISSILE = "missile",
        FLOOR = "floor",
        WALL_REG = "wall-regular",
        BULLET_SMOKE = "bullet-smoke",
        TANK_SHARDS = "tank-shards",
        TANK_FLAME = "tank-flame",
        TANK_SMOKE = "tank-smoke",
        LASER = "laser",
        MINE = "mine",
        MUZZLEFLASH = "muzzleflash",
        EXPLOSION_SPHERE = "explosion-sphere",
        DEBUG_CUBE = "debug-cube",
        DEBUG_SPHERE = "debug-sphere",
        SLIDING_DOOR_RIGHT = "sliding-door-right",
        SLIDING_DOOR_LEFT = "sliding-door-left",
        GATEWAY = "gateway",
        BORDER_WALL_SOLID = "border-wall-solid",
        BORDER_WALL_GATE = "border-wall-gate";
    
    private final FactoryState factory;
    private final EntityData ed;
    private final AssetManager assetManager;
    private final LinkedList<UserDataIterator> preprocessors = new LinkedList<>();
    
    public SpatialFactory(FactoryState factory, AssetManager assetManager) {
        this.factory = factory;
        this.ed = factory.getEntityData();
        this.assetManager = assetManager;
        provideBasicSpatialPreProcessors();
    }
    
    public Spatial create(String model, EntityId id) {
        var spatial = createSpatial(model, id);
        if (spatial != null) for (var pre : preprocessors) {
            pre.setSpatial(spatial);
            for (var i = pre.iterator(); i.hasNext();) {
                pre.accept(i.getSpatial(), i.next());
            }
        }
        return spatial;
    }
    private Spatial createSpatial(String model, EntityId id) {
        return switch (model) {
            case NODE               -> new Node(""+id);
            case TANK               -> createTank();
            case BULLET             -> createBullet();
            case MISSILE            -> createMissile();
            case FLOOR              -> createWorldFloor();
            case WALL_REG           -> createWall(Vector3f.UNIT_XYZ);
            case DEBUG_CUBE         -> GameUtils.createDebugCube(assetManager, ColorRGBA.Blue, 1f);
            case DEBUG_SPHERE       -> GameUtils.createDebugSphere(assetManager, ColorRGBA.Blue, 1f);
            case MUZZLEFLASH        -> createMuzzleflash();
            case LASER              -> createLaser(id);
            case MINE               -> createMine();
            case BULLET_SMOKE       -> createBulletSmoke();
            case EXPLOSION_SPHERE   -> createExplosionSphere();
            case TANK_SHARDS        -> createTankShards(id, .2f);
            case TANK_FLAME         -> createTankFlame(id, .5f);
            case BORDER_WALL_SOLID  -> createFromModel("Models/outer-walls.j3o", "solid-wall");
            case BORDER_WALL_GATE   -> createFromModel("Models/outer-walls.j3o", "wall");
            case GATEWAY            -> createFromModel("Models/outer-walls.j3o", "gateway");
            case SLIDING_DOOR_RIGHT -> createFromModel("Models/outer-walls.j3o", "door-right");
            case SLIDING_DOOR_LEFT  -> createFromModel("Models/outer-walls.j3o", "door-left");
            default                 -> createNode();
        };
    }
    
    public Node createNode() {
        return new Node();
    }
    
    public Spatial createTank() {
        Spatial tank = assetManager.loadModel("Models/tank.j3o");
        tank.addLight(new AmbientLight(new ColorRGBA(0.02f, 0.02f, 0.02f, 1f)));
        Material mat = new Material(assetManager, "Materials/tank.j3md");
        mat.setTexture("DiffuseMap", assetManager.loadTexture(new TextureKey("Models/tankTexture.png", false)));
        mat.setColor("MainColor", ColorRGBA.Blue);
        mat.setColor("SecondaryColor", new ColorRGBA(.3f, .5f, 1f, 1f));
        tank.setMaterial(mat);
        tank.setShadowMode(RenderQueue.ShadowMode.Cast);
        //GameUtils.getChildNode(tank, "muzzle").attachChild(GameUtils.createDebugCube(assetManager, ColorRGBA.Blue, .5f));
        return tank;
    }
    public Spatial createBullet() {
        Spatial bullet = assetManager.loadModel("Models/bullet.j3o");
        bullet.setShadowMode(RenderQueue.ShadowMode.Off);
        bullet.setLocalScale(.2f);
        return bullet;
    }
    public Spatial createMissile() {
        Spatial missile = createBullet();
        var mat = new Material(assetManager, "Materials/flame.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture(new TextureKey("Effects/flame.png", false)));
        mat.setFloat("Seed", 0f);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.setTransparent(true);
        var flame = assetManager.loadModel("Effects/flame.j3o");
        flame.setName("flame");
        flame.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y));
        flame.setLocalScale(3f, 3f, 4f);
        flame.setQueueBucket(RenderQueue.Bucket.Transparent);
        flame.setMaterial(mat);
        GameUtils.getChildNode(missile, "emitter").attachChild(flame);
        return missile;
    }
    public Spatial createWorldFloor() {
        Spatial floor = assetManager.loadModel("Models/floor.j3o");
        floor.setShadowMode(RenderQueue.ShadowMode.Receive);
        Material mat = new Material(assetManager, "Common/MatDefs/Light/PBRLighting.j3md");
        //mat.setBoolean("UseMaterialColors", true);
        mat.setColor("BaseColor", ColorRGBA.Green);
        //var tex = assetManager.loadTexture("Textures/testgrid.png");
        //tex.setWrap(Texture.WrapMode.Repeat);
        //mat.setTexture("DiffuseMap", tex);
        floor.setMaterial(mat);
        return floor;
    }
    public Spatial createWall(Vector3f size) {
        var wall = new Geometry("wall", new Box(size.x, size.y, size.z));
        var mat = new Material(assetManager, "Common/MatDefs/Light/PBRLighting.j3md");
        //mat.getAdditionalRenderState().setWireframe(true);
        wall.setMaterial(mat);
        return wall;
    }
    public Spatial createMuzzleflash() {
        var flash = assetManager.loadModel("Effects/muzzleflash.j3o");
        var mat = assetManager.loadMaterial("Materials/muzzleflash.j3m");
        mat.setTransparent(true);
        flash.setMaterial(mat);
        flash.setQueueBucket(RenderQueue.Bucket.Transparent);
        return flash;
    }
    public Spatial createLaser(EntityId id) {
        var mesh = new PathMesh();
        mesh.setPoints(ed.getComponent(id, Points.class).getPoints());
        var color = ed.getComponent(id, ColorScheme.class).getPallete()[0];
        var geometry = new Geometry("laser", mesh);
        var material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", color);
        material.setColor("GlowColor", color);
        geometry.setMaterial(material);
        return geometry;
    }
    public Spatial createMine() {
        return assetManager.loadModel("Models/mine.j3o");
    }
    public Spatial createExplosionSphere() {
        var sphere = GameUtils.createDebugSphere(assetManager, ColorRGBA.Blue, 1f);
        sphere.setMaterial(assetManager.loadMaterial("Materials/explosion.j3m"));
        return sphere;
    }
    public Spatial createFromModel(String model, String child) {
        return GameUtils.getChild(assetManager.loadModel(model), child);
    }
    
    public Geometry createDebug(ColorRGBA color, float size) {
        return GameUtils.createDebugCube(assetManager, color, size);
    }
    
    public Emitter createBulletSmoke() {
        var mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.setTransparent(true);
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Smoke.png"));
        var smoke = new Emitter("bullet-smoke", mat, 100);
        smoke.setCullHint(Spatial.CullHint.Never);
        //smoke.setShape(new PointEmissionShape());
        smoke.setShape(new EmitterSphere(.3f));
        smoke.setQueueBucket(RenderQueue.Bucket.Transparent);
        smoke.setStartSpeed(new ValueType(0f));
        smoke.setStartSize(new ValueType(.5f));
        smoke.setLifeFixedDuration(2.0f);
        smoke.setEmissionsPerSecond(40);
        smoke.setParticlesPerEmission(1);
        final ValueType life = new ValueType(.5f);
        smoke.setLifeMinMax(life, life);
        smoke.setParticlesFollowEmitter(false);
        var color = new ColorInfluencer();
        color.setStartEndColor(new ColorRGBA(.3f, .3f, .3f, .05f), new ColorRGBA(.1f, .1f, .1f, 0f));
        smoke.addInfluencer(color);
        var sprite = new SpriteInfluencer();
        smoke.addInfluencer(sprite);
        sprite.setSpriteRows(1);
        sprite.setSpriteCols(15);
        sprite.setAnimate(false);
        //sprite.setUseRandomImage(true);
        return smoke;
    }
    public Emitter createTankShards(EntityId id, float radius) {
        var mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.setTransparent(true);
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Shards.png"));
        var shards = new Emitter("tank-shards", mat, 30);
        //shards.setCullHint(Spatial.CullHint.Never);
        //smoke.setShape(new PointEmissionShape());
        shards.setShape(new EmitterSphere(radius));
        shards.setQueueBucket(RenderQueue.Bucket.Transparent);
        shards.setStartSpeed(new ValueType(13f));
        shards.setStartSize(new ValueType(.4f));
        shards.setStartColor(new ColorValueType(ColorRGBA.DarkGray));
        shards.setLifeFixedDuration(2.0f);
        shards.setEmissionsPerSecond(0);
        //shards.setParticlesPerEmission(5);
        final ValueType life = new ValueType(.7f);
        shards.setLifeMinMax(life, life);
        var coloring = new ColorInfluencer();
        var color = ed.getComponent(id, ColorScheme.class).getPallete()[0];
        coloring.setStartEndColor(color, color.clone().setAlpha(0f));
        shards.addInfluencer(coloring);
        var gravity = new GravityInfluencer();
        gravity.setGravity(0f, 30f, 0f);
        shards.addInfluencer(gravity);
        var rotation = new RotationLifetimeInfluencer();
        rotation.setSpeedOverLifetime(new VectorValueType(new Vector3f(1f, 1f, 1f), new Vector3f(-1f, -1f, -1f)));
        shards.addInfluencer(rotation);
        var sprite = new SpriteInfluencer();
        shards.addInfluencer(sprite);
        sprite.setSpriteRows(3);
        sprite.setSpriteCols(3);
        sprite.setAnimate(true);
        sprite.setFixedDuration(.03f);
        sprite.setUseRandomImage(true);
        //debris.emitAllParticles();
        return shards;
    }
    public Emitter createTankFlame(EntityId id, float radius) {
        var mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.setTransparent(true);
        mat.setTexture("Texture", assetManager.loadTexture("Effects/flame-burst.png"));
        var flame = new Emitter("tank-flame", mat, 100);
        //flame.setCullHint(Spatial.CullHint.Never);
        //smoke.setShape(new PointEmissionShape());
        flame.setShape(new EmitterSphere(.01f));
        flame.setQueueBucket(RenderQueue.Bucket.Transparent);
        flame.setStartSpeed(new ValueType(0f, 4f));
        flame.setStartSize(new ValueType(2f));
        //flame.setStartColor(new ColorValueType(new Gradient().addGradPoint(ColorRGBA.Orange, 0f).addGradPoint(ColorRGBA.DarkGray, 1f)));
        //flame.setLifeFixedDuration(10.0f);
        flame.setEmissionsPerSecond(0);
        //flame.setParticlesPerEmission(40);
        final ValueType life = new ValueType(.7f);
        flame.setLifeMinMax(life, life);
        var coloring = new ColorInfluencer();
        var color = ed.getComponent(id, ColorScheme.class).getPallete()[0];
        coloring.setStartEndColor(color, color.clone().setAlpha(0f));
        //flame.addInfluencer(coloring);
        var dist = new ColorDistanceInfluencer(new Vector2f(.1f, .5f), new Gradient().addGradPoint(new ColorRGBA(1f, .1f, 0f, 1f), 0f).addGradPoint(new ColorRGBA(.01f, .01f, .01f, 1f), 1f));
        flame.addInfluencer(dist);
        var sizing = new SizeInfluencer();
        sizing.setSizeOverTime(new ValueType(-1f));
        flame.addInfluencer(sizing);
        var gravity = new GravityInfluencer();
        gravity.setGravity(0f, 30f, 0f);
        //flame.addInfluencer(gravity);
        var rotation = new RotationLifetimeInfluencer();
        rotation.setSpeedOverLifetime(new VectorValueType(new Vector3f(2f, 2f, 2f), new Vector3f(-2f, -2f, -2f)));
        flame.addInfluencer(rotation);
        var sprite = new SpriteInfluencer();
        flame.addInfluencer(sprite);
        sprite.setSpriteRows(2);
        sprite.setSpriteCols(2);
        sprite.setAnimate(false);
        sprite.setUseRandomImage(true);
        //debris.emitAllParticles();
        return flame;
    }
    public Emitter createShockwave(float start, float end, ColorRGBA color) {
        var mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.setTransparent(true);
        mat.setTexture("Texture", assetManager.loadTexture("Effects/flame-burst.png"));
        var shock = new Emitter("shockwave", mat, 1);
        //smoke.setShape(new PointEmissionShape());
        shock.setShape(new EmitterSphere(.01f));
        shock.setQueueBucket(RenderQueue.Bucket.Transparent);
        shock.setStartSpeed(new ValueType(0f, 4f));
        shock.setStartSize(new ValueType(.2f));
        shock.setStartColor(new ColorValueType(color));
        //flame.setLifeFixedDuration(10.0f);
        shock.setEmissionsPerSecond(0);
        //flame.setParticlesPerEmission(40);
        final ValueType life = new ValueType(.7f);
        shock.setLifeMinMax(life, life);
        shock.setParticlesFollowEmitter(false);
        var sizing = new SizeInfluencer();
        sizing.setSizeOverTime(new ValueType(5f));
        shock.addInfluencer(sizing);
        return shock;
    }
    
    public void addSpatialPreProcessor(UserDataIterator pre) {
        preprocessors.add(pre);
    }
    public void removeSpatialPreProcessor(UserDataIterator pre) {
        preprocessors.remove(pre);
    }
    public void clearAllPreProcessors() {
        preprocessors.clear();
    }
    private void provideBasicSpatialPreProcessors() {
        for (var p : BASIC_PREPROCESSORS) {
            addSpatialPreProcessor(p);
        }
    }
    
}
