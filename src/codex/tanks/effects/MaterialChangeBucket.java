/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.effects;

import codex.tanks.components.MaterialUpdate;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import java.util.LinkedList;

/**
 *
 * @author codex
 */
public class MaterialChangeBucket extends LinkedList<MatChange> {
    
    public int applyChanges(EntityData ed, EntityId id) {
        int size = size();
        if (size > 0) {
            var prev = ed.getComponent(id, MaterialUpdate.class);
            if (prev == null) {
                ed.setComponent(id, new MaterialUpdate(toArray(MatChange[]::new)));
            }
            else {
                ed.setComponent(id, prev.add(toArray(MatChange[]::new)));
            }
            clear();
        }
        return size;
    }
    
    public static void addChanges(EntityData ed, EntityId id, MatChange... changes) {
        var update = ed.getComponent(id, MaterialUpdate.class);
        if (update == null) {
            ed.setComponent(id, new MaterialUpdate(changes));
        }
        else {
            ed.setComponent(id, update.add(changes));
        }
    }
    
}
