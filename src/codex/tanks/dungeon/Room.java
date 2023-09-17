/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.dungeon;

import codex.tanks.components.RoomIndex;
import codex.boost.Timer;
import codex.tanks.components.EntityTransform;
import codex.tanks.components.RemoveOnSleep;
import codex.tanks.components.RoomStatus;
import codex.tanks.components.SpawnAssignment;
import codex.tanks.systems.VisualState;
import com.jme3.math.Vector3f;
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
    private final Vector3f position;
    private EntitySet entities;
    private EntityId roomScene;
    private final ArrayList<Integer> adj = new ArrayList<>();
    private RoomStatus condition = new RoomStatus(RoomStatus.SLEEPING);
    private Timer timer = new Timer(1f);
    
    public Room(RoomIndex index, Vector3f position) {
        this.index = index;
        this.position = position;
    }
    
    public void initialize(DungeonMaster master, VisualState visuals, Spatial scene) {
        var created = master.getFactory().getEntityFactory().createFromScene(scene);
        for (EntityId id : created) {
            master.getEntityData().setComponents(id,
                master.getEntityData().getComponent(id, EntityTransform.class).move(position),
                index, condition, new SpawnAssignment(SpawnAssignment.TO_OWN)
            );
        }
        
    }
    
    public void setCondition(int c) {
        if (c == condition.getState()) {
            return;
        }
        condition = new RoomStatus(c);
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
    public Vector3f getPosition() {
        return position;
    }
    public RoomStatus getStatus() {
        return condition;
    }
    public ArrayList<Integer> getAdjacencies() {
        return adj;
    }
    
}
