/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks;

import codex.j3map.J3map;

/**
 *
 * @author gary
 */
public class TankModel {
    
    private final float speed;
    private final float rps;
    private final float bulletSpeed;
    private final int maxBullets;
    private final int maxBounces;
    private final int maxMines;
    
    public TankModel(J3map source) {
        speed = source.getFloat("speed", 6f);
        rps = source.getFloat("rps", .2f);
        bulletSpeed = source.getFloat("bulletSpeed", 10f);
        maxBullets = source.getInteger("maxBullets", -1);
        maxBounces = source.getInteger("maxBounces", 1);
        maxMines = source.getInteger("maxMines", 2);
    }

    public float getSpeed() {
        return speed;
    }
    public float getRps() {
        return rps;
    }
    public float getBulletSpeed() {
        return bulletSpeed;
    }
    public int getMaxBullets() {
        return maxBullets;
    }
    public int getMaxBounces() {
        return maxBounces;
    }
    
}
