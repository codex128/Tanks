/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package school;

/**
 *
 * @author codex
 */
public class Vector3 {
    
    public double x, y, z;
    
    public Vector3() {
        x = y = z = 0;
    }
    public Vector3(double x, double y) {
        this(x, y, 0);
    }
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3 fromAngle(double degrees, double magnitude) {
        double rad = Math.toRadians(degrees);
        x = Math.cos(rad)*magnitude;
        y = Math.sin(rad)*magnitude;
        return this;
    }
    
    public double magnitude() {
        return Math.sqrt(x*x+y*y+z*z);
    }
    public double dot(Vector3 vec) {
        return x*vec.x + y*vec.y + z*vec.z;
    }
    public Vector3 cross(Vector3 vec) {
        return new Vector3(
            y*vec.z - z*vec.y,
            z*vec.x - x*vec.z,
            x*vec.y - y*vec.x
        );
    }
    public double angleBetween(Vector3 vec) {
        return Math.toDegrees(Math.acos(dot(vec)/(magnitude()*vec.magnitude())));
    }
    public Vector3 normalize() {
        double mag = magnitude();
        return new Vector3(x/mag, y/mag, z/mag);
    }

    @Override
    public String toString() {
        return "Vector3{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }
    
}
