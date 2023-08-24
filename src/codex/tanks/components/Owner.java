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
public class Owner implements EntityComponent {
    
    private final EntityId id;

    public Owner(EntityId id) {
        this.id = id;
    }

    public EntityId getId() {
        return id;
    }
    public boolean isOwner(EntityId test) {
        return id.equals(test);
    }
    @Override
    public String toString() {
        return "Owner{" + "id=" + id + '}';
    }
    
}
