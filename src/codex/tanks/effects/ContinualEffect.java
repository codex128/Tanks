/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.tanks.effects;

import codex.tanks.Entity;

/**
 *
 * @author codex
 */
public interface ContinualEffect extends Effect {
    
    public Entity getEffectSource();
    public void onSourceStopped();
    
}
