/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.dungeon;

import codex.boost.Timer;
import codex.tanks.components.OnSleep;
import codex.tanks.components.RoomCondition;
import codex.tanks.components.Visual;
import codex.tanks.systems.VisualState;
import com.jme3.scene.Node;
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
    
    private EntityId entity;
    private final RoomIndex index;
    private EntitySet entities;
    private EntityId roomScene;
    private final ArrayList<Integer> adj = new ArrayList<>();
    private RoomCondition condition = new RoomCondition(RoomCondition.SLEEPING);
    private Timer timer = new Timer(1f);
    
    public Room(RoomIndex index) {
        this.index = index;
    }
    
    public void initialize(DungeonMaster master, VisualState visuals, Spatial scene) {
        entity = master.getEntityData().createEntity();
        master.getEntityData().setComponents(
            entity, index, condition,
            new Visual(),
            new OnSleep(OnSleep.DETACH_SPATIAL)
        );
        visuals.link(entity, new Node());
        var created = master.getFactory().getEntityFactory().createFromScene(scene);
        for (EntityId id : created) {
            master.getEntityData().setComponents(id, index, condition);
        }
    }
    
    public void setCondition(int c) {
        if (c == condition.getCondition()) {
            return;
        }
        condition = new RoomCondition(c);
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
    
    public EntityId getId() {
        return entity;
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
