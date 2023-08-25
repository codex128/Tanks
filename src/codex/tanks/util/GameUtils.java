/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.util;

import codex.boost.scene.SceneGraphIterator;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 *
 * @author gary
 */
public class GameUtils {
    
    public static final String OWNER = "collision-owner";
    
    public static CollisionResults raycast(Spatial scene, Ray ray, Spatial... ignore) {
        var results = new CollisionResults();
        var it = new SceneGraphIterator(scene);
        main: for (Spatial spatial : it) {
            for (Spatial i : ignore) {
                if (spatial == i) {
                    it.ignoreChildren();
                    continue main;
                }
            }
            if (spatial instanceof Geometry) {
                spatial.collideWith(ray, results);
            }
        }
        return results;
    }    
    public static Vector3f ricochet(Vector3f vector, Vector3f normal) {
        return normal.mult(normal.dot(vector)*-2).addLocal(vector).normalizeLocal();
    }    
    
    public static float random(float min, float max) {
        return FastMath.rand.nextFloat()*(max-min)+min;
    }
    public static float gaussian(float mean, float radius) {
        return (float)FastMath.rand.nextGaussian()*radius+mean;
    }
    
    public static Geometry createDebugGeometry(AssetManager assetManager, ColorRGBA color, Vector3f size) {
        Geometry g = new Geometry("debug", new Box(size.x, size.y, size.z));
        Material m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        m.setColor("Color", color);
        g.setMaterial(m);
        return g;
    }
    public static Geometry createDebugGeometry(AssetManager assetManager, ColorRGBA color, float radius) {
        return createDebugGeometry(assetManager, color, new Vector3f(radius, radius, radius));
    }
    public static Material fetchMaterial(Spatial spatial) {
        for (Spatial s : new SceneGraphIterator(spatial)) {
            if (s instanceof Geometry) {
                return ((Geometry)s).getMaterial();
            }
        }
        return null;
    }
    public static Spatial getChild(Spatial parent, String name) {
        for (Spatial s : new SceneGraphIterator(parent)) {
            if (s.getName().equals(name)) return s;
        }
        return null;
    }
    public static Node getChildNode(Spatial parent, String name) {
        for (Spatial s : new SceneGraphIterator(parent)) {
            if (s.getName().equals(name) && s instanceof Node) {
                return (Node)s;
            }
        }
        return null;
    }
    public static void visualizeHierarchy(Spatial spatial) {
        System.out.println("children of "+spatial.getName()+":");
        for (var s : new SceneGraphIterator(spatial)) {
            System.out.println("   "+s);
        }
    }
    
    public static void setWorldTranslation(Spatial spatial, Vector3f translation) {
        if (spatial.getParent() == null) {
            spatial.setLocalTranslation(translation);
        }
        else {
            spatial.setLocalTranslation(translation.subtract(spatial.getParent().getWorldTranslation()));
        }
    }
    
}
