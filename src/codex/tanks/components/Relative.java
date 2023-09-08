/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import codex.tanks.util.ComponentRelation;
import com.simsilica.es.EntityComponent;

/**
 *
 * @author codex
 */
public class Relative implements EntityComponent {
    
    private ComponentRelation[] relations;
    
    public Relative(ComponentRelation... relations) {
        this.relations = relations;
    }

    public ComponentRelation[] getRelations() {
        return relations;
    }
    @Override
    public String toString() {
        return "Relative{" + "relations=" + relations.length + '}';
    }
    
}
