/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.util.ESAppState;
import com.epagagames.particles.Emitter;
import com.jme3.app.Application;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import java.util.HashMap;

/**
 *
 * @author codex
 */
public class ParticleState extends ESAppState {
    
    private EntitySet entities;
    private final HashMap<EntityId, Emitter> emitters = new HashMap<>();
    
    @Override
    protected void init(Application app) {
        super.init(app);
        
    }
    @Override
    protected void cleanup(Application app) {}
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    
}
