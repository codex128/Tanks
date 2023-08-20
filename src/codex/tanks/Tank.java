/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResult;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author gary
 */
public class Tank implements CollisionShape, Savable {

    private Node root;
    private Spatial base, turret, muzzle, hitbox, pointer, probe;
    private RigidBodyControl physics;
    private LinkedList<BulletInfo> bullets = new LinkedList<>();
    private final TankModel model;
    private final int team;
    
    private float reload = 0f;
    private boolean alive = true;
    private int lastDir = 1;
    
    public Tank(Node root, TankModel model, int team) {
        this.root = root;
        fetchComponents();
        this.model = model;
        this.team = team;
    }
    
    private void fetchComponents() {
        base = root.getChild("base");
        turret = root.getChild("turret");
        muzzle = root.getChild("muzzle");
        hitbox = root.getChild("hitbox");
        pointer = root.getChild("pointer");
        probe = root.getChild("probe");
        hitbox.setCullHint(Spatial.CullHint.Always);
        pointer.setCullHint(Spatial.CullHint.Always);
    }
    public RigidBodyControl initPhysics() {
        //CollisionShape shape = ;
        physics = new RigidBodyControl(CollisionShapeFactory.createMergedHullShape(hitbox), 2000f);
        physics.setAngularFactor(0f);
        //rbc.setFriction(0f);
        root.addControl(physics);
        //rbc.setCollisionShape();
        return physics;
    }
    
    public void update(float tpf) {
        if ((reload -= tpf) < 0f) reload = 0f;
        for (Iterator<BulletInfo> i = bullets.iterator(); i.hasNext();) {
            BulletInfo b = i.next();
            if (!b.isAlive()) i.remove();
        }
    }
    public void move(Vector3f move, float tpf) {
        if (rotateTo(move)) {
            setLinearVelocity(move.mult(model.getSpeed()));
        }
        else {
            stop();
        }
    }
    public void move(int direction) {
        setLinearVelocity(getMoveDirection().mult(model.getSpeed()*direction));
        lastDir = direction;
    }
    private void setLinearVelocity(Vector3f vel) {
        vel.setY(physics.getLinearVelocity().getY());
        physics.setLinearVelocity(vel);
        physics.activate();
    }
    public void rotate(float angle) {
        base.rotate(new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Y));
    }
    public boolean rotateTo(Vector3f direction) {
        final float bias = .1f;
        final float blend = .2f;
        final float threshold = FastMath.PI*.3f;
        direction.setY(0f).normalizeLocal();
        Quaternion q = base.getLocalRotation().clone();
        Quaternion t1 = new Quaternion().lookAt(direction, Vector3f.UNIT_Y);
        Quaternion t2 = new Quaternion().lookAt(direction.negate(), Vector3f.UNIT_Y);
        if (q.dot(t1)+bias*lastDir >= q.dot(t2)) {
            q.nlerp(t1, blend);
            lastDir = 1;
        }
        else {
            q.nlerp(t2, blend);
            lastDir = -1;
        }
        base.setLocalRotation(q);
        float angle = direction.angleBetween(getMoveDirection());
        return angle < threshold || angle > FastMath.PI-threshold;
    }
    public void stop() {
        setLinearVelocity(Vector3f.ZERO.clone());
    }
    
    public BulletInfo shoot() {
        if (!bulletAvailable()) return null;
        BulletInfo b = new BulletInfo(this, muzzle.getWorldTranslation(), getAimDirection().multLocal(model.getBulletSpeed()), model.getMaxBounces(), 10f);
        bullets.add(b);
        reload = model.getRps();
        return b;
    }
    public void rotateAim(float angle) {
        turret.rotate(new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Y));
    }
    public void aimAt(Vector3f target) {
        Vector3f dir = target.subtract(turret.getWorldTranslation()).setY(0f).normalizeLocal();
        Quaternion q = new Quaternion().lookAt(dir, Vector3f.UNIT_Y);
        turret.setLocalRotation(q);
    }
    public CollisionShape probeAim(Collection<CollisionShape> shapes, int maxBounces) {
        return GameUtils.target(shapes, getAimRay(), this,
                (maxBounces >= 0 ? Math.min(model.getMaxBounces(), maxBounces) : model.getMaxBounces()));
    }
    public Ray getAimRay() {
        return new Ray(muzzle.getWorldTranslation(), getAimDirection());
    }
    
    public Vector3f getPosition() {
        return physics.getPhysicsLocation();
    }
    public Vector3f getAimDirection() {
        return turret.getLocalRotation().mult(Vector3f.UNIT_Z);
    }
    public Vector3f getMoveDirection() {
        return base.getLocalRotation().mult(Vector3f.UNIT_Z);
    }
    public Vector3f getProbeLocation() {
        return probe.getWorldTranslation();
    }
    public Node getRootSpatial() {
        return root;
    }
    public Spatial getPointerMesh() {
        return pointer;
    }
    public RigidBodyControl getPhysics() {
        return physics;
    }
    public TankModel getModel() {
        return model;
    }
    public int getTeam() {
        return team;
    }
    
    public boolean bulletAvailable() {
        return reload <= 0 && (model.getMaxBullets() < 0 || bullets.size() < model.getMaxBullets());
    }
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void onHit(Bullet bullet, CollisionResult collision) {
        alive = false;
        bullet.getBulletInfo().kill();
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
