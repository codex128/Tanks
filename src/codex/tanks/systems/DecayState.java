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
    
    private EntitySet life;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        life = ed.getEntities(new FunctionFilter<>(Alive.class, c -> c.isAlive()), Decay.class, Alive.class);
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
        life.applyChanges();
        for (var e : life) {
            var d = e.get(Decay.class);
            var p = ed.getComponent(e.getId(), Power.class);
            if (p != null) {
                ed.setComponent(e.getId(), new Power(Math.max(p.getPower()*(1f-tpf/d.getDecay()), 0.01f)));
            }
            e.set(d.increment(tpf));
            if (d.isExhausted()) {
                e.set(new Alive(false));
            }
        }
    }
    
}
