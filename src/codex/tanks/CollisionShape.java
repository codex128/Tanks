/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.tanks;

import com.jme3.collision.CollisionResult;
import com.jme3.scene.Spatial;

/**
 *
 * @author gary
 */
public interface CollisionShape {
    
    public void onHit(Bullet bullet, CollisionResult collision);
    public Spatial getCollisionShape();
    
    public default boolean ricochet() {
        return false;
    }
    
}
