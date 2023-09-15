/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityId;

/**
 *
 * @author codex
 */
public class Dependency implements EntityComponent {
    
    private final EntityId id;
    private final Class locator;

    public Dependency(EntityId id, Class locator) {
        this.id = id;
        this.locator = locator;
    }

    public EntityId getId() {
        return id;
    }
    public Class getLocator() {
        return locator;
    }
    @Override
    public String toString() {
        return "Dependency{" + "id=" + id + ", locator=" + locator + '}';
    }
    
}
