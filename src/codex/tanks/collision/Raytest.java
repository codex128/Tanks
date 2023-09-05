/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.collision;

import codex.tanks.systems.ContactState;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.simsilica.es.EntityId;

/**
 *
 * @author codex
 */
public interface Raytest {
    
    public void cast(ContactState state);
    
    public default CollisionResult getCollision() {
        return null;
    }
    public default EntityId getCollisionEntity() {
        return null;
    }
    
    public static void raycast(ContactState state, Ray ray, CollisionResults results) {
        for (var i = state.iterator(); i.hasNext();) {
            i.next().collideWith(ray, results);
        }
    }
    public static void raycast(ContactState state, Ray ray, ShapeFilter filter, CollisionResults results) {
        for (var i = state.iterator(filter); i.hasNext();) {
            i.next().collideWith(ray, results);
        }
    }
    
}
