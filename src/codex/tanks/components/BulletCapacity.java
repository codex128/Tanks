/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

/**
 *
 * @author codex
 */
public class BulletCapacity implements CapacityComponent {
    
    private final int max;

    public BulletCapacity(int max) {
        this.max = max;
    }

    @Override
    public int getMaxCapacity() {
        return max;
    }
    @Override
    public String toString() {
        return "MaxBullets{" + "max=" + max + '}';
    }
    
}
