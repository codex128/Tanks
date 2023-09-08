/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.util;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

/**
 *
 * @author codex
 */
public class PathMesh extends Mesh {
    
    private final Vector3f radius = new Vector3f();
    private float baseAngle;
    private int samples;
    
    public PathMesh() {
        this(3, .2f, 0f);
    }
    public PathMesh(int samples) {
        this(samples, .2f, 0f);
    }
    public PathMesh(int samples, float radius) {
        this(samples, radius, 0f);
    }
    public PathMesh(int samples, float radius, float baseAngle) {
        this.samples = samples;
        this.radius.z = radius;
        this.baseAngle = baseAngle;
    }
    
    public void setPoints(Vector3f[] points) {
        if (points.length <= 1) {
            throw new IllegalArgumentException("Laser must have at least two points!");
        }
        System.out.println(points.length);
        int chunkSize = samples*2;
        var verts = new Vector3f[(points.length-1)*chunkSize];
        var uvs = new Vector2f[verts.length];
        var index = new int[(points.length-1)*(samples > 2 ? samples : samples-1)*6];
        for (int i = 1; i < points.length; i++) {
            createChunk(verts, uvs, index, i-1, points[i-1], points[i]);
        }
        setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(verts));
        setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(uvs));
        setBuffer(VertexBuffer.Type.Index,    3, BufferUtils.createIntBuffer(index));
        updateBound();
        setStatic();
    }
    private void createChunk(Vector3f[] verts, Vector2f[] uvs, int[] index, int chunk, Vector3f a, Vector3f b) {
        /* 2---0
           |\  |
        <- | \ | <-
           |  \|
           3---1 */
        int c = chunk*samples*2;
        var q = new Quaternion();
        float uvPos = 0f;
        for (int i = 0; i < samples*2;) {
            q.fromAngleAxis(FastMath.TWO_PI/samples*i+baseAngle, b.subtract(a).normalizeLocal());
            verts[c+i] = q.mult(radius).addLocal(a);
            uvs[c+i++] = new Vector2f(uvPos, 0f);
            verts[c+i] = q.mult(radius).addLocal(b);
            uvs[c+i++] = new Vector2f(uvPos, 1f);
            uvPos += 1f/(samples-1);
        }
        int i = 0;
        while (i < samples*2-2) {
            assignIndices(chunk, i/2, index, i, i+2, i+1, i+2, i+3, i+1);
            i += 2;
        }
        if (samples > 2) {
            assignIndices(chunk, i/2, index, i, 0, i+1, 0, 1, i+1);
        }
    }
    private void assignIndices(int chunk, int sample, int[] index, int... values) {
        for (int i = 0; i < values.length; i++) {
            index[chunk*samples*6+sample*6+i] = chunk*samples*6+values[i];
        }
    }

    public void setRadius(float radius) {
        this.radius.z = radius;
    }
    public void setBaseAngle(float baseAngle) {
        this.baseAngle = baseAngle;
    }
    public void setSamples(int samples) {
        this.samples = samples;
    }

    public float getRadius() {
        return radius.z;
    }
    public float getBaseAngle() {
        return baseAngle;
    }
    public int getSamples() {
        return samples;
    }
    
}
