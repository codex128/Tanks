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
            ed.setComponent(id, new MaterialUpdate(toArray(MatChange[]::new)));
            clear();
        }
        return size;
    }
    
}
