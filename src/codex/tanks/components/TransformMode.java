/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.simsilica.es.EntityComponent;
import java.util.function.Function;

/**
 *
 * @author codex
 */
public class TransformMode implements EntityComponent {
    
    public static final int
            PHYSICS_TO_ENTITY = -3,
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

    public int getTranslation() {
        return translation;
    }
    public int getRotation() {
        return rotation;
    }
    public int getScale() {
        return scale;
    }
    
    public boolean anyMatch(Function<Integer, Boolean> filter) {
        return filter.apply(translation) || filter.apply(rotation) || filter.apply(scale);
    }
    
    @Override
    public String toString() {
        return "SpatialTransform{" + "translation=" + translation + ", rotation=" + rotation + ", scale=" + scale + '}';
    }
    
    public static boolean isEntityToSpatial(int m) {
        return m >= ENTITY_TO_LOCAL;
    }
    public static boolean isSpatialToEntity(int m) {
        return m == LOCAL_TO_ENTITY || m == WORLD_TO_ENTITY;
    }
    public static boolean isWorld(int m) {
        return m == ENTITY_TO_WORLD || m == WORLD_TO_ENTITY;
    }
    public static boolean isNone(int m) {
        return m == NONE;
    }
    public static boolean isPhysics(int m) {
        return m == PHYSICS_TO_ENTITY;
    }
    
}
