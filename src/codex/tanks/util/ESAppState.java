/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.util;

import codex.boost.GameAppState;
import codex.tanks.systems.EntityState;
import com.jme3.app.Application;
import com.simsilica.es.EntityData;

/**
 *
 * @author codex
 */
public abstract class ESAppState extends GameAppState {
    
    protected EntityData ed;
    
    @Override
    protected void init(Application app) {
        ed = getState(EntityState.class, true).getEntityData();
    }
    
}
