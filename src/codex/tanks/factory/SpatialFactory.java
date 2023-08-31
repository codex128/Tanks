/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.tanks.factory;

import codex.tanks.util.GameUtils;
import com.epagagames.particles.Emitter;
import com.epagagames.particles.emittershapes.EmitterSphere;
import com.epagagames.particles.influencers.ColorInfluencer;
import com.epagagames.particles.influencers.SpriteInfluencer;
import com.epagagames.particles.valuetypes.ColorValueType;
import com.epagagames.particles.valuetypes.ValueType;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

/**
 *
 * @author codex
 */
public class SpatialFactory {
    
    public static final String
            TANK = "tank",
            BULLET = "bullet",
            MISSILE = "missile",
            FLOOR = "floor",
            BULLET_SMOKE = "bullet-smoke",
            TANK_DEBRIS = "tank-debris",
            MUZZLEFLASH = "muzzleflash",
            DEBUG = "debug";
    
    private final AssetManager assetManager;
    
    public SpatialFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
    
    public Spatial create(String model) {
        return switch (model) {
            case TANK           -> createTank();
            case BULLET         -> createBullet();
            case MISSILE        -> createMissile();
            case FLOOR          -> createWorldFloor();
            case DEBUG          -> createDebug(ColorRGBA.Blue, 1f);
            case MUZZLEFLASH    -> createMuzzleflash();
            case BULLET_SMOKE   -> createBulletSmoke();
            case TANK_DEBRIS    -> createTankDebris();
            default             -> createNode();
        };
    }
    public Node createNode() {
        return new Node();
    }
    public Geometry createGeometry(String model) {
        return switch (model) {
            case DEBUG          -> createDebug(ColorRGBA.Blue, 1f);
            default             -> null;
        };
    }
    public Emitter createEmitter(String model) {
        return switch (model) {
            case BULLET_SMOKE   -> createBulletSmoke();
            case TANK_DEBRIS    -> createTankDebris();
            default             -> null;
        };
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
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        //mat.setBoolean("UseMaterialColors", true);
        //mat.setColor("Diffuse", ColorRGBA.Green);
        var tex = assetManager.loadTexture("Textures/testgrid.png");
        tex.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("DiffuseMap", tex);
        floor.setMaterial(mat);
        return floor;
    }
    public Spatial createMuzzleflash() {
        var flash = assetManager.loadModel("Effects/muzzleflash.j3o");
        var mat = GameUtils.fetchMaterial(flash);
        mat.setTransparent(true);
        flash.setQueueBucket(RenderQueue.Bucket.Transparent);
        return flash;
    }
    
    public Geometry createDebug(ColorRGBA color, float size) {
        return GameUtils.createDebugGeometry(assetManager, color, size);
    }
    
    public Emitter createBulletSmoke() {
        var mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.setTransparent(true);
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Smoke.png"));
        var smoke = new Emitter("bullet-smoke", mat, 100);
        //smoke.setShape(new PointEmissionShape());
        smoke.setShape(new EmitterSphere(.3f));
        smoke.setQueueBucket(RenderQueue.Bucket.Transparent);
        smoke.setStartSpeed(new ValueType(.5f));
        smoke.setStartSize(new ValueType(.2f));
        smoke.setLifeFixedDuration(2.0f);
        smoke.setEmissionsPerSecond(40);
        smoke.setParticlesPerEmission(1);
        final ValueType life = new ValueType(.5f);
        smoke.setLifeMinMax(life, life);
        smoke.setParticlesFollowEmitter(false);
        var color = new ColorInfluencer();
        color.setStartEndColor(new ColorRGBA(.3f, .3f, .3f, .5f), new ColorRGBA(.1f, .1f, .1f, 0f));
        smoke.addInfluencer(color);
        var sprite = new SpriteInfluencer();
        smoke.addInfluencer(sprite);
        sprite.setSpriteRows(15);
        sprite.setSpriteCols(1);
        //sprite.setUseRandomImage(true);
        return smoke;
    }
    public Emitter createTankDebris() {
        return createTankDebris(ColorRGBA.Gray, .2f);
    }
    public Emitter createTankDebris(ColorRGBA color, float radius) {
        var mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.setTransparent(true);
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Debris.png"));
        var debris = new Emitter("bullet-smoke", mat, 100);
        //smoke.setShape(new PointEmissionShape());
        debris.setShape(new EmitterSphere(radius));
        debris.setQueueBucket(RenderQueue.Bucket.Transparent);
        debris.setStartSpeed(new ValueType(.5f));
        debris.setStartSize(new ValueType(.2f));
        debris.setStartColor(new ColorValueType(color));
        debris.setLifeFixedDuration(2.0f);
        debris.setEmissionsPerSecond(40);
        debris.setParticlesPerEmission(1);
        final ValueType life = new ValueType(.5f);
        debris.setLifeMinMax(life, life);
        debris.setParticlesFollowEmitter(false);
        var sprite = new SpriteInfluencer();
        debris.addInfluencer(sprite);
        sprite.setSpriteRows(3);
        sprite.setSpriteCols(3);
        sprite.setUseRandomImage(true);
        return debris;
    }
    
}
