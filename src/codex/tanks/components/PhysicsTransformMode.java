/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

/**
 *
 * @author codex
 */
public class PhysicsTransformMode extends TransformMode {
    
    public PhysicsTransformMode() {
        super(-2, -2, -2);
    }
    public PhysicsTransformMode(int translation, int rotation, int scale) {
        super(translation, rotation, scale);
    }
    
}
