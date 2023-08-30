/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks;

import codex.j3map.J3map;
import codex.tanks.components.*;
import codex.tanks.systems.BulletState;
import codex.tanks.systems.VisualState;
import codex.tanks.util.FunctionFilter;
import codex.tanks.util.GameUtils;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;

/**
 *
 * @author gary
 */
public class Tank {

    private final Spatial spatial;
    private final Entity entity;
    private Spatial base, turret, muzzle, hitbox, pointer, probe, shield;
    private final Spatial[] wheels = new Spatial[4];
    private Material material;
    private RigidBodyControl physics;
    private EntitySet bullets;
    private final Vector2f treadOffset = new Vector2f();
    private final Vector2f nextTreadMove = new Vector2f();
    private final float treadSpeed = -0.002f;
    private final float wheelSpeedRatio = -15f;
    
    private float reload = 0f;
    private int drive = 1;
    
    public Tank(Spatial spatial, Entity entity, EntityData ed) {
        this.spatial = spatial;
        this.entity = entity;
        initialize(ed);
    }
    
    private void initialize(EntityData ed) {
        base = GameUtils.getChild(spatial, "base");
        turret = GameUtils.getChild(spatial, "turret");
        muzzle = GameUtils.getChild(spatial, "muzzle");
        hitbox = GameUtils.getChild(spatial, "hitbox");
        pointer = GameUtils.getChild(spatial, "pointer");
        probe = GameUtils.getChild(spatial, "probe");
        shield = GameUtils.getChild(muzzle, "shield");
        wheels[0] = GameUtils.getChild(spatial, "wheel.FL");
        wheels[1] = GameUtils.getChild(spatial, "wheel.BL");
        wheels[2] = GameUtils.getChild(spatial, "wheel.FR");
        wheels[3] = GameUtils.getChild(spatial, "wheel.BR");
        hitbox.setCullHint(Spatial.CullHint.Always);
        pointer.setCullHint(Spatial.CullHint.Always);
        shield.setCullHint(Spatial.CullHint.Always);
        var scheme = entity.get(ColorScheme.class);
        scheme.verifySize(2);
        material = GameUtils.fetchMaterial(spatial);
        material.setColor("MainColor", scheme.getPallete()[0]);
        material.setColor("SecondaryColor", scheme.getPallete()[1]);
        bullets = ed.getEntities(new FunctionFilter<>(Owner.class, c -> c.isOwner(entity.getId())), Owner.class);
        initPhysics();
    }
    private void initPhysics() {
        physics = new RigidBodyControl(CollisionShapeFactory.createMergedHullShape(hitbox), 2000f);
        physics.setAngularFactor(0f);
        spatial.addControl(physics);
    }
    public void cleanup() {
        bullets.release();
    }
    
