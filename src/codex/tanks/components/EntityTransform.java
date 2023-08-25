/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.simsilica.es.EntityComponent;

/**
 *
 * @author codex
 */
public class EntityTransform implements EntityComponent {
    
    private final Vector3f translation = new Vector3f();
    private final Quaternion rotation = new Quaternion();
    private final Vector3f scale = new Vector3f(1f, 1f, 1f);
    
    public EntityTransform() {}
    public EntityTransform(EntityTransform transform) {
        translation.set(transform.translation);
        rotation.set(transform.rotation);
        scale.set(transform.scale);
    }
    
    public EntityTransform setTranslation(Vector3f location) {
        this.translation.set(location);
        return this;
    }
    public EntityTransform setTranslation(float x, float y, float z) {
        translation.set(x, y, z);
        return this;
    }
    public EntityTransform setRotation(Quaternion rotation) {
        this.rotation.set(rotation);
        return this;
    }
    public EntityTransform setRotation(Vector3f lookAt, Vector3f up) {
        rotation.lookAt(lookAt, up);
        return this;
    }
    public EntityTransform setRotation(float angle, Vector3f axis) {
        rotation.fromAngleAxis(angle, axis);
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
    
    public EntityTransform move(Vector3f move) {
        translation.addLocal(move);
        return this;
    }
    public EntityTransform rotate(Quaternion rotate) {
        rotation.mult(rotate);
        return this;
    }

    public Vector3f getTranslation() {
        return translation;
    }
    public Quaternion getRotation() {
        return rotation;
    }
    public Vector3f getScale() {
        return scale;
    }
    
    public void applyToSpatial(Spatial spatial) {
        spatial.setLocalTranslation(translation);
        spatial.setLocalRotation(rotation);
        spatial.setLocalScale(scale);
    }

    @Override
    public String toString() {
        return "EntityTransform{" + "location=" + translation + ", rotation=" + rotation + ", scale=" + scale + '}';
    }
    
}
