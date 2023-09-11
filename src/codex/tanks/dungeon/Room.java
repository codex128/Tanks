/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.dungeon;

import codex.boost.Timer;
import codex.tanks.blueprints.SpatialFactory;
import codex.tanks.components.OnSleep;
import codex.tanks.components.RoomCondition;
import codex.tanks.components.Visual;
import com.jme3.scene.Spatial;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import java.util.ArrayList;

/**
 * Represents the state of a dungeon room.
 * 
 * @author codex
 */
public class Room {
    
    private final RoomIndex index;
    private EntitySet entities;
    private EntityId roomScene;
    private final ArrayList<Integer> adj = new ArrayList<>();
    private RoomCondition condition = new RoomCondition(RoomCondition.SLEEPING);
    private Timer timer;
    
    public Room(RoomIndex index) {
        this.index = index;
    }
    
    public void initialize(DungeonMaster master, Spatial scene) {
        entities = master.getEntityData().getEntities(RoomIndex.filter(index), RoomIndex.class, RoomCondition.class);
        roomScene = master.getEntityData().createEntity();
        master.getEntityData().setComponents(
            roomScene, index, condition,
            new Visual(SpatialFactory.NODE),
            new OnSleep(OnSleep.DETACH_SPATIAL)
        );
        var created = master.getFactory().getEntityFactory().createFromScene(scene);
        for (EntityId id : created) {
            master.getEntityData().setComponents(id, index, condition);
        }
    }
    
    public void setCondition(int c) {
        if (c == condition.getCondition()) {
            return;
        }
        entities.applyChanges();
        condition = new RoomCondition(c);
        for (var e : entities) {
            e.set(condition);
        }
        if (condition.isBetween()) {
            timer.start();
            if (condition.isUpper()) {
                // begin awakening filters
            }
            else {
                // begin sleeping filters
            }
        }
    }
    public void update(float tpf) {
        timer.update(tpf);
    }
    
    public RoomIndex getIndex() {
        return index;
    }
    public RoomCondition getCondition() {
        return condition;
    }
    public ArrayList<Integer> getAdjacencies() {
        return adj;
    }
    
}