    public void update(float tpf) {
        if ((reload -= tpf) < 0f) reload = 0f;
        bullets.applyChanges();
        if (!nextTreadMove.equals(Vector2f.ZERO)) {
            moveRightTread(nextTreadMove.y);
            moveLeftTread(nextTreadMove.x);
            nextTreadMove.set(0f, 0f);
        }
    }
    public void drive(Vector3f move) {
        if (rotateTo(move)) {
            var s = entity.get(Speed.class).getSpeed();
            setLinearVelocity(move.mult(s));
            final float treadMovement = s*drive*treadSpeed;
            nextTreadMove.addLocal(treadMovement, treadMovement);
        }
        else {
            stop();
        }
    }
    public int drive(float factor) {
        //direction = FastMath.sign(direction);
        final float speed = entity.get(Speed.class).getSpeed();
        final float treadMovement = speed*factor*treadSpeed*drive;
        setLinearVelocity(getDriveDirection().mult(speed*factor));
        nextTreadMove.addLocal(treadMovement, treadMovement);
        if (factor < 0) {
            drive *= -1;
            return -1;
        }
        return 1;
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
        final float threshold = .7f;
        direction.setY(0f).normalizeLocal();
        var move = getDriveDirection();
        var q = new Quaternion().lookAt(direction, Vector3f.UNIT_Y);
        var factor = move.dot(direction);
        if (factor >= 0f) {
            // rotate current drive direction to match direction
            if (move.angleBetween(direction) > 0.1f) {
                rotate(-0.1f*FastMath.sign(move.dot(q.getRotationColumn(0))));
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
    public void stop() {
        setLinearVelocity(Vector3f.ZERO.clone());
    }
    
    public EntityId shoot(EntityData ed, VisualState visuals) {
        if (!bulletAvailable()) return null;
        var bullet = ed.createEntity();
        ed.setComponents(bullet,
            new GameObject("bullet"),
            new Visual(BulletState.getBulletModelId(entity.get(Power.class).getPower())),
            new EntityTransform().setTranslation(muzzle.getWorldTranslation()).setScale(.17f),
            new TransformMode(1, 1, 0),
            new Velocity(getAimDirection().multLocal(entity.get(Power.class).getPower())),
            new FaceVelocity(),
            new Bounces(entity.get(Bounces.class).getRemaining()),
            new CollisionShape("hitbox"),
            new ContactReaction(ContactReaction.DIE),
            new Owner(entity.getId()),
            new Alive()
        );
        var flash = ed.createEntity();
        ed.setComponents(flash,
                new GameObject("light"),
                new EntityLight(EntityLight.POINT),
                new EntityTransform().setTranslation(muzzle.getWorldTranslation()),
                new Decay(.03f),
                new Alive(),
                new Power(50f),
                new ColorScheme(ColorRGBA.Orange));
        if (BulletState.isMissile(entity.get(Power.class).getPower())) {
            var light = ed.createEntity();
            ed.setComponents(light,
                    new GameObject("light"),
                    new EntityLight(EntityLight.POINT),
                    new EntityTransform(),
                    new TransformMode(1, 1, 1),
                    new Copy(bullet, Copy.TRANSFORM, Copy.LIFE),
                    new Alive(),
                    new Power(100f),
                    new ColorScheme(ColorRGBA.Orange));
        }
//        var smokeEntity = ed.createEntity();
//        ed.setComponents(smokeEntity,
//                new GameObject("particle-emitter"),
//                new Visual(ModelFactory.BULLET_SMOKE).setIndependent(true),
//                new EntityTransform(),
//                new TransformMode(1, 0, 0),
//                new Copy(bullet, Copy.TRANSFORM, Copy.LIFE),
//                new Alive());
//        var shield = ed.createEntity();
//        ed.setComponents(shield,
//                new Visual(),
//                new CollisionShape(),
//                new ContactReaction(ContactReaction.SIMPLE),
//                new Decay(10f),
//                new Alive());
//        visuals.link(shield, this.shield);
        reload = entity.get(Firerate.class).getRate();
        return bullet;
    }
    public void rotateAim(float angle) {
        turret.rotate(new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Y));
    }
    public void aimAt(Vector3f target) {
        Vector3f dir = target.subtract(turret.getWorldTranslation()).setY(0f).normalizeLocal();
        Quaternion q = new Quaternion().lookAt(dir, Vector3f.UNIT_Y);
        turret.setLocalRotation(q);
    }
    public void aimAtDirection(Vector3f direction) {
        turret.setLocalRotation(new Quaternion().lookAt(direction, Vector3f.UNIT_Y));
    }
    public Ray getAimRay() {
        return new Ray(muzzle.getWorldTranslation(), getAimDirection());
    }
    
    private void moveRightTread(float amount) {
        treadOffset.y += amount;
        material.setFloat("TreadOffset2", treadOffset.y);
        Quaternion q = new Quaternion().fromAngleAxis(amount*wheelSpeedRatio, Vector3f.UNIT_X);
        wheels[2].rotate(q);
        wheels[3].rotate(q);
    }
    private void moveLeftTread(float amount) {
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
    public Vector3f getDriveDirection() {
        return getForwardDirection().multLocal(drive);
    }
    public Vector3f getForwardDirection() {
        return base.getLocalRotation().mult(Vector3f.UNIT_Z);
    }
    public Vector3f getProbeLocation() {
        return probe.getWorldTranslation();
    }
    public Spatial getSpatial() {
        return spatial;
    }
    public Spatial getPointerMesh() {
        return pointer;
    }
    public RigidBodyControl getPhysics() {
        return physics;
    }
    public Entity getEntity() {
        return entity;
    }
    
    public boolean bulletAvailable() {
        int max = entity.get(BulletCapacity.class).getMax();
        return reload <= 0 && (max < 0 || bullets.size() < max);
    }
    public boolean ownsBullet(Bullet b) {
        for (var e : bullets) {
            if (e.getId().equals(b.getEntity().getId())) {
                return true;
            }
        }
        return false;
    }
    
    public static void applyProperties(EntityData ed, EntityId id, J3map source) {
        ed.setComponents(id,
                new Speed(source.getFloat("speed", 6f)),
                new Firerate(source.getFloat("rps", 1f)),
                new BulletCapacity(source.getInteger("maxBullets", 5)),
                new Bounces(source.getInteger("maxBounces", 1)),
                new Power(source.getFloat("bulletSpeed", 10f)),
                new MineCapacity(source.getInteger("maxMines", 2)),
                new ColorScheme(
                        source.getProperty(ColorRGBA.class, "color1", ColorRGBA.Blue),
                        source.getProperty(ColorRGBA.class, "color2", ColorRGBA.DarkGray)));
    }
    
}
