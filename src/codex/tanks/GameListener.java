/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.tanks;

/**
 *
 * @author gary
 */
public interface GameListener {
    
    public void tankAdded(Tank t);
    public void bulletAdded(Bullet b);
    public void collisionShapeAdded(CollisionShape s);
    
}
