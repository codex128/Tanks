/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks;

import codex.tanks.util.GameUtils;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResult;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
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
public class Tank implements CollisionShape {

    private Node root;
    private Spatial base, turret, muzzle, hitbox, pointer, probe;
    private Spatial[] wheels = new Spatial[4];
    private Material material;
    private RigidBodyControl physics;
    private LinkedList<BulletInfo> bullets = new LinkedList<>();
    private final TankModel model;
    private final int team;
    private final Vector2f treadOffset = new Vector2f();
    private final Vector2f nextTreadMove = new Vector2f();
    private final float treadSpeed = -0.002f;
    private final float wheelSpeedRatio = -15f;
    
    private float reload = 0f;
    private boolean alive = true;
    private int lastDir = 1;
    
    public Tank(Node root, Material material, TankModel model, int team) {
        this.root = root;
        this.material = material;
        this.model = model;
        this.team = team;
        fetchComponents();
    }
    
    private void fetchComponents() {
        base = root.getChild("base");
        turret = root.getChild("turret");
        muzzle = root.getChild("muzzle");
        hitbox = root.getChild("hitbox");
        pointer = root.getChild("pointer");
        probe = root.getChild("probe");
        wheels[0] = root.getChild("wheel.FL");
        wheels[1] = root.getChild("wheel.BL");
        wheels[2] = root.getChild("wheel.FR");
        wheels[3] = root.getChild("wheel.BR");
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
        if (!nextTreadMove.equals(Vector2f.ZERO)) {
            moveRightTread(nextTreadMove.y);
            moveLeftTread(nextTreadMove.x);
            nextTreadMove.set(0f, 0f);
        }
    }
    public void move(Vector3f move) {
        if (rotateTo(move)) {
            setLinearVelocity(move.mult(model.getSpeed()));
            final float treadMovement = model.getSpeed()*lastDir*treadSpeed;
            nextTreadMove.addLocal(treadMovement, treadMovement);
        }
        else {
            stop();
        }
    }
    public void move(int direction) {
        direction = FastMath.sign(direction);
        final float treadMovement = model.getSpeed()*direction*treadSpeed;
        setLinearVelocity(getMoveDirection().mult(model.getSpeed()*direction));
        nextTreadMove.addLocal(treadMovement, treadMovement);
        lastDir = direction;
    }
    private void setLinearVelocity(Vector3f vel) {
        vel.setY(physics.getLinearVelocity().getY());
        physics.setLinearVelocity(vel);
        physics.activate();
    }
    public void rotate(float angle) {
        final float treadMoveMovement = angle*treadSpeed;
        final float isRight = FastMath.sign(angle);
        base.rotate(new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Y));
        nextTreadMove.addLocal(treadMoveMovement*isRight, -treadMoveMovement*isRight);
    }
    public boolean rotateTo(Vector3f direction) {
        final float bias = .1f;
        final float blend = .2f;
        final float threshold = FastMath.PI*.3f;
        direction.setY(0f).normalizeLocal();
        Quaternion q = base.getLocalRotation().clone();
        int isLeft = q.getRotationColumn(0).dot(direction) > 0f ? 1 : -1;
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
        nextTreadMove.addLocal(-treadSpeed*isLeft, treadSpeed*isLeft);
        float angle = direction.angleBetween(getMoveDirection());
        return angle < threshold || angle > FastMath.PI-threshold;
    }
    public void stop() {
        setLinearVelocity(Vector3f.ZERO.clone());
    }
    
    public BulletInfo shoot() {
        if (!bulletAvailable()) return null;
        BulletInfo b = new BulletInfo(this, BulletInfo.Type.valueOf(BulletInfo.Type.class, "Basic"), muzzle.getWorldTranslation(), getAimDirection().multLocal(model.getBulletSpeed()), model.getMaxBounces(), 10f);
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
    
    public void moveRightTread(float amount) {
        treadOffset.y += amount;
        material.setFloat("TreadOffset2", treadOffset.y);
        Quaternion q = new Quaternion().fromAngleAxis(amount*wheelSpeedRatio, Vector3f.UNIT_X);
        wheels[2].rotate(q);
        wheels[3].rotate(q);
    }
    public void moveLeftTread(float amount) {
        treadOffset.x += amount;
        material.setFloat("TreadOffset1", treadOffset.x);
        Quaternion q = new Quaternion().fromAngleAxis(amount*wheelSpeedRatio, Vector3f.UNIT_X);
        wheels[0].rotate(q);
        wheels[1].rotate(q);
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
