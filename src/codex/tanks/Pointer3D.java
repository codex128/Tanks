/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.event.CursorButtonEvent;
import com.simsilica.lemur.event.CursorEventControl;
import com.simsilica.lemur.event.CursorListener;
import com.simsilica.lemur.event.CursorMotionEvent;

/**
 *
 * @author gary
 */
public class Pointer3D implements CursorListener {

    private Spatial collider;
    private Vector3f pointerLocation = new Vector3f();
    
    public Pointer3D(Spatial collider) {
        this.collider = collider;
        this.collider.addControl(new CursorEventControl(this));
    }
    
    @Override
    public void cursorButtonEvent(CursorButtonEvent cbe, Spatial sptl, Spatial sptl1) {}
    @Override
    public void cursorEntered(CursorMotionEvent cme, Spatial sptl, Spatial sptl1) {}
    @Override
    public void cursorExited(CursorMotionEvent cme, Spatial sptl, Spatial sptl1) {}
    @Override
    public void cursorMoved(CursorMotionEvent cme, Spatial sptl, Spatial sptl1) {
        pointerLocation.set(cme.getCollision().getContactPoint());
    }
    
    public Vector3f getGlobalPointer() {
        return pointerLocation;
    }
    public Vector3f getRelativePointer() {
        return pointerLocation.subtract(collider.getWorldTranslation());
    }
    public void exit() {
        collider.removeControl(CursorEventControl.class);
    }
    
}
