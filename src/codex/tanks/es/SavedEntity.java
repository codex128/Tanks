/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.es;

import codex.tanks.components.Save;
import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import java.util.LinkedList;

/**
 * Stores components saved from an entity.
 * 
 * <p>Does not implements {@link Entity}.
 * 
 * @author codex
 */
public class SavedEntity {
    
    private EntityId id;
    private Save save;
    private final LinkedList<EntityComponent> components = new LinkedList<>();
    
    public SavedEntity(EntityData ed, EntityId id) {
        this.id = id;
        save(ed);
    }
    
    private void save(EntityData ed) {
        save = ed.getComponent(id, Save.class);
        if (save == null) {
            throw new NullPointerException("Entity does not have a Save component!");
        }
        for (var t : save.getComponents()) {
            var c = ed.getComponent(id, t);
            if (c == null) continue;
            components.add(c);
        }
    }
    
    public EntityId getId() {
        return id;
    }
    public Save getSaveComponent() {
        return save;
    }
    public LinkedList<EntityComponent> getComponents() {
        return components;
    }
    public <T extends EntityComponent> T get(Class<T> type) {
        for (var c : components) {
            if (type.equals(c.getClass())) {
                return (T)c;
            }
        }
        return null;
    }
    
}
