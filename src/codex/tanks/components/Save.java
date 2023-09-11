/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.simsilica.es.EntityComponent;

/**
 * Indicates which components should be saved in order to accurately
 * recreate the entity via {@link EntityFactory}.
 * 
 * @author codex
 */
public class Save implements EntityComponent {
    
    private final String restore;
    private final Class[] components;

    public Save(String restore, Class... components) {
        this.restore = restore;
        this.components = components;
    }

    public String getRestore() {
        return restore;
    }
    public Class[] getComponents() {
        return components;
    }
    public boolean contains(Class clazz) {
        for (var c : components) {
            if (c.equals(clazz)) return true;
        }
        return false;
    }
    @Override
    public String toString() {
        return "Save{" + "components=" + components.length + '}';
    }
    
}
