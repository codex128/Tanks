/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.tanks.factory;

import codex.tanks.util.GameUtils;
import com.epagagames.particles.Emitter;
import com.epagagames.particles.emittershapes.EmitterCone;
import com.epagagames.particles.valuetypes.ValueType;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

/**
 *
 * @author codex
 */
public class ModelFactory {
    
    public static final String
            TANK = "tank",
            BULLET = "bullet",
            MISSILE = "missile",
            FLOOR = "floor",
            DEBUG = "debug";
    
    private final AssetManager assetManager;
    
    public ModelFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
    
    public Spatial create(String model) {
        return switch (model) {
            case TANK    -> createTank();
            case BULLET  -> createBullet();
            case MISSILE -> createMissile();
            case FLOOR   -> createWorldFloor();
            case DEBUG   -> GameUtils.createDebugGeometry(assetManager, ColorRGBA.Blue, 1f);
            default      -> createDefault();
        };
    }
    
    public Spatial createDefault() {
        return new Node();
    }
    public Spatial createTank() {
        Spatial tank = assetManager.loadModel("Models/tank.j3o");
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
    public Spatial createBulletSmoke() {
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        Texture tex = assetManager.loadTexture("Effects/Particles/part_light.png");
        mat.setTexture("Texture", tex);
        Emitter emitter = new Emitter("test", mat, 100);
        emitter.setStartSpeed(new ValueType(6.5f));
        emitter.setLifeFixedDuration(2.0f);
        emitter.setEmissionsPerSecond(20);
        emitter.setParticlesPerEmission(1);
        emitter.setShape(new EmitterCone());
        ((EmitterCone)emitter.getShape()).setRadius(0.005f);
        return emitter;
    }
    
}
