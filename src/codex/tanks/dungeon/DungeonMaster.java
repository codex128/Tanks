/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.dungeon;

import codex.tanks.blueprints.SpatialFactory;
import codex.tanks.components.AccessKey;
import codex.tanks.components.Activated;
import codex.tanks.components.BorderMember;
import codex.tanks.components.Gateway;
import codex.tanks.components.Lock;
import codex.tanks.components.RoomCondition;
import codex.tanks.components.Team;
import codex.tanks.es.ESAppState;
import codex.tanks.es.FunctionFilter;
import com.jme3.app.Application;
import com.jme3.material.Material;
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

/**
 *
 * @author codex
 */
public class DungeonMaster extends ESAppState implements SceneProcessor {
    
    public static final float BORDER_WIDTH = 2;
    public static final Vector3f ROOM_SIZE = new Vector3f(50f+BORDER_WIDTH, 0f, 50f+BORDER_WIDTH);
    
    private Room[][] rooms;
    private Room active;
    private EntitySet roomEntities;
    private EntitySet current;
    private EntitySet gateways;
    private EntitySet teams;
    private ViewPort upView, downView;
    private Material wireframe;
    private boolean combat = true;
    private AccessKey gateKey = new AccessKey();
    
    @Override
    protected void init(Application app) {
        super.init(app);
        
        roomEntities = ed.getEntities(RoomIndex.class, RoomCondition.class);
        current = ed.getEntities(new FunctionFilter<>(RoomIndex.class, c -> c.isMatchActive()), RoomIndex.class);
        gateways = ed.getEntities(new FunctionFilter<>(Activated.class, c -> c.isActivated()),
                Gateway.class, RoomIndex.class, Activated.class, Lock.class);
        teams = ed.getEntities(Team.class, RoomIndex.class);
        
        createDungeon(1, 2, new RoomIndex(0, 0));
        
    }
    @Override
    protected void cleanup(Application app) {}
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        if (current.applyChanges()) {
            for (var e : current.getAddedEntities()) {
                e.set(e.get(RoomIndex.class).set(active.getIndex()));
            }
        }
        roomEntities.applyChanges();
        for (var e : roomEntities) {
            var r = getRoomByIndex(e.get(RoomIndex.class));
            if (r != null) {
                e.set(r.getCondition());
            }
        }
        if (active.getCondition().getCondition() == RoomCondition.ACTIVE && detectTeamVictory()) {
            unlockActiveDoors();
            combat = false;
        }
    }
    
    public void createDungeon(int width, int height, RoomIndex start) {
        rooms = new Room[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                var r = new Room(new RoomIndex(j, i));
                r.initialize(this, visuals, assetManager.loadModel("Models/dungeons/blank.j3o"));
                rooms[i][j] = r;
            }
        }
        createDungeonWalls();
        setActiveRoom(getRoomByIndex(start));
    }
    private void createDungeonWalls() {
        for (int i = 0; i < rooms.length; i++) {
            for (int j = 0; j < rooms[i].length; j++) {
                var position = new Vector3f(rooms[i].length-1-j, 0f, (rooms.length-1-i)).multLocal(ROOM_SIZE);
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
        if (x == rooms.length-1) {
            createBorderWallSolid(p, new RoomIndex(i), FastMath.HALF_PI);
        }
        else {
            createBorderWallGate(p, new RoomIndex(i, new Vec3i(x+1, y, 0)), FastMath.HALF_PI);
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
            createBorderWallSolid(position.subtract(ROOM_SIZE.x/2, 0f, 0f), new RoomIndex(x, y), FastMath.HALF_PI);
        }
    }
    private void createBorderWallSolid(Vector3f p, RoomIndex index, float angle) {
        var wall = factory.getEntityFactory().createWall(SpatialFactory.BORDER_WALL_SOLID, p, angle);
        ed.setComponents(wall, index, new RoomCondition(RoomCondition.SLEEPING));
    }
    private void createBorderWallGate(Vector3f p, RoomIndex index, float angle) {
        System.out.println("create border wall gate");
        var c = new RoomCondition(RoomCondition.SLEEPING);
        var wall = factory.getEntityFactory().createWall(SpatialFactory.BORDER_WALL_GATE, p, angle);
        ed.setComponents(wall, c, index);
        var leftDoor = factory.getEntityFactory().createSlidingDoor(SpatialFactory.SLIDING_DOOR_LEFT, p, angle, 2.1f, 1f);
        ed.setComponents(leftDoor, c, index);
        var rightDoor = factory.getEntityFactory().createSlidingDoor(SpatialFactory.SLIDING_DOOR_RIGHT, p, angle, 2.1f, -1f);
        ed.setComponents(rightDoor, c, index);
        var gate = factory.getEntityFactory().createGateway(p, angle, gateKey.getKey(), leftDoor, rightDoor);
        ed.setComponents(gate, c, index);
    }
    
    public void setActiveRoom(Room room) {
        if (room == null) return;
        if (active != null) {
            active.setCondition(RoomCondition.SLEEPING);
            downView.attachScene(visuals.getSpatial(active.getId()));
        }
        active = room;
        current.applyChanges();
        for (var e : current) {
            e.set(e.get(RoomIndex.class).set(active.getIndex()));
        }
        active.setCondition(RoomCondition.ACTIVE);
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
            for (var e : teams.getRemovedEntities()) {
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
            if (e.get(RoomIndex.class).contains(active.getIndex().getPrimaryIndex())) {
                e.set(e.get(Lock.class).unlock(gateKey));
            }
        }
    }
    
    public Room getRoomByIndex(RoomIndex index) {
        return rooms[index.getPrimaryIndex().y][index.getPrimaryIndex().x];
    }    
    public Room getActiveRoom() {
        return active;
    }
    public RoomIndex getActiveRoomIndex() {
        return active.getIndex();
    }

    @Override
    public void initialize(RenderManager rm, ViewPort vp) {
        rm.setForcedMaterial(wireframe);
    }
    @Override
    public void reshape(ViewPort vp, int w, int h) {}
    @Override
    public void preFrame(float tpf) {}
    @Override
    public void postQueue(RenderQueue rq) {
        renderManager.setForcedMaterial(wireframe);
    }
    @Override
    public void postFrame(FrameBuffer out) {
        renderManager.setForcedMaterial(null);
    }
    @Override
    public void setProfiler(AppProfiler profiler) {}
    
}
