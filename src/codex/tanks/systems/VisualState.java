/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.OnSleep;
import codex.tanks.components.EntityTransform;
import codex.tanks.components.RoomCondition;
import codex.tanks.components.Visual;
import codex.tanks.es.ESAppState;
import codex.tanks.util.GameUtils;
import com.jme3.app.Application;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import java.util.HashMap;

/**
 *
 * @author codex
 */
public class VisualState extends ESAppState {
    
    public static final String USERDATA = "EntityId";
    
    private EntitySet entities;
    private EntitySet sleepDetach;
    private final HashMap<EntityId, Spatial> spatials = new HashMap<>();
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(Visual.class);
        sleepDetach = ed.getEntities(OnSleep.filter(OnSleep.DETACH_SPATIAL),
                Visual.class, RoomCondition.class, OnSleep.class);
    }
    @Override
    protected void cleanup(Application app) {
        entities.release();
        sleepDetach.release();
        spatials.values().forEach(s -> s.removeFromParent());
        spatials.clear();
    }
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        if (entities.applyChanges()) {
            entities.getAddedEntities().forEach(e -> createModel(e));
            entities.getRemovedEntities().forEach(e -> destroyModel(e));
        }
        if (sleepDetach.applyChanges()) {
            sleepDetach.getAddedEntities().forEach(e -> updateCondition(e, true));
            sleepDetach.getChangedEntities().forEach(e -> updateCondition(e, false));
        }
    }
    
    private void createModel(Entity e) {
        if (getSpatial(e.getId()) != null) return;
        var v = e.get(Visual.class);
        Spatial spatial = null;
        if (v.getModel() != null) {
            spatial = factory.getSpatialFactory().create(v.getModel(), e.getId());
        }
        if (spatial == null) {
            spatial = GameUtils.createDebugCube(assetManager, ColorRGBA.Blue, 1f);
        }
        if (v.getScene() != null) {
            ((Node)getSpatial(v.getScene())).attachChild(spatial);
        }
        link(e.getId(), spatial);
    }
    private void destroyModel(Entity e) {
        var spatial = unlink(e.getId());
        if (spatial != null) {
            spatial.removeFromParent();
        }
    }
    private void updateCondition(Entity e, boolean force) {
        var spatial = getSpatial(e.getId());
        if (spatial == null) return;
        if (e.get(RoomCondition.class).getCondition() == RoomCondition.SLEEPING) {
            spatial.removeFromParent();
        }
        else if (force || spatial.getParent() == null) {
            var v = e.get(Visual.class);
            if (v.getScene() == null) {
                rootNode.attachChild(spatial);
            }
            else {
                ((Node)getSpatial(e.getId())).attachChild(spatial);
            }
        }
    }
    
    public Spatial getSpatial(EntityId id) {
        return spatials.get(id);
    }
    public <T extends Spatial> T getSpatial(EntityId id, Class<T> type) {
        var s = getSpatial(id);
        if (s != null && type.isAssignableFrom(s.getClass())) {
            return (T)s;
        }
        return null;
    }
    public boolean link(EntityId id, Spatial spatial) {
        return link(id, spatial, true);
    }
    public boolean link(EntityId id, Spatial spatial, boolean attach) {
        if (spatials.putIfAbsent(id, spatial) != null) {
            return false;
        }
        assignId(spatial, id);
        if (spatial.getParent() == null && attach) {
            rootNode.attachChild(spatial);
        }
        var transform = ed.getComponent(id, EntityTransform.class);
        if (transform != null) {
            transform.assignToSpatial(spatial);
        }
        return true;
    }
    private Spatial unlink(EntityId id) {
        var spatial = spatials.remove(id);
        if (spatial != null) {
            assignId(spatial, null);
        }
        return spatial;
    }
    
    private static void assignId(Spatial spatial, EntityId id) {
        spatial.setUserData(USERDATA, (id != null ? id.getId() : null));
    }
    public static EntityId fetchId(Spatial spatial, int depth) {
        do {
            Long id = spatial.getUserData(USERDATA);
            if (id != null) return new EntityId(id);
            spatial = spatial.getParent();
        } while (spatial != null && depth-- != 0);
        return null;
    }
    
}
