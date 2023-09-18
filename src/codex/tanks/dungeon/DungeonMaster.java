/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.dungeon;

import codex.tanks.components.RoomIndex;
import codex.tanks.factories.SpatialFactory;
import codex.tanks.components.*;
import codex.tanks.es.ESAppState;
import codex.tanks.es.FunctionFilter;
import com.jme3.app.Application;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.post.SceneProcessor;
import com.jme3.profile.AppProfiler;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.texture.FrameBuffer;
import com.simsilica.es.EntitySet;
import com.simsilica.mathd.Vec3i;
import java.util.LinkedList;

/**
 *
 * @author codex
 */
public class DungeonMaster extends ESAppState implements SceneProcessor {
    
    public static final float BORDER_WIDTH = 2;
    public static final Vector3f ROOM_SIZE = new Vector3f(50f+BORDER_WIDTH, 0f, 50f+BORDER_WIDTH);
    
    private Room[][] rooms;
    private LinkedList<Room> roomStack = new LinkedList<>();
    private EntitySet entities;
    private EntitySet gateways;
    private EntitySet teams;
    private EntitySet copy;
    private boolean combat = true;
    private final AccessKey gateKey = new AccessKey();
    
    @Override
    protected void init(Application app) {
        super.init(app);
        
        entities = ed.getEntities(RoomIndex.class, RoomStatus.class);
        gateways = ed.getEntities(Gateway.class, RoomIndex.class, Activated.class, Lock.class);
        teams = ed.getEntities(new FunctionFilter<>(RoomStatus.class, c -> c.getState() == RoomStatus.ACTIVE),
                Team.class, RoomStatus.class);
        copy = ed.getEntities(Copy.filter(Copy.ROOM_STATUS), Copy.class, RoomStatus.class);
        
        createDungeon(1, 1, new RoomIndex(0, 0));
        
//        var e1 = factory.getEntityFactory().createAITank(new Vector3f(7f, 0f, 7f), 1, new PropertySource("Properties/AI.j3map", "grey"));
//        factory.getEntityFactory().makeDungeonCompatible(e1, new RoomIndex(0, 1));
//        var e2 = factory.getEntityFactory().createAITank(new Vector3f(7f, 0f, 7f+20f), 1, new PropertySource("Properties/AI.j3map", "light-green"));
//        factory.getEntityFactory().makeDungeonCompatible(e2, new RoomIndex(0, 0));
        
    }
    @Override
    protected void cleanup(Application app) {}
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        // update room status
        entities.applyChanges();
        for (var e : entities) {
            var index = e.get(RoomIndex.class);
            RoomStatus highest = null;
            for (var i : index.getIndices()) {
                var r = getRoomByIndex(i);
                if (highest == null || r.getStatus().getState() > highest.getState()) {
                    highest = r.getStatus();
                }
            }
            if (highest == null) {
                highest = roomStack.getFirst().getStatus();
            }
            e.set(highest);
        }
        copy.applyChanges();
        for (var e : copy) {
            var status = ed.getComponent(e.get(Copy.class).getCopy(), RoomStatus.class);
            if (status != null) {
                e.set(status);
            }            
        }
        if (roomStack.getFirst().getStatus().getState() == RoomStatus.ACTIVE && detectTeamVictory()) {
            unlockActiveDoors();
            combat = false;
        }
        gateways.applyChanges();
        for (var e : gateways) {
            if (e.get(Activated.class).isActivated()) {
                var i = e.get(RoomIndex.class).getDifferentIndex(roomStack.getFirst().getIndex().getPrimaryIndex());
                setActiveRoom(getRoomByIndex(i));
                e.set(new Activated(false));
            }
        }
    }
    
    public void createDungeon(int width, int height, RoomIndex start) {
        rooms = new Room[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                var r = new Room(new RoomIndex(j, i), new Vector3f(j, 0f, height-1-i).multLocal(ROOM_SIZE));
                r.initialize(this, visuals, assetManager.loadModel("Scenes/dungeons/blank.j3o"));
                rooms[i][j] = r;
            }
        }
        createDungeonWalls();
        setActiveRoom(getRoomByIndex(start));
    }
    private void createDungeonWalls() {
        for (int i = 0; i < rooms.length; i++) {
            for (int j = 0; j < rooms[i].length; j++) {
                var position = new Vector3f(j, 0f, (rooms.length-1-i)).multLocal(ROOM_SIZE);
                createWallUp(position, j, i);
                createWallRight(position, j, i);
                createWallDown(position, j, i);
                createWallLeft(position, j, i);
            }
        }
    }
    private void createWallUp(Vector3f position, int x, int y) {
        if (y == 0) {
            createBorderWallSolid(position.add(0f, 0f, ROOM_SIZE.z/2), new RoomIndex(x, y), 0f);
        }
    }
    private void createWallRight(Vector3f position, int x, int y) {
        var p = position.add(ROOM_SIZE.x/2, 0f, 0f);
        var i = new Vec3i(x, y, 0);
        if (x == rooms[y].length-1) {
            createBorderWallSolid(p, new RoomIndex(i), -FastMath.HALF_PI);
        }
        else {
            createBorderWallGate(p, new RoomIndex(i, new Vec3i(x+1, y, 0)), -FastMath.HALF_PI);
        }
    }
    private void createWallDown(Vector3f position, int x, int y) {
        var p = position.subtract(0f, 0f, ROOM_SIZE.z/2f);
        var i = new Vec3i(x, y, 0);
        if (y == rooms.length-1) {
            createBorderWallSolid(p, new RoomIndex(i), 0f);
        }
        else {
            createBorderWallGate(p, new RoomIndex(i, new Vec3i(x, y+1, 0)), 0f);
        }
    }
    private void createWallLeft(Vector3f position, int x, int y) {
        if (x == 0) {
            createBorderWallSolid(position.subtract(ROOM_SIZE.x/2, 0f, 0f), new RoomIndex(x, y), -FastMath.HALF_PI);
        }
    }
    private void createBorderWallSolid(Vector3f p, RoomIndex index, float angle) {
        var wall = factory.getEntityFactory().createWall(SpatialFactory.BORDER_WALL_SOLID, p, angle);
        ed.setComponents(wall, index, new RoomStatus(RoomStatus.ACTIVE));
    }
    private void createBorderWallGate(Vector3f p, RoomIndex index, float angle) {
        var c = new RoomStatus(RoomStatus.ACTIVE);
        var wall = factory.getEntityFactory().createWall(SpatialFactory.BORDER_WALL_GATE, p, angle);
        ed.setComponents(wall, c, index);
        var leftDoor = factory.getEntityFactory().createSlidingDoor(SpatialFactory.SLIDING_DOOR_LEFT, p, angle, 2.1f, 10f);
        ed.setComponents(leftDoor, c, index);
        var rightDoor = factory.getEntityFactory().createSlidingDoor(SpatialFactory.SLIDING_DOOR_RIGHT, p, angle, 2.1f, -10f);
        ed.setComponents(rightDoor, c, index);
        var gate = factory.getEntityFactory().createGateway(index.getIndexAt(0), index.getIndexAt(1), p.add(0f, 2f, 0f), angle, gateKey.getKey(), leftDoor, rightDoor);
        ed.setComponents(gate, c, index);
    }
    
    public void setActiveRoom(Room room) {
        if (room == null) return;
        if (!roomStack.isEmpty()) {
            roomStack.getFirst().setCondition(RoomStatus.SLEEPING);
        }
        roomStack.addFirst(room);
        roomStack.getFirst().setCondition(RoomStatus.ACTIVE);
        if (roomStack.size() > 2) {
            roomStack.removeLast();
        }
    }
    private boolean detectTeamVictory() {
        if (teams.applyChanges()) {
            int t = -1;
            for (var e : teams.getAddedEntities()) {
                if (t < 0) {
                    t = e.get(Team.class).getTeam();
                }
                else if (t != e.get(Team.class).getTeam()) {
                    return false;
                }
            }
            for (var e : teams.getChangedEntities()) {
                if (t < 0) {
                    t = e.get(Team.class).getTeam();
                }
                else if (t != e.get(Team.class).getTeam()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    private void unlockActiveDoors() {
        gateways.applyChanges();
        for (var e : gateways) {
            if (e.get(RoomIndex.class).contains(roomStack.getFirst().getIndex().getPrimaryIndex())) {
                e.set(e.get(Lock.class).unlock(gateKey));
            }
        }
    }
    
    public Room getRoomByIndex(RoomIndex index) {
        return rooms[index.getPrimaryIndex().y][index.getPrimaryIndex().x];
    }    
    public Room getRoomByIndex(Vec3i index) {
        return rooms[index.y][index.x];
    }
    public Room getActiveRoom() {
        return roomStack.getFirst();
    }
    public RoomIndex getActiveRoomIndex() {
        return roomStack.getFirst().getIndex();
    }

    @Override
    public void initialize(RenderManager rm, ViewPort vp) {}
    @Override
    public void reshape(ViewPort vp, int w, int h) {}
    @Override
    public void preFrame(float tpf) {}
    @Override
    public void postQueue(RenderQueue rq) {}
    @Override
    public void postFrame(FrameBuffer out) {}
    @Override
    public void setProfiler(AppProfiler profiler) {}
    
}
