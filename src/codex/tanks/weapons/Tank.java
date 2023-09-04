/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.weapons;

import codex.j3map.J3map;
import codex.tanks.components.*;
import codex.tanks.effects.MatChange;
import codex.tanks.effects.MaterialChangeBucket;
import codex.tanks.util.GameUtils;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.shader.VarType;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

/**
 *
 * @author gary
 */
public class Tank {

    private final Spatial spatial;
    private final Entity entity;
    private Spatial base, turret, probe, muzzle;
    private final MaterialChangeBucket matBucket = new MaterialChangeBucket();
    private final Spatial[] wheels = new Spatial[4];
    private RigidBodyControl physics;
    private final Vector2f treadOffset = new Vector2f();
    private final Vector2f nextTreadMove = new Vector2f();
    private final float treadSpeed = -0.002f;
    private final float wheelSpeedRatio = -15f;
    
    //private float reload = 0f;
    private int drive = 1;
    
    public Tank(Spatial spatial, Entity entity) {
        this.spatial = spatial;
        this.entity = entity;
        initialize();
    }
    
    private void initialize() {
        base = GameUtils.getChild(spatial, "base");
        turret = GameUtils.getChild(spatial, "turret");
        muzzle = GameUtils.getChild(spatial, "muzzle");
        probe = GameUtils.getChild(spatial, "probe");
        Spatial hitbox = GameUtils.getChild(spatial, "hitbox");
        wheels[0] = GameUtils.getChild(spatial, "wheel.FL");
        wheels[1] = GameUtils.getChild(spatial, "wheel.BL");
        wheels[2] = GameUtils.getChild(spatial, "wheel.FR");
        wheels[3] = GameUtils.getChild(spatial, "wheel.BR");
        physics = new RigidBodyControl(CollisionShapeFactory.createMergedHullShape(hitbox), 2000f);
        physics.setAngularFactor(0f);
        spatial.addControl(physics);
    }
    
    public void update(float tpf) {
        //if ((reload -= tpf) < 0f) reload = 0f;
        //bullets.applyChanges();
        drive(entity.get(MoveVelocity.class).getMove());
        aimAtDirection(entity.get(AimDirection.class).getAim());
        entity.set(new Forward(getDriveDirection()));
        //entity.set(new MuzzlePosition(muzzle.getWorldTranslation()));
        entity.set(new MoveVelocity(Vector3f.ZERO));
        entity.set(new ProbeLocation(probe.getWorldTranslation()));
        if (!nextTreadMove.equals(Vector2f.ZERO)) {
            moveRightTread(nextTreadMove.y);
            moveLeftTread(nextTreadMove.x);
            nextTreadMove.set(0f, 0f);
        }
    }
    
    private void drive(Vector3f move) {
        if (move.equals(Vector3f.ZERO)) {
            setLinearVelocity(move);
            return;
        }
        if (rotateTo(move.clone())) {
            //var s = entity.get(MaxSpeed.class).getSpeed();
            setLinearVelocity(move);
            final float treadMovement = move.length()*drive*treadSpeed;
            nextTreadMove.addLocal(treadMovement, treadMovement);
        }
        else {
            setLinearVelocity(Vector3f.ZERO);
        }
    }
    private void setLinearVelocity(Vector3f velocity) {
        entity.set(new LinearVelocity(GameUtils.merge(velocity, entity.get(LinearVelocity.class).getVelocity(), 1, -1, 1)));
    }
    private boolean rotateTo(Vector3f direction) {
        final float threshold = .6f;
        direction.setY(0f).normalizeLocal().multLocal(drive);
        var move = base.getLocalRotation().mult(Vector3f.UNIT_Z);
        var q = new Quaternion().lookAt(direction, Vector3f.UNIT_Y);
        var factor = move.dot(direction);
        if (factor >= 0f) {
            // rotate current drive direction to match direction
            float turn = entity.get(TurnSpeed.class).getSpeed();
            if (move.angleBetween(direction) > turn) {
                rotate(-turn*FastMath.sign(move.dot(q.getRotationColumn(0))));
            }
            else {
                base.setLocalRotation(q);
            }
        }
        else {
            // reverse the drive direction
            drive = -drive;
        }
        return factor >= 1f-threshold;
    }
    private void rotate(float angle) {
        final float treadMoveMovement = angle*treadSpeed;
        final float isRight = FastMath.sign(angle);
        base.rotate(0f, angle, 0f);
        nextTreadMove.addLocal(treadMoveMovement*isRight, -treadMoveMovement*isRight);
    }
    private void aimAtDirection(Vector3f direction) {
        turret.setLocalRotation(new Quaternion().lookAt(direction, Vector3f.UNIT_Y));
    }    
    private void moveRightTread(float amount) {
        treadOffset.y += amount;
        matBucket.add(new MatChange("TreadOffset2", VarType.Float, treadOffset.y));
        Quaternion q = new Quaternion().fromAngleAxis(amount*wheelSpeedRatio, Vector3f.UNIT_X);
        wheels[2].rotate(q);
        wheels[3].rotate(q);
    }
    private void moveLeftTread(float amount) {
        treadOffset.x += amount;
        matBucket.add(new MatChange("TreadOffset2", VarType.Float, treadOffset.y));
        Quaternion q = new Quaternion().fromAngleAxis(amount*wheelSpeedRatio, Vector3f.UNIT_X);
        wheels[0].rotate(q);
        wheels[1].rotate(q);
    }
    
    public Ray getAimRay() {
        return new Ray(entity.get(MuzzlePosition.class).getPosition(), entity.get(AimDirection.class).getAim());
    }
    
    private Vector3f getDriveDirection() {
        return base.getLocalRotation().mult(Vector3f.UNIT_Z).mult(drive);
    }
    public Vector3f getProbeLocation() {
        return probe.getWorldTranslation();
    }
    public Vector3f getMuzzleLocation() {
        return muzzle.getWorldTranslation();
    }
    public Spatial getSpatial() {
        return spatial;
    }
    public MaterialChangeBucket getMatChanges() {
        return matBucket;
    }
    public RigidBodyControl getPhysics() {
        return physics;
    }
    public Entity getEntity() {
        return entity;
    }
    
    public static void applyProperties(EntityData ed, EntityId id, J3map source) {
        ed.setComponents(id,
            new MaxSpeed(source.getFloat("speed", 6f)),
            new Firerate(source.getFloat("rps", 1f)),
            new BulletCapacity(source.getInteger("maxBullets", 5)),
            new Bounces(source.getInteger("maxBounces", 1)),
            new Power(source.getFloat("bulletSpeed", 10f)),
            new MineCapacity(source.getInteger("maxMines", 2)),
            new MaterialUpdate(
                new MatChange("MainColor", VarType.Vector4, source.getProperty(ColorRGBA.class, "color1", ColorRGBA.Blue)),
                new MatChange("SecondaryColor", VarType.Vector4, source.getProperty(ColorRGBA.class, "color2", ColorRGBA.DarkGray))));
    }
    
}
