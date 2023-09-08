/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.blueprints;

import codex.j3map.J3map;
import codex.tanks.weapons.Tank;
import codex.tanks.ai.Algorithm;
import codex.tanks.components.*;
import codex.tanks.effects.MatChange;
import codex.tanks.systems.ProjectileState;
import codex.tanks.util.ComponentRelation;
import codex.tanks.util.Interpolator;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.shader.VarType;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import java.util.Collection;

/**
 *
 * @author codex
 */
public class EntityFactory {
    
    public static final String
            TANK_DEATH_EXPLOSION = "tank-death-explosion",
            WEAPON_EXPLOSION = "weapon-explosion";
    
    private final EntityData ed;
    private final FactoryState factory;
    
    public EntityFactory(FactoryState factory) {
        this.factory = factory;
        this.ed = this.factory.getEntityData();
    }
    
    public EntityId createTank(Vector3f position, int team, J3map properties) {
        var tank = ed.createEntity();
        var muzzle = ed.createEntity();        
        var colors = new ColorScheme(properties.getProperty(ColorRGBA.class, "color1", ColorRGBA.Blue),
                properties.getProperty(ColorRGBA.class, "color2", ColorRGBA.DarkGray));
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
            new CreateOnDeath(EntityFactory.TANK_DEATH_EXPLOSION),
            new MaxSpeed(properties.getFloat("speed", 6f)),
            new Firerate(properties.getFloat("rps", 1f)),
            new BulletCapacity(properties.getInteger("maxBullets", 5)),
            new Bounces(properties.getInteger("maxBounces", 1)),
            new Power(properties.getFloat("bulletSpeed", 12f)),
            new MineCapacity(properties.getInteger("maxMines", 2)),
            colors,
            new MaterialUpdate(
                new MatChange("MainColor", VarType.Vector4, colors.getPallete()[0]),
                new MatChange("SecondaryColor", VarType.Vector4, colors.getPallete()[1]))
        );
        ed.setComponents(muzzle,
            new EntityTransform(),
            new Copy(tank, Copy.LIFE),
            new Alive(Alive.UNAFFECTED)
        );
        //Tank.applyProperties(ed, tank, properties);
        return tank;
    }
    public EntityId createAITank(Vector3f position, int team, J3map properties) {
        var tank = createTank(position, team, properties.getJ3map("tank"));
        Algorithm.applyProperties(ed, tank, properties);
        return tank;
    }
    
    public EntityId createProjectile(EntityId owner, Vector3f position, Vector3f direction, float speed, int bounces) {
        if (ProjectileState.isMissile(speed)) {
            return createMissile(owner, position, direction.mult(speed), bounces)[0];
        }
        else if (ProjectileState.isLaser(speed)) {
            return createLaser(owner, position, direction, bounces);
        }
        else {
            return createBullet(owner, position, direction.mult(speed), bounces);
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
            new Alive(Alive.UNAFFECTED),
            new Power(.001f, 40f),
            new ColorScheme(ColorRGBA.Orange),
            new OrphanBucket(missile,
                new Decay(.3f),
                new Relative(
                    new ComponentRelation(Decay.class, Power.class, Interpolator.LINEAR)
                )
            )
        );
        return new EntityId[]{missile, light};
    }
    public EntityId createLaser(EntityId owner, Vector3f position, Vector3f direction, int bounces) {
        var laser = ed.createEntity();
        ed.setComponents(laser,
            new EntityTransform().setTranslation(position),
            new Velocity(direction, -1f),
            new Damage(1f),
            new Owner(owner, "bullet"),
            new Alive()
        );
        return laser;
    }
    public EntityId createLaserEffect(Collection<Vector3f> points, ColorRGBA color) {
        var laser = ed.createEntity();
        ed.setComponents(laser,
            new Visual(SpatialFactory.LASER),
            new Points(points),
            new ColorScheme(color),
            new Decay(1f),
            new Alive()
        );
        return laser;
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
            new Power(.001f, 50f),
            new Decay(.03f),
            new Alive(Alive.UNAFFECTED),
            new ColorScheme(ColorRGBA.Orange),
            new Relative(
                new ComponentRelation(Decay.class, Power.class, Interpolator.LINEAR)
            )
        );
        return flash;
    }    
    public EntityId createMine(EntityId owner, Vector3f position) {
        var mine = ed.createEntity();
        ed.setComponents(mine,
            new GameObject("mine"),
            new Visual(SpatialFactory.MINE),
            new Owner(owner, "mine"),
            new EntityTransform()
                .setTranslation(position)
                .setScale(.7f),
            new Explosive(6f, .3f),
            new CollisionShape("hitbox"),
            new ContactResponse(ContactMethods.DIE, ContactMethods.KILL_PROJECTILE),
            new CreateOnDeath(EntityFactory.WEAPON_EXPLOSION),
            new Decay(10f),
            new Alive()
        );
        return mine;
    }
    
    public void createAfterEffect(String model, EntityId id) {
        switch (model) {
            case TANK_DEATH_EXPLOSION -> createTankDeathExplosion(id);
            case WEAPON_EXPLOSION     -> createWeaponExplosion(id);
        }
    }
    public void createTankDeathExplosion(EntityId id) {
        var position = ed.getComponent(id, EntityTransform.class).getTranslation().clone();
        position.addLocal(0f, 1f, 0f);
        var color = ed.getComponent(id, ColorScheme.class).getPallete()[0];
        createTankShards(position, color);
        createTankExplosion(position, new ColorRGBA(1f, .2f, 0f, 1f));
        var light = ed.createEntity();
        ed.setComponents(light,
            new GameObject("light"),
            new EntityLight(EntityLight.POINT),
            new EntityTransform().setTranslation(position),
            new TransformMode(1, 0, 0),
            new Power(1000f),
            new ColorScheme(new ColorRGBA(1f, .25f, 0f, 1f)),
            new Decay(.3f),
            new Alive(Alive.UNAFFECTED),
            new Relative(
                new ComponentRelation(Decay.class, Power.class, Interpolator.LINEAR)
            )
        );
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
            new Alive(Alive.UNAFFECTED)
        );
        return shards;
    }
    public EntityId createTankExplosion(Vector3f position, ColorRGBA color) {
        var explosion = ed.createEntity();
        ed.setComponents(explosion,
            new Visual(SpatialFactory.TANK_FLAME),
            new Particles(),
            new EntityTransform().setTranslation(position),
            new TransformMode(1, 0, 0),
            new SingleEmission(),
            new ColorScheme(color),
            new Decay(5f),
            new Alive(Alive.UNAFFECTED)
        );
        return explosion;
    }    
    public EntityId createWeaponExplosion(EntityId id) {
        var position = ed.getComponent(id, EntityTransform.class).getTranslation();
        var explosive = ed.getComponent(id, Explosive.class);
        var ex = ed.createEntity();
        ed.setComponents(ex,
            new GameObject("explosion"),
            new Visual(SpatialFactory.EXPLOSION_SPHERE),
            new EntityLight(EntityLight.POINT),
            new EntityTransform()
                .setTranslation(position.add(0f, 1f, 0f))
                .setScale(0f)
                .setScaleRange(0f, explosive.getRadius()),
            new TransformMode(1, 1, 1),
            new Rotate(new Quaternion().fromAngleAxis(FastMath.PI*0.01f, Vector3f.UNIT_Y)),
            new Power(0.001f, 1000f),
            new ColorScheme(ColorRGBA.Orange),
            new Relative(
                new ComponentRelation(Decay.class, EntityTransform.class, Interpolator.LINEAR),
                new ComponentRelation(Decay.class, Power.class, Interpolator.LINEAR)
            ),
            new Decay(explosive.getLifetime()),
            new Alive(Alive.UNAFFECTED)
        );
        return ex;
    }
    
}
