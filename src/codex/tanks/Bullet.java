/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.IOException;
import java.util.Collection;

/**
 *
 * @author gary
 */
public class Bullet implements CollisionShape, Savable {
    
    private Node root;
    private Spatial bullet, hitbox, emitter;
    private BulletInfo info;
    private int bouncesMade = 0;
    
    public Bullet(Node root, BulletInfo info) {
        this.root = root;
        this.info = info;
        fetchComponents();
    }
    
    private void fetchComponents() {
        bullet = root.getChild("bullet");
        hitbox = root.getChild("hitbox");
        emitter = root.getChild("emitter");
        hitbox.setCullHint(Spatial.CullHint.Always);
    }
    
    public void update(float tpf) {
        info.life -= tpf;
        root.setLocalRotation(new Quaternion().lookAt(info.velocity.negate(), Vector3f.UNIT_Y));
    }
    public CollisionResult raycast(Collection<CollisionShape> shapes, float tpf) {
        CollisionResults res = GameUtils.raycast(shapes, new Ray(getPosition(), info.getDirection()), this);
        if (res.size() > 0) {
            CollisionResult closest = res.getClosestCollision();
            if (closest.getDistance() < info.velocity.length()*tpf*2) {
                return closest;
            }
        }
        return null;
    }
    public void move(float tpf) {
        root.move(info.velocity.mult(tpf));
    }
    public void ricochet(Vector3f normal) {
        if (info.bounces-- > 0) {
            Vector3f dir = info.getDirection();
            info.velocity.set(GameUtils.ricochet(dir, normal).multLocal(info.velocity.length()));
            bouncesMade++;
        }
    }
    
    public Node getRoot() {
        return root;
    }
    public Vector3f getPosition() {
        return root.getWorldTranslation();
    }
    public BulletInfo getBulletInfo() {
        return info;
    }
    public int getBouncesMade() {
        return bouncesMade;
    }

    @Override
    public void onHit(Bullet bullet, CollisionResult collision) {
        bullet.getBulletInfo().kill();
        info.kill();
    }
    @Override
    public Spatial getCollisionShape() {
        return hitbox;
    }
    @Override
    public void write(JmeExporter ex) throws IOException {}
    @Override
    public void read(JmeImporter im) throws IOException {}
    
}
