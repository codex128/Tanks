/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.effects;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author codex
 */
public class SpatialUpdateDetector extends AbstractControl {

    private boolean updated = false;
    
    @Override
    protected void controlUpdate(float tpf) {
        updated = true;
    }
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {}
    
    public boolean updatedSinceLastCheck() {
        if (updated) {
            updated = false;
            return true;
        }
        return false;
    }
    
}
