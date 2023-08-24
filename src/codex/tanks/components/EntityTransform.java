/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityComponent;

/**
 *
 * @author codex
 */
public class EntityTransform implements EntityComponent {
    
    private Vector3f location = new Vector3f();
    private Quaternion rotation = new Quaternion();
    private Vector3f scale = new Vector3f(1f, 1f, 1f);
    
    public EntityTransform() {}
    
    public EntityTransform setLocation(Vector3f location) {
        this.location.set(location);
        return this;
    }
    public EntityTransform setRotation(Quaternion rotation) {
        this.rotation.set(rotation);
        return this;
    }
    public EntityTransform setScale(Vector3f scale) {
        this.scale.set(scale);
        return this;
    }
    public EntityTransform setScale(float scalar) {
        this.scale.set(scalar, scalar, scalar);
        return this;
    }

    public Vector3f getLocation() {
        return location;
    }
    public Quaternion getRotation() {
        return rotation;
    }
    public Vector3f getScale() {
        return scale;
    }

    @Override
    public String toString() {
        return "EntityTransform{" + "location=" + location + ", rotation=" + rotation + ", scale=" + scale + '}';
    }
    
}
