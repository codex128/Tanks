/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks;

import com.jme3.collision.CollisionResult;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.scene.Spatial;
import java.io.IOException;

/**
 *
 * @author gary
 */
public class Wall implements CollisionShape, Savable {

    private Spatial spatial;
    
    public Wall(Spatial spatial) {
        this.spatial = spatial;
    }
    
    @Override
    public void onHit(Bullet bullet, CollisionResult collision) {
        bullet.ricochet(collision.getContactNormal());
    }
    @Override
    public Spatial getCollisionShape() {
        return spatial;
    }
    @Override
    public boolean ricochet() {
        return true;
    }
    @Override
    public void write(JmeExporter ex) throws IOException {}
    @Override
    public void read(JmeImporter im) throws IOException {}
    
}
