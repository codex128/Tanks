/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.factory;

import codex.j3map.J3map;
import codex.tanks.weapons.Tank;
import codex.tanks.ai.Algorithm;
import codex.tanks.collision.ContactEventPipeline;
import codex.tanks.components.*;
import codex.tanks.systems.ProjectileState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

/**
 *
 * @author codex
 */
public class EntityFactory {
    
    public static final String
            TANK_DEATH_EXPLOSION = "tank-death-explosion";
    
    private final EntityData ed;
    
    public EntityFactory(EntityData ed) {
        this.ed = ed;
    }
    
    public EntityId createTank(Vector3f position, int team, J3map properties) {
        var tank = ed.createEntity();
        var muzzle = ed.createEntity();
        ed.setComponents(tank,
            new GameObject("tank"),
            new Visual(SpatialFactory.TANK),
            new RigidBody(),
            new EntityTransform().setTranslation(position),
            new TransformMode(-3, -3, 0),
            new MoveVelocity(Vector3f.ZERO),
            new CollisionShape("hitbox"),
            new ContactReaction(
                ContactEventPipeline.KILL_BULLET.getPipelineName(),
                ContactEventPipeline.DIE.getPipelineName()),
            new Team(team),
            new Alive(),
            new AimDirection(Vector3f.UNIT_Z),
            new TurnSpeed(0.1f),
            new Forward(),
            new LinearVelocity(Vector3f.ZERO),
            new ProbeLocation(Vector3f.ZERO),
            new MuzzlePointer(muzzle),
            new CreateOnDeath(EntityFactory.TANK_DEATH_EXPLOSION)
        );
        ed.setComponents(muzzle,
            new EntityTransform(),
            new Copy(tank, Copy.LIFE),
            new Alive()
        );
        Tank.applyProperties(ed, tank, properties);
        return tank;
    }
    public EntityId createAITank(Vector3f position, int team, J3map properties) {
        var tank = createTank(position, team, properties.getJ3map("tank"));
        Algorithm.applyProperties(ed, tank, properties);
        return tank;
    }
    
    public EntityId createProjectile(EntityId owner, Vector3f position, Vector3f direction, float speed, int bounces) {
        if (!ProjectileState.isMissile(speed)) {
            return createBullet(owner, position, direction.mult(speed), bounces);
        }
        else {
            return createMissile(owner, position, direction.mult(speed), bounces)[0];
        }
    }
    public EntityId createBullet(EntityId owner, Vector3f position, Vector3f velocity, int bounces) {
        var bullet = ed.createEntity();
        ed.setComponents(bullet,
            new GameObject("bullet"),
            new Visual(SpatialFactory.BULLET),
            new EntityTransform()
                .setTranslation(position)
                .setScale(.17f),
            new TransformMode(1, 1, 0),
            new Velocity(velocity),
            new FaceVelocity(),
            new Bounces(bounces),
            new CollisionShape("hitbox"),
            new ContactReaction(ContactReaction.DIE),
            new Owner(owner, "bullet"),
            new Alive()
        );
        var smoke = ed.createEntity();
        ed.setComponents(smoke,
            new Visual(SpatialFactory.BULLET_SMOKE),
            new Particles(),
            new EntityTransform(),
            new TransformMode(1, 0, 0),
            new Copy(bullet, Copy.TRANSFORM),
            new OrphanBucket(bullet, new Decay(1f), new EmissionsPerSecond(0)),
            new Alive()
        );
        return bullet;
    }
    public EntityId[] createMissile(EntityId owner, Vector3f position, Vector3f velocity, int bounces) {
        var missile = ed.createEntity();
        ed.setComponents(missile,
            new GameObject("bullet"),
            new Visual(SpatialFactory.MISSILE),
            new EntityTransform()
                .setTranslation(position)
                .setScale(.17f),
            new TransformMode(1, 1, 0),
            new Velocity(velocity),
            new FaceVelocity(),
            new Bounces(bounces),
            new CollisionShape("hitbox"),
            new ContactReaction(ContactReaction.DIE),
            new Owner(owner),
            new Alive()
        );
        var light = ed.createEntity();
        ed.setComponents(light,
            new GameObject("light"),
            new EntityLight(EntityLight.POINT),
            new EntityTransform(),
            new TransformMode(1, 1, 1),
            new Copy(missile, Copy.TRANSFORM),
            new Alive(),
            new Power(100f),
            new ColorScheme(ColorRGBA.Orange),
            new OrphanBucket(missile, new Decay(.3f))
        );
        return new EntityId[]{missile, light};
    }
    public EntityId createMuzzleflash(EntityId parent, float scale) {
        var flash = ed.createEntity();
        ed.setComponents(flash,
            new GameObject("light"),
            new Visual(SpatialFactory.MUZZLEFLASH),
            new EntityLight(EntityLight.POINT),
            new EntityTransform().setScale(scale),
            new TransformMode(1, 1, 0),
            new Copy(parent, Copy.TRANSFORM),
            new CollisionShape(),
            new Power(50f),
            new Decay(.03f),
            new Alive(),
            new ColorScheme(ColorRGBA.Orange)
        );
        return flash;
    }
    
    public EntityId createTankShards(Vector3f position, ColorRGBA color) {
        var shards = ed.createEntity();
        ed.setComponents(shards,
            new Visual(SpatialFactory.TANK_SHARDS),
            new EntityTransform().setTranslation(position),
            new ColorScheme(color),
            new Decay(1f),
            new Alive()
        );
        return shards;
    }
    public EntityId createExplosion1(Vector3f position) {
        var explosion = ed.createEntity();
        // todo: set explosion components
        return explosion;
    }
    
    public void create(String model, EntityId id) {
        switch (model) {
            case TANK_DEATH_EXPLOSION -> createTankDeathExplosion(id);
        }
    }
    
    public void createTankDeathExplosion(EntityId id) {
        var position = ed.getComponent(id, EntityTransform.class).getTranslation();
        var color = ed.getComponent(id, ColorScheme.class).getPallete()[0];
        createTankShards(position, color);
        createExplosion1(position);
    }
    
}
