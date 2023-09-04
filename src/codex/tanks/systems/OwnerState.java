/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.BulletCapacity;
import codex.tanks.components.CapacityComponent;
import codex.tanks.components.Owner;
import codex.tanks.util.ESAppState;
import codex.tanks.weapons.ShootEventListener;
import com.jme3.app.Application;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import java.util.Objects;

/**
 *
 * @author codex
 */
public class OwnerState extends ESAppState {

    private EntitySet entities;
    
    @Override
    protected void init(Application app) {
        super.init(app);
        entities = ed.getEntities(Owner.class);
    }
    @Override
    protected void cleanup(Application app) {
        entities.release();
    }
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    
    public int getNumOwnedEntities(EntityId id, String tag) {
        entities.applyChanges();
        int sum = 0;
        for (var e : entities) {
            var owner = e.get(Owner.class);
            if (owner.isOwner(id) && (tag == null || Objects.equals(owner.getTag(), tag))) {
                sum++;
            }
        }
        return sum;
    }
    public <T extends CapacityComponent> boolean isBelowCapacity(EntityId id, Class<T> type, String tag) {
        var cap = ed.getComponent(id, type);
        if (cap == null) return true;
        return cap.getMaxCapacity() > getNumOwnedEntities(id, tag);
    }
    
}
