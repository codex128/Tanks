/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.Alive;
import codex.tanks.components.Decay;
import codex.tanks.util.ESAppState;
import codex.tanks.util.FunctionFilter;
import com.jme3.app.Application;
import com.simsilica.es.EntitySet;

/**
 *
 * @author codex
 */
public class DecayState extends ESAppState {
    
    private EntitySet decaying;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        decaying = ed.getEntities(new FunctionFilter<>(Alive.class, c -> c.isAlive()), Decay.class, Alive.class);
    }
    @Override
    protected void cleanup(Application app) {
        decaying.release();
    }
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        decaying.applyChanges();
        for (var e : decaying) {
            var d = e.get(Decay.class).increment(tpf);
            e.set(d);
            if (d.isExhausted()) {
                e.set(new Alive(false));
            }
        }
    }
    
}
