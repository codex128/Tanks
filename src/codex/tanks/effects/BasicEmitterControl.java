/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.effects;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author codex
 */
public class BasicEmitterControl extends ParticleEmitterControl {
    
    private final Node ancestor;
    private final Spatial target;
    
    public BasicEmitterControl(Node ancestor, Spatial target) {
        this.ancestor = ancestor;
        this.target = target;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        if (target.hasAncestor(ancestor)) {
            emitter.setLocalTranslation(target.getWorldTranslation());
        }
        else {
            emitter.setParticlesPerSec(0);
            if (emitter.getNumVisibleParticles() == 0) {
                emitter.removeFromParent();
            }
        }
    }
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {}
    @Override
    public void setSpatial(Spatial spat) {
        super.setSpatial(spat);
        if (emitter != null) {
            
        }
    }
    
    public Node getAncestor() {
        return ancestor;
    }
    public Spatial getTarget() {
        return target;
    }
    
}
