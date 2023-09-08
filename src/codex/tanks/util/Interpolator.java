/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.util;

/**
 *
 * @author codex
 */
public interface Interpolator {
    
    public static final Interpolator
    LINEAR    = (float in) -> { return in; },
    QUADRATIC = (float in) -> { return in*in; };
    
    public float interpolate(float in);
    
}
