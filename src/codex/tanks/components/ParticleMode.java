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
public class ParticleMode implements EntityComponent {
    
    public static final ParticleMode
            NORMAL = new ParticleMode("particlemode:normal"),
            SUNSET = new ParticleMode("particlemode:sunset");
    
    private final String mode;
    
    private ParticleMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }
    @Override
    public String toString() {
        return "EffectMode{" + "mode=" + mode + '}';
    }
    
}
