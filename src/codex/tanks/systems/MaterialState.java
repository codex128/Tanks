/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.MaterialUpdate;
import codex.tanks.components.Visual;
import codex.tanks.effects.MatChange;
import codex.tanks.util.ESAppState;
import codex.tanks.util.GameUtils;
import com.jme3.app.Application;
import com.jme3.material.Material;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import java.util.HashMap;

/**
 *
 * @author codex
 */
public class MaterialState extends ESAppState {
    
    private EntitySet entities;
    private final HashMap<EntityId, Material> materials = new HashMap<>();
    private VisualState visuals;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(Visual.class, MaterialUpdate.class);
        visuals = getState(VisualState.class, true);
    }
    @Override
    protected void cleanup(Application app) {}
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        if (entities.applyChanges()) {
            entities.getAddedEntities().forEach(e -> updateMaterial(e));
        }
    }
    
    private void updateMaterial(Entity e) {
        var update = e.get(MaterialUpdate.class);
        for (var param : update.getUpdates()) {
            var mat = locateMaterial(e.getId(), update, param);
            if (mat == null) continue;
            mat.setParam(param.getParamName(), param.getType(), param.getValue());
        }
        ed.removeComponent(e.getId(), MaterialUpdate.class);
    }
    private Material locateMaterial(EntityId id, MaterialUpdate update, MatChange param) {
        var spatial = visuals.getSpatial(id);
        if (spatial == null) return null;
        if (update.getRootSpatial() == null && param.getOwnerName() == null) {
            return GameUtils.fetchMaterial(spatial);
        }
        var root = update.getRootSpatial() != null ? GameUtils.getChild(spatial, update.getRootSpatial()) : spatial;
        if (root == null) return null;
        else if (param.getOwnerName() == null) {
            return GameUtils.fetchMaterial(root);
        }
        var child = GameUtils.getChild(root, param.getOwnerName());
        if (child == null) return null;
        return GameUtils.fetchMaterial(child);
    }
    
}
