/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.effects;

import com.jme3.effect.ParticleEmitter;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author codex
 */
public abstract class ParticleEmitterControl extends AbstractControl {
    
    protected ParticleEmitter emitter;
    
    @Override
    public void setSpatial(Spatial spat) {
        super.setSpatial(spat);
        if (spatial == null) {
            emitter = null;
        }
        else if (spatial instanceof ParticleEmitter) {
            emitter = (ParticleEmitter)spatial;
        }
        else {
            throw new IllegalArgumentException("This control may only control ParticleEmitters!");
        }
    }
    
    public ParticleEmitter getEmitter() {
        return emitter;
    }
    
}
