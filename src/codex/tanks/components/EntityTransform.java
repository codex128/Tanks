/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import codex.tanks.util.FloatComponent;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Transform;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * Defines the transform of the entity.
 * 
 * @author codex
 */
public class EntityTransform implements FloatComponent {
    
    private final Vector3f translation = new Vector3f();
    private final Quaternion rotation = new Quaternion();
    private final Vector3f scale = new Vector3f(1f, 1f, 1f);
    private final Vector2f scaleRange = new Vector2f(0f, 1f);
    private boolean applyOnAssign = true;
    
    public EntityTransform() {}
    public EntityTransform(boolean applyOnAssign) {
        this.applyOnAssign = applyOnAssign;
    }
    public EntityTransform(EntityTransform transform) {
        translation.set(transform.translation);
        rotation.set(transform.rotation);
        scale.set(transform.scale);
        scaleRange.set(transform.scaleRange);
    }
    public EntityTransform(Transform transform) {
        transform.getTranslation(translation);
        transform.getRotation(rotation);
        transform.getScale(scale);
    }
    public EntityTransform(Ray ray) {
        translation.set(ray.getOrigin());
        rotation.set(new Quaternion().lookAt(ray.getDirection(), Vector3f.UNIT_Y));
    }
    
    // should be used during initialization only
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
    public EntityTransform setScaleRange(float min, float max) {
        scaleRange.set(min, max);
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
    public float getAverageScale() {
        return (scale.x+scale.y+scale.z)/3;
    }
    
    public void assignToSpatial(Spatial spatial) {
        if (applyOnAssign) {
            spatial.setLocalTranslation(translation);
            spatial.setLocalRotation(rotation);
            spatial.setLocalScale(scale);
        }
    }
    public Transform toJmeTransform() {
        return new Transform(translation, rotation, scale);
    }
    public Ray toRay() {
        return new Ray(translation, rotation.mult(Vector3f.UNIT_Z));
    }

    @Override
    public String toString() {
        return "EntityTransform{" + "location=" + translation + ", rotation=" + rotation + ", scale=" + scale + '}';
    }
    @Override
    public EntityTransform setPercent(float percent) {
        float s = FastMath.interpolateLinear(percent, scaleRange.x, scaleRange.y);
        return new EntityTransform(this).setScale(s);
    }    
    @Override
    public float getPercent() {
        return (getAverageScale()-scaleRange.x)/(scaleRange.y-scaleRange.x);
    }
    
}
