/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.es;

import codex.boost.GameAppState;
import codex.tanks.blueprints.FactoryState;
import codex.tanks.components.RoomCondition;
import codex.tanks.dungeon.DungeonMaster;
import codex.tanks.systems.EntityState;
import codex.tanks.systems.VisualState;
import com.jme3.app.Application;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

/**
 *
 * @author codex
 */
public abstract class ESAppState extends GameAppState {
    
    protected EntityData ed;
    protected FactoryState factory;
    protected VisualState visuals;
    
    @Override
    protected void init(Application app) {
        ed = getState(EntityState.class, true).getEntityData();
        factory = getState(FactoryState.class, true);
        visuals = getState(VisualState.class);
    }
    
    public EntityData getEntityData() {
        return ed;
    }
    public FactoryState getFactory() {
        return factory;
    }
    public VisualState getVisualState() {
        return visuals;
    }
    public DungeonMaster getDungeonMaster() {
        return getState(DungeonMaster.class);
    }
    
    public boolean isEntityRoomActive(EntityId id) {
        var asleep = ed.getComponent(id, RoomCondition.class);
        return asleep != null && asleep.getCondition() == RoomCondition.ACTIVE;
    }
    
}