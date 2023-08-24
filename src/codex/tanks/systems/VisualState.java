/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.EntityTransform;
import codex.tanks.factory.ModelFactory;
import codex.tanks.util.ESAppState;
import com.jme3.app.Application;
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
    
    public static final String USERDATA = "entityId";
    
    private EntitySet visuals;
    private final HashMap<EntityId, Spatial> spatials = new HashMap<>();
    private final HashMap<String, Node> scenes = new HashMap<>();
    private ModelFactory factory;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        visuals = ed.getEntities(Visual.class);
        addScene(rootNode);
        addScene(guiNode);
        factory = new ModelFactory(assetManager);
    }
    @Override
    protected void cleanup(Application app) {
        visuals.release();
        spatials.values().forEach(s -> s.removeFromParent());
        spatials.clear();
        scenes.values().forEach(s -> s.removeFromParent());
        scenes.clear();
    }
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        if (visuals.applyChanges()) {
            visuals.getAddedEntities().forEach(e -> createModel(e));
            visuals.getRemovedEntities().forEach(e -> destroyModel(e));
        }
    }
    
    private void createModel(Entity e) {
        var v = e.get(Visual.class);
        if (!v.isCustom()) {
            Spatial spatial = factory.create(v.getModel());
            if (v.getScene() != null) {
                getScene(v.getScene()).attachChild(spatial);
            }
            var transform = ed.getComponent(e.getId(), EntityTransform.class);
            if (transform != null) {
                transform.applyToSpatial(spatial);
            }
            link(e.getId(), spatial);
            rootNode.attachChild(spatial);
        }
    }
    private void destroyModel(Entity e) {
        var spatial = unlink(e.getId());
        if (spatial != null) {
            spatial.removeFromParent();
        }
    }
    
    public Spatial getSpatial(EntityId id) {
        return spatials.get(id);
    }
    public boolean link(EntityId id, Spatial spatial) {
        if (spatials.putIfAbsent(id, spatial) == null) {
            assignId(spatial, id);
            return true;
        }
        return false;
    }
    private Spatial unlink(EntityId id) {
        var spatial = spatials.remove(id);
        if (spatial != null) {
            assignId(spatial, null);
        }
        return spatial;
    }
    
    public boolean addScene(Node scene) {
        return scenes.putIfAbsent(scene.getName(), scene) == null;
    }
    public Node removeScene(String scene) {
        return scenes.remove(scene);
    }
    public Node getScene(String scene) {
        return scenes.get(scene);
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