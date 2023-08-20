/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
import com.jme3.math.FastMath;

/**
 *
 * @author gary
 */
public class AIModel {
    
    private final float preferredPlayerDistance;
    private final float previousTurnBias;
    private final float turnSpeed;
    private final float wander;
    private final float directional;
    private final float bulletAwareness;
    private final float barrelWander;
    
    public AIModel(J3map source) {
        preferredPlayerDistance = source.getFloat("preferredPlayerDistance", 1f);
        previousTurnBias = source.getFloat("previousTurnBias", 2.0f);
        turnSpeed = source.getFloat("turnSpeed", FastMath.PI*0.01f);
        wander = source.getFloat("wander", 1f);
        directional = source.getFloat("directional", 1f);
        bulletAwareness = source.getFloat("bulletAwareness", 10f);
        barrelWander = source.getFloat("barrelWander", .5f);
    }
    
    public float getPreferredPlayerDistance() {
        return preferredPlayerDistance;
    }
    public float getPreviousTurnBias() {
        return previousTurnBias;
    }
    public float getTurnSpeed() {
        return turnSpeed;
    }
    public float getWander() {
        return wander;
    }
    public float getDirectional() {
        return directional;
    }
    public float getBulletAwareness() {
        return bulletAwareness;
    }
    public float getBarrelWander() {
        return barrelWander;
    }
    
}
