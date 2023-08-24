/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.simsilica.es.EntityComponent;

/**
 *
 * @author codex
 */
public class SpatialTransform implements EntityComponent {
    
    public final boolean translation, rotation, scale;
    
    public SpatialTransform() {
        this(true, true, true);
    }
    public SpatialTransform(boolean translation, boolean rotation, boolean scale) {
        this.translation = translation;
        this.rotation = rotation;
        this.scale = scale;
    }

    public boolean isTranslation() {
        return translation;
    }
    public boolean isRotation() {
        return rotation;
    }
    public boolean isScale() {
        return scale;
    }
    @Override
    public String toString() {
        return "SpatialTransform{" + "translation=" + translation + ", rotation=" + rotation + ", scale=" + scale + '}';
    }
    
}
