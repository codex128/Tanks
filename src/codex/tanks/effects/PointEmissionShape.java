/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.effects;

import com.epagagames.particles.EmitterShape;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author codex
 */
public class PointEmissionShape extends EmitterShape {
    
    @Override
    public void setNext() {}
    @Override
    public void setNext(int index) {}
    @Override
    public int getIndex() {
        return -1;
    }
    @Override
    public Vector3f getNextTranslation() {
        return Vector3f.ZERO;
    }
    @Override
    public Vector3f getRandomTranslation() {
        return Vector3f.ZERO;
    }
    @Override
    public Vector3f getNextDirection() {
        return Vector3f.ZERO;
    }
    @Override
    public Spatial getDebugShape(Material mat, boolean ignoreTransforms) {
        var sphere = new Sphere(5, 8, 0.1f);
        Geometry geometry = new Geometry("DebugShape", sphere);
        geometry.setMaterial(mat);
        return geometry;
    }
    
}
