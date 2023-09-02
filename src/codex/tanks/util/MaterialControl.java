/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.util;

import codex.boost.control.GeometryControl;
import com.jme3.material.Material;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;

/**
 *
 * @author codex
 */
public abstract class MaterialControl extends GeometryControl {

    @Override
    protected void controlUpdate(float tpf) {
        updateMaterial(geometry, geometry.getMaterial(), tpf);
    }
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {}
    
    protected void updateMaterial(Geometry geometry, Material material, float tpf) {
        
    }
    
}
