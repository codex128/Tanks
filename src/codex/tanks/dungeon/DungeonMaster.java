/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.dungeon;

import codex.tanks.components.BorderMember;
import codex.tanks.components.Gateway;
import codex.tanks.components.RoomCondition;
import codex.tanks.components.Visual;
import codex.tanks.es.ESAppState;
import codex.tanks.es.FunctionFilter;
import com.jme3.app.Application;
import com.simsilica.es.EntitySet;

/**
 *
 * @author codex
 */
public class DungeonMaster extends ESAppState {
    
    private Room[][] rooms;
    private Room active;
    private EntitySet dynamicIndices;
    private EntitySet borders;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        dynamicIndices = ed.getEntities(new FunctionFilter<>(RoomIndex.class, c -> c.isMatchActive()), RoomIndex.class);
        borders = ed.getEntities(BorderMember.class);
        gateways = ed.getEntities(Visual.class, BorderMember.class, Gateway.class);
        
        // create a very simple 1x1 dungeon
        createDungeon(1, 1, new RoomIndex(0, 0));
        
    }
    @Override
    protected void cleanup(Application app) {}
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    
    public void createDungeon(int width, int height, RoomIndex start) {
        rooms = new Room[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                var r = new Room(new RoomIndex(j, i));
                r.initialize(this, assetManager.loadModel("Models/dungeons/blank.j3o"));
                rooms[i][j] = r;
            }
        }
        getRoomByIndex(start).setCondition(RoomCondition.ACTIVE);
    }
    private void createDungeonWalls() {
        for (int i = 0; i < rooms.length; i++) {
            for (int j = 0; j < rooms[i].length; i++) {
                var r = rooms[i][j];
                if (i == 0) {
                    // create solid wall up
                }
                if (j == 0) {
                    // create solid wall left
                }
                if (i == rooms.length-1) {
                    // create solid wall down
                }
                else {
                    // create gateway wall down
                }
                if (j == rooms[i].length-1) {
                    // create solid wall right
                }
                else {
                    // create gateway wall right
                }
            }
        }
    }
    
    public Room getRoomByIndex(RoomIndex index) {
        if (index.x < 0 || index.y < 0) return null;
        return rooms[index.y][index.x];
    }    
    public Room getActiveRoom() {
        return active;
    }
    public RoomIndex getActiveRoomIndex() {
        return active.getIndex();
    }
    
    public void setActiveRoom(Room room) {
        active.setCondition(RoomCondition.SLEEPING);
        active = room;
        dynamicIndices.applyChanges();
        for (var e : dynamicIndices) {
            e.set(e.get(RoomIndex.class).set(active.getIndex()));
        }
        active.setCondition(RoomCondition.ACTIVE);
    }
    public Room advance(int direction) {
        if (direction == RoomIndex.NONE) return null;
        var next = getActiveRoomIndex().add(direction);
        // mark entities for save and removal
        borders.applyChanges();
        for (var e : borders) {
            if (!e.get(BorderMember.class).equals(getActiveRoomIndex(), next)) {
                // mark for removal
            }
        }
        setActiveRoom(getRoomByIndex(next));
        // load border entities for the current room
        return getActiveRoom();
    }
    public Room advance(RoomIndex index) {
        return advance(getActiveRoomIndex().getDirectionTo(index));
    }
    
}
