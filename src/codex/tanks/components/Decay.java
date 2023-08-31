/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.simsilica.es.EntityComponent;

/**
 *
 * @author codex
 */
public class Decay implements EntityComponent {
    
    private final float original;
    private final float decay;
    
    public Decay(float decay) {
        this.original = decay;
        this.decay = decay;
    }
    private Decay(float original, float decay) {
        this.original = original;
        this.decay = decay;
    }
    
    public float getDecay() {
        return decay;
    }
    public float getOriginalValue() {
        return original;
    }
    public boolean isExhausted() {
        return decay <= 0;
    }
    public Decay increment(float tpf) {
        return new Decay(original, decay-tpf);
    }
    @Override
    public String toString() {
        return "Decay{" + "original=" + original + ", decay=" + decay + '}';
    }
    
}
