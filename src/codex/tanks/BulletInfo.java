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
    
    public enum Type {
        
        Basic("Basic"), Missile("Missile");
        
        private final String name;
        private Type(String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return name;
        }
        
    }
    
    public Tank owner;
    public Type type;
    public Vector3f start;
    public Vector3f velocity;
    public int bounces;
    public float life;
    
    public BulletInfo(Tank owner, Type type, Vector3f start, Vector3f velocity, int bounces, float life) {
        this.owner = owner;
        this.type = type;
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
    public float getSpeed() {
        return velocity.length();
    }
    
}
