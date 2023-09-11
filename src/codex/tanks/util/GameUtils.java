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
import com.jme3.scene.shape.Sphere;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.lemur.Axis;
import java.util.Collection;
import java.util.function.BiConsumer;

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
    public static Vector3f merge(Vector3f a, Vector3f b, int x, int y, int z) {
        return new Vector3f(merge(a.x, b.x, x), merge(a.y, b.y, y), merge(a.z, b.z, z));
    }
    public static float merge(float a, float b, int dir) {
        if (dir > 0) return a;
        else if (dir < 0) return b;
        else return 0f;
    }
    
    public static float random(float min, float max) {
        return FastMath.rand.nextFloat()*(max-min)+min;
    }
    public static float gaussian(float mean, float radius) {
        return (float)FastMath.rand.nextGaussian()*radius+mean;
    }
    
    public static Geometry createDebugCube(AssetManager assetManager, ColorRGBA color, Vector3f size) {
        Geometry g = new Geometry("debug-cube", new Box(size.x, size.y, size.z));
        Material m = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        m.setBoolean("UseMaterialColors", true);
        m.setColor("Diffuse", color);
        g.setMaterial(m);
        return g;
    }
    public static Geometry createDebugCube(AssetManager assetManager, ColorRGBA color, float radius) {
        return GameUtils.createDebugCube(assetManager, color, new Vector3f(radius, radius, radius));
    }
    public static Geometry createDebugSphere(AssetManager assetManager, ColorRGBA color, float radius) {
        var sphere = new Sphere(8, 10, radius);
        var geometry = new Geometry("debug-sphere", sphere);
        Material m = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        m.setBoolean("UseMaterialColors", true);
        m.setColor("Diffuse", color);
        geometry.setMaterial(m);
        return geometry;
    }
    
    public static Geometry fetchGeometry(Spatial spatial) {
        for (Spatial s : new SceneGraphIterator(spatial)) {
            if (s instanceof Geometry) {
                return (Geometry)s;
            }
        }
        return null;
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
    
    public static float distance2D(Vector3f a, Vector3f b, Axis omit) {
        return switch (omit) {
            case X -> distance2D(a.z, a.y, b.z, b.y);
            case Y -> distance2D(a.x, a.z, b.x, b.z);
            case Z -> distance2D(a.x, a.y, b.x, b.y);
        };
    }
    private static float distance2D(float x1, float y1, float x2, float y2) {
        return FastMath.sqrt(FastMath.sqr(x1-x2)+FastMath.sqr(y1-y2));
    }
    
    public static <T, R> void extract(Collection<T> in, Collection<R> out, BiConsumer<Collection<R>, T> apply) {
        for (T e : in) {
            apply.accept(out, e);
        }
    }
    
    public static void removeAllComponents(EntityData ed, EntityId id) {
        
    }
    
}
