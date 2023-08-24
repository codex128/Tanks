/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.util;

import com.jme3.light.Light;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author codex
 * @param <T>
 */
public abstract class LightNode <T extends Light> extends Node {
    
    protected T light;
    private Spatial owner;
    private boolean active = false;
    
    public LightNode(T light, Spatial owner) {
        this.light = light;
        this.owner = owner;
    }
    
    @Override
    public void updateLogicalState(float tpf) {
        super.updateLogicalState(tpf);
        if (!active) {
            owner.addLight(light);
            active = true;
        }
        updateLightTransform();
    }
    @Override
    public boolean removeFromParent() {
        if (super.removeFromParent()) {
            active = false;
            owner.removeLight(light);
            return true;
        }
        return false;
    }
    protected abstract void updateLightTransform();
    
}
