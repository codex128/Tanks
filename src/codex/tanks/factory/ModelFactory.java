/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.tanks.factory;

import codex.tanks.util.GameUtils;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.HashMap;
import java.util.function.Function;

/**
 *
 * @author codex
 */
public class ModelFactory {
    
    public static final String
            TANK = "tank",
            BULLET = "bullet",
            MISSILE = "missile",
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
        flame.setLocalScale(3f, 3f, 4f);
        flame.setQueueBucket(RenderQueue.Bucket.Transparent);
        flame.setMaterial(mat);
        GameUtils.getNodeNamed(missile, "emitter").attachChild(flame);
        return missile;
    }
    
}
