/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks;

import codex.jmeutil.scene.SceneGraphIterator;
import com.jme3.asset.AssetManager;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.util.Collection;

/**
 *
 * @author gary
 */
public class GameUtils {
    
    public static final String OWNER = "collision-owner";
    
    public static CollisionResults raycast(Spatial scene, Collidable collider, Spatial... ignore) {
        CollisionResults results = new CollisionResults();
        SceneGraphIterator it = new SceneGraphIterator(scene);
        main: for (Spatial spatial : it) {
            for (Spatial i : ignore) {
                if (spatial == i) {
                    it.ignoreChildren();
                    continue main;
                }
            }
            if (spatial instanceof Geometry) {
                spatial.collideWith(collider, results);
            }
        }
        return results;
    }
    public static CollisionResults raycast(Collection<CollisionShape> shapes, Collidable collider, CollisionShape ignore) {
        CollisionResults results = new CollisionResults();
        raycast(shapes, collider, ignore, results);
        return results;
    }
    public static void raycast(Collection<CollisionShape> shapes, Collidable collider, CollisionShape ignore, CollisionResults results) {
        for (CollisionShape s : shapes) {
            if (ignore != null && ignore == s) continue;
            s.getCollisionShape().collideWith(collider, results);
        }
    }
    public static CollisionShape target(Collection<CollisionShape> shapes, Ray ray, CollisionShape ignore, int maxBounces) {
        CollisionResults results = new CollisionResults();
        do {
            raycast(shapes, ray, ignore, results);
            if (results.size() > 0) {
                CollisionResult closest = results.getClosestCollision();
                CollisionShape shape = fetchGeometryOwner(closest.getGeometry());
                if (shape != null) {
                    if (shape.ricochet()) {
                        ray = new Ray(closest.getContactPoint(), ricochet(ray.getDirection(), closest.getContactNormal()));
                        ignore = shape;
                    }
                    else return shape;
                }
                else break;
            }
        } while (maxBounces-- > 0);
        return null;
    }
    public static float getDistance(Collection<CollisionShape> shapes, Ray ray, CollisionShape ignore) {
        CollisionResults results = raycast(shapes, ray, ignore);
        if (results.size() > 0) {
            return results.getClosestCollision().getDistance();
        }
        return -1f;
    }
    
    public static void assignGeometryOwner(Spatial spatial, CollisionShape owner) {
        if (spatial instanceof Geometry) {
            spatial.setUserData(OWNER, owner);
        }
        else for (Spatial s : new SceneGraphIterator(spatial)) {
            if (s instanceof Geometry) {
                s.setUserData(OWNER, owner);
            }
        }
    }
    public static CollisionShape fetchGeometryOwner(Geometry geometry) {
        return geometry.getUserData(OWNER);
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
    
    public static Geometry createDebugGeometry(AssetManager assetManager, ColorRGBA color, float radius) {
        Geometry g = new Geometry("debug", new Box(radius, radius, radius));
        Material m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        m.setColor("Color", color);
        g.setMaterial(m);
        return g;
    }
    
}
