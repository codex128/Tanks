/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.Alive;
import codex.tanks.components.Decay;
import codex.tanks.components.Power;
import codex.tanks.util.ESAppState;
import codex.tanks.util.FunctionFilter;
import com.jme3.app.Application;
import com.simsilica.es.EntitySet;

/**
 *
 * @author codex
 */
public class DecayState extends ESAppState {
    
    private EntitySet decay;
    private EntitySet life;
    private EntitySet power;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        decay = ed.getEntities(Decay.class);
        life = ed.getEntities(new FunctionFilter<>(Alive.class, c -> c.isAlive()), Decay.class, Alive.class);
        power = ed.getEntities(Decay.class, Power.class);
    }
    @Override
    protected void cleanup(Application app) {
        life.release();
    }
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        decay.applyChanges();
        life.applyChanges();
        power.applyChanges();
        for (var e : power) {
            e.set(new Power(Math.max(e.get(Power.class).getPower()*(1f-tpf/e.get(Decay.class).getDecay()), 0.01f)));
        }
        for (var e : decay) {
            e.set(e.get(Decay.class).increment(tpf));
        }
        for (var e : life) {
            if (e.get(Decay.class).isExhausted()) {
                e.set(new Alive(false));
            }
        }
    }
    
}
