/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.tanks.components.Bounces;
import codex.tanks.systems.AIManager;
import codex.tanks.systems.BulletState;
import codex.tanks.collision.*;
import codex.tanks.components.*;
import codex.tanks.systems.VisualState;
import codex.tanks.systems.GunState;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityComponent;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;

/**
 *
 * @author gary
 */
public class AlgorithmUpdate {
    
    private final AIManager manager;
    //private final EntityId id;
    private final Entity entity;
    //private final Tank tank;
    private final float tpf;
    private final boolean satisfied;
    private Entity target;
    private VisualState visuals;
    private CollisionState collision;
    private BulletState bulletState;
    private Vector3f dirToTarget;
    private float distToTarget;
    
    public AlgorithmUpdate(AIManager manager, Entity entity, float tpf) {
        this.manager = manager;
        this.entity = entity;
        //this.tank = manager.getState(TankState.class).getTank(id);
        this.tpf = tpf;
        satisfied = initialize();
    }
    
    private boolean initialize() {
        visuals = manager.getState(VisualState.class);
        collision = manager.getState(CollisionState.class);
        bulletState = manager.getState(BulletState.class);
        return true;
    }
    public Vector3f getDirectionToTarget() {
        return getTargetComponent(EntityTransform.class).getTranslation().subtract(entity.get(EntityTransform.class).getTranslation()).normalizeLocal();
    }
    public float getDistanceToTarget() {
        return getTargetComponent(EntityTransform.class).getTranslation().distance(getComponent(EntityTransform.class).getTranslation());
    }
    public boolean isTargetInView() {
        var raytest = new BasicRaytest(new Ray(entity.get(ProbeLocation.class).getLocation(), getDirectionToTarget()), ShapeFilter.none(ShapeFilter.byId(getAgentId())));
        raytest.cast(collision);
        return target.getId().equals(raytest.getCollisionEntity());
    }
    public boolean isTargetInBounce() {
        var raytest = new LaserRaytest(getAimRay(), new OriginFilter(getAgentId(), null), getComponent(Bounces.class).getRemaining());
        raytest.cast(collision);
        return target.getId().equals(raytest.getCollisionEntity());
    }
    
    public void drive(Vector3f direction) {
        setComponent(new MoveVelocity(direction.multLocal(getComponent(MaxSpeed.class).getSpeed())));
    }
    public boolean shoot() {
        return manager.getState(GunState.class).shoot(entity.getId());
    }
    public Vector3f rotate(float angle) {
        return new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Y).mult(getComponent(Forward.class).getForward());
    }
    public Vector3f rotateAim(float angle) {
        return new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Y).mult(getComponent(AimDirection.class).getAim());
    }
    public Vector3f aimAt(Vector3f location) {
        location.setY(0f);
        Vector3f here = getComponent(EntityTransform.class).getTranslation().clone().setY(0f);
        return location.subtractLocal(here);
    }
    
    public AIManager getManager() {
        return manager;
    }
    public float getTpf() {
        return tpf;
    }
    public boolean isInfoSatisfied() {
        return satisfied;
    }
    
    public <T extends EntityComponent> T getComponent(Class<T> type) {
        return entity.get(type);
    }
    public void setComponent(EntityComponent component) {
        entity.set(component);
    }
    public <T extends EntityComponent> T getTargetComponent(Class<T> type) {
        return target.get(type);
    }
    
    public void setTarget(EntityId id) {
        target = getEntityData().getEntity(id, EntityTransform.class);
    }
    
    public EntityId getAgentId() {
        return entity.getId();
    }
    public Entity getTarget() {
        return target;
    }
    public Ray getAimRay() {
        return new Ray(getMuzzlePosition(), getMuzzleDirection());
    }    
    public Vector3f getMuzzlePosition() {
        return getEntityData().getComponent(entity.get(MuzzlePointer.class).getId(), EntityTransform.class).getTranslation();
    }
    public Vector3f getMuzzleDirection() {
        return getEntityData().getComponent(entity.get(MuzzlePointer.class).getId(), EntityTransform.class).getRotation().mult(Vector3f.UNIT_Z);
    }
    public EntityData getEntityData() {
        return manager.getEntityData();
    }
    public CollisionState getCollisionState() {
        return collision;
    }
    public EntitySet getBullets() {
        return bulletState.getBullets();
    }
        
}
