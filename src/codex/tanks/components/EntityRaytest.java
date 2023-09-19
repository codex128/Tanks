/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import codex.tanks.collision.ShapeFilter;
import com.jme3.math.Ray;
import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityId;

/**
 *
 * @author codex
 */
public class EntityRaytest implements EntityComponent {
    
    private final EntityId origin;
    private final Ray ray;
    private final ShapeFilter mainFilter, firstCastFilter;
    private final float distance;

    public EntityRaytest(EntityId origin, Ray ray, float distance) {
        this(origin, ray, null, null, distance);
    }
    public EntityRaytest(EntityId origin, Ray ray, ShapeFilter mainFilter, float distance) {
        this(origin, ray, mainFilter, null, distance);
    }
    public EntityRaytest(EntityId origin, Ray ray, ShapeFilter mainFilter, ShapeFilter firstCastFilter, float distance) {
        this.origin = origin;
        this.ray = ray;
        this.mainFilter = mainFilter;
        this.firstCastFilter = firstCastFilter;
        this.distance = distance;
    }

    public EntityId getOrigin() {
        return origin;
    }
    public Ray getRay() {
        return ray;
    }
    public ShapeFilter getMainFilter() {
        return mainFilter;
    }
    public ShapeFilter getFirstCastFilter() {
        return firstCastFilter;
    }
    public float getDistance() {
        return distance;
    }    
    
}
