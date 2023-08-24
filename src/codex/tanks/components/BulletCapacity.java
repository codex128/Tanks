/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.simsilica.es.EntityComponent;

/**
 *
 * @author codex
 */
public class BulletCapacity implements EntityComponent {
    
    private final int max;

    public BulletCapacity(int max) {
        this.max = max;
    }

    public int getMax() {
        return max;
    }
    @Override
    public String toString() {
        return "MaxBullets{" + "max=" + max + '}';
    }
    
}
