/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks;

import com.jme3.math.Vector3f;

/**
 *
 * @author gary
 */
public class BulletInfo {
    
    public Tank owner;
    public Vector3f start;
    public Vector3f velocity;
    public int bounces;
    public float life;
    
    public BulletInfo(Tank owner, Vector3f start, Vector3f velocity, int bounces, float life) {
        this.owner = owner;
        this.start = start;
        this.velocity = velocity;
        this.bounces = bounces;
        this.life = life;
    }
    
    public boolean isAlive() {
        return life > 0 && bounces >= 0;
    }
    public void kill() {
        life = 0;
    }
    public Vector3f getDirection() {
        return velocity.normalize();
    }
    
}
