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
public class TransformMode implements EntityComponent {
    
    public static final int
            WORLD_TO_ENTITY = -2,
            LOCAL_TO_ENTITY = -1,
            NONE = 0,
            ENTITY_TO_LOCAL =  1,
            ENTITY_TO_WORLD =  2;
    
    public final int translation, rotation, scale;
    
    public TransformMode() {
        this(1, 1, 1);
    }
    public TransformMode(int translation, int rotation, int scale) {
        this.translation = translation;
        this.rotation = rotation;
        this.scale = scale;
    }

    public int getTranslationState() {
        return translation;
    }
    public int getRotationState() {
        return rotation;
    }
    public int getScaleState() {
        return scale;
    }
    
    public boolean isEntityToSpatialTranslation() {
        return translation > 0;
    }
    public boolean isEntityToSpatialRotation() {
        return rotation > 0;
    }
    public boolean isEntityToSpatialScale() {
        return rotation > 0;
    }
    public boolean isSpatialTranslationToEntity() {
        return translation < 0;
    }
    public boolean isSpatialRotationToEntity() {
        return rotation < 0;
    }
    public boolean isSpatialScaleToEntity() {
        return rotation < 0;
    }
    public boolean useWorldTranslation() {
        return Math.abs(translation) == 2;
    }
    public boolean useWorldRotation() {
        return Math.abs(rotation) == 2;
    }
    public boolean useWorldScale() {
        return Math.abs(scale) == 2;
    }
    
    @Override
    public String toString() {
        return "SpatialTransform{" + "translation=" + translation + ", rotation=" + rotation + ", scale=" + scale + '}';
    }
    
}
