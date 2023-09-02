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
public class Firerate implements EntityComponent {
    
    private final float rate;
    private final float reload;
    
    public Firerate(float rps) {
        this(1/rps, 0);
    }
    private Firerate(float rate, float reload) {
        this.rate = rate;
        this.reload = reload;
    }

    public float getRate() {
        return rate;
    }
    public float getCurrentReload() {
        return reload;
    }
    public boolean isReady() {
        return reload <= 0f;
    }
    @Override
    public String toString() {
        return "Firerate{" + "rate=" + rate + '}';
    }
    
    public Firerate increment(float tpf) {
        return new Firerate(rate, reload-tpf);
    }
    public Firerate shoot() {
        return new Firerate(rate, rate);
    }
    
}
