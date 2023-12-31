/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.tanks.systems.ContactState;
import codex.tanks.components.Bounces;
import codex.tanks.systems.AIManager;
import codex.tanks.systems.ProjectileState;
import codex.tanks.collision.*;
import codex.tanks.components.*;
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
    private final Entity entity;
    private final float tpf;
    private final boolean satisfied;
    private Entity target;
    private ContactState collision;
    private ProjectileState projectileState;
    
    public AlgorithmUpdate(AIManager manager, Entity entity, float tpf) {
        this.manager = manager;
        this.entity = entity;
        //this.tank = manager.getState(TankState.class).getTank(id);
        this.tpf = tpf;
        satisfied = initialize();
    }
    
    private boolean initialize() {
        collision = manager.getState(ContactState.class);
        projectileState = manager.getState(ProjectileState.class);
        return true;
    }
    public Vector3f getDirectionToTarget() {
        return getTargetComponent(EntityTransform.class).getTranslation().subtract(entity.get(EntityTransform.class).getTranslation()).normalizeLocal();
    }
    public float getDistanceToTarget() {
        return getTargetComponent(EntityTransform.class).getTranslation().distance(getComponent(EntityTransform.class).getTranslation());
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
    
    public Entity createRaycastEntity(Ray ray) {
        var tester = getEntityData().createEntity();
        getEntityData().setComponents(tester,
            new EntityTransform(),
            new Velocity(ray.direction, -1f),
            new Damage(0f),
            new Bounces(entity.get(Bounces.class)),
            new Alive()
        );
        return getEntityData().getEntity(tester, ProjectileState.COMPONENT_TYPES);
    }
    public boolean isTargetInView() {
        var raytest = new BasicRaytest(new Ray(entity.get(ProbeLocation.class).getLocation(), getDirectionToTarget()), ShapeFilter.none(ShapeFilter.byId(getAgentId())));
        raytest.cast(collision);
        return target.getId().equals(raytest.getCollisionEntity());
    }
    public boolean checkIsEndangeringTeam() {
        var e = createRaycastEntity(getAimRay());
        var margin = ShapeFilter.and(ShapeFilter.none(ShapeFilter.byTeam(getComponent(Team.class).getTeam())), ShapeFilter.byGameObject("tank"));
        SegmentedRaytest raytest = new SegmentedRaytest(getCollisionState());
        raytest.setRay(e.get(EntityTransform.class).toRay());
        raytest.setDistance(-1f);
        raytest.setFirstCastFilter(ShapeFilter.notId(getAgentId()));
        raytest.setOriginEntity(getAgentId());
        var iterator = getProjectileState().raytest(e, raytest);
        getEntityData().removeEntity(e.getId());
//        if (iterator.getMarginTestResults().size() > 0) {
//            return true;
//        }
        if (iterator.getCollisionEntity() != null) {
            var team = getEntityData().getComponent(iterator.getCollisionEntity(), Team.class);
            if (team != null && team.getTeam() == getComponent(Team.class).getTeam()) {
                return true;
            }
        }
        return false;
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
    public ContactState getCollisionState() {
        return collision;
    }
    public ProjectileState getProjectileState() {
        return projectileState;
    }
    public EntitySet getBullets() {
        return projectileState.getBullets();
    }
        
}
