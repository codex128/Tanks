/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.util;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import java.util.Collection;

/**
 *
 * @author codex
 */
public class LaserGeometry extends Geometry {
    
    private float radius = .1f;
    private boolean useWorldSpace = true;
    
    public void setPointData(Collection<Vector3f> points) {
        
    }
    private Vector3f[] createSegment(Vector3f a, Vector3f b) {
        int c = 0; // segment chunk (chunk index * chunk size)
        final int s = 3; // radial samples
        final var direction = b.subtract(a);
        final var offset = new Vector3f(0f, 0f, radius);
        var verts = new Vector3f[s*2];
        // assign vertices for vector a
        for (int i = 0; i < s; i++) {
            verts[c+i] = new Quaternion().fromAngleAxis(FastMath.TWO_PI/s*i, direction).mult(offset);
            verts[c+i].addLocal(a);
        }
        // assign vertices for vector b
        for (int i = 0; i < s; i++) {
            verts[c+i+s] = new Quaternion().fromAngleAxis(FastMath.TWO_PI/s*i, direction).mult(offset);
            verts[c+i+s].addLocal(b);
        }
        // assign uvs
        var uvs = new Vector2f[s*2];
        for (int i = 0; i < s; i++) {
            float near = 1f/(i*s);
            float far = 1f/(i*(s+1));
            uvs[c+i] = new Vector2f(near, 0f);
            //uvs[c+]
        }
        // assign indices
        var indices = new int[s*6];
        int j = 0;
        for (int i = 0; i < s; i++) {
            indices[c+j++] = c+i;
            indices[c+j++] = c+i+1;
            indices[c+j++] = c+i+s;
            indices[c+j++] = c+i+1;
            indices[c+j++] = c+i+s+1;
            indices[c+j++] = c+i+s;
        }
        return null;
    }
    
}
