/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.collision;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.simsilica.es.EntityId;

/**
 *
 * @author codex
 */
public interface Raytest {
    
    public void cast(CollisionState state);
    public CollisionResult getCollision();
    public EntityId getCollisionEntity();
    
    public static void raycast(CollisionState state, Ray ray, ShapeFilter filter, CollisionResults results) {
        for (var i = state.iterator(filter); i.hasNext();) {
            i.next().collideWith(ray, results);
        }
    }
    
}
