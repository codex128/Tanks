/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.blueprints;

import codex.j3map.J3map;
import codex.tanks.weapons.Tank;
import codex.tanks.ai.Algorithm;
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
            new ContactResponse(ContactMethods.DIE, ContactMethods.KILL_PROJECTILE),
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
                .setScale(.2f),
            new TransformMode(1, 1, 0),
            new Velocity(velocity),
            new FaceVelocity(),
            new Bounces(bounces),
            new Damage(1f),
            new CollisionShape("hitbox"),
            new ContactResponse(ContactMethods.DIE, ContactMethods.KILL_PROJECTILE),
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
            new Damage(1f),
            new CollisionShape("hitbox"),
            new ContactResponse(ContactMethods.DIE, ContactMethods.KILL_PROJECTILE),
            new Owner(owner, "bullet"),
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
            new Power(40f),
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
            new Particles(),
            new EntityTransform().setTranslation(position),
            new TransformMode(1, 0, 0),
            new SingleEmission(),
            new ColorScheme(color),
            new Decay(5f),
            new Alive()
        );
        return shards;
    }
    public EntityId createExplosion1(Vector3f position, ColorRGBA color) {
        var explosion = ed.createEntity();
        ed.setComponents(explosion,
            new Visual(SpatialFactory.TANK_FLAME),
            new Particles(),
            new EntityTransform().setTranslation(position),
            new TransformMode(1, 0, 0),
            new SingleEmission(),
            new ColorScheme(color),
            new Decay(5f),
            new Alive()
        );
        return explosion;
    }
    
    public void create(String model, EntityId id) {
        switch (model) {
            case TANK_DEATH_EXPLOSION -> createTankDeathExplosion(id);
        }
    }    
    public void createTankDeathExplosion(EntityId id) {
        var position = ed.getComponent(id, EntityTransform.class).getTranslation().clone();
        position.addLocal(0f, 1f, 0f);
        var color = ed.getComponent(id, ColorScheme.class).getPallete()[0];
        createTankShards(position, color);
        createExplosion1(position, new ColorRGBA(1f, .2f, 0f, 1f));
        var light = ed.createEntity();
        ed.setComponents(light,
            new GameObject("light"),
            new EntityLight(EntityLight.POINT),
            new EntityTransform().setTranslation(position),
            new TransformMode(1, 0, 0),
            new Power(1000f),
            new ColorScheme(new ColorRGBA(1f, .25f, 0f, 1f)),
            new Decay(.3f),
            new Alive()
        );
    }
    
}
