/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.blueprints;

import codex.boost.scene.SceneGraphIterator;
import codex.j3map.J3map;
import codex.tanks.ai.Algorithm;
import codex.tanks.components.*;
import codex.tanks.dungeon.RoomIndex;
import codex.tanks.effects.MatChange;
import codex.tanks.systems.ProjectileState;
import codex.tanks.systems.VisualState;
import codex.tanks.es.ComponentRelation;
import codex.tanks.util.Interpolator;
import codex.tanks.es.SavedEntity;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.shader.VarType;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author codex
 */
public class EntityFactory {
    
    public static final String            
            ENTITY_USERDATA = "Entity",
            PROPERTIES = "GameProperties",
            TEAM = "Team";
    public static final String
            TANK = "tank",
            AITANK = "ai-tank",
            WALL = "wall",
            TANK_DEATH_EXPLOSION = "tank-death-explosion",
            WEAPON_EXPLOSION = "weapon-explosion";
    
    private final EntityData ed;
    private final AssetManager assetManager;
    private final FactoryState factory;
    private final HashMap<String, EntityBlueprint> blueprints = new HashMap<>();
    
    public EntityFactory(FactoryState factory) {
        this.factory = factory;
        this.ed = this.factory.getEntityData();
        this.assetManager = this.factory.getAssetManager();
        initializeBlueprints();
    }
    
    private void initializeBlueprints() {
        createTank();
        createBullet();
        createBulletSmoke();
        createMissile();
        createMissileLight();
        createMuzzleflash();
        createMine();
        EntityFactory.this.createWall();
    }
    private void createTank() {
        blueprints.put(TANK, new EntityBlueprint(ed,
            new GameObject("tank"),
            new Visual(SpatialFactory.TANK),
            new RigidBody(),
            new TransformMode(-3, -3, 0),
            new MoveVelocity(Vector3f.ZERO),
            new CollisionShape("hitbox"),
            new ContactResponse(ContactMethods.DIE, ContactMethods.KILL_PROJECTILE),
            new Alive(),
            new AimDirection(Vector3f.UNIT_Z),
            new TurnSpeed(0.1f),
            new Forward(),
            new LinearVelocity(Vector3f.ZERO),
            new ProbeLocation(Vector3f.ZERO),
            new CreateOnDeath(EntityFactory.TANK_DEATH_EXPLOSION)
        ));
    }
    private void createBullet() {
        blueprints.put("bullet", new EntityBlueprint(ed,
            new GameObject("bullet"),
            new Visual(SpatialFactory.BULLET),
            new TransformMode(1, 1, 0),
            new FaceVelocity(),
            new Damage(1f),
            new CollisionShape("hitbox"),
            new ContactResponse(ContactMethods.DIE, ContactMethods.KILL_PROJECTILE),
            new Alive()
        ));
    }
    private void createBulletSmoke() {
        blueprints.put("bullet-smoke", new EntityBlueprint(ed,
            new Visual(SpatialFactory.BULLET_SMOKE),
            new Particles(),
            new EntityTransform(),
            new TransformMode(1, 1, 0),
            new Alive(Alive.UNAFFECTED)
        ));
    }
    private void createMissile() {
        blueprints.put("missile", new EntityBlueprint(ed,
            new GameObject("bullet"),
            new Visual(SpatialFactory.MISSILE),
            new TransformMode(1, 1, 0),
            new FaceVelocity(),
            new Damage(1f),
            new CollisionShape("hitbox"),
            new ContactResponse(ContactMethods.DIE, ContactMethods.KILL_PROJECTILE),
            new Alive()
        ));
    }
    private void createMissileLight() {
        blueprints.put("missile-light", new EntityBlueprint(ed,
            new GameObject("light"),
            new EntityLight(EntityLight.POINT),
            new EntityTransform(),
            new TransformMode(1, 1, 1),
            new Alive(Alive.UNAFFECTED),
            new Power(.001f, 40f),
            new ColorScheme(ColorRGBA.Orange)
        ));
    }
    private void createMuzzleflash() {
        blueprints.put("muzzleflash", new EntityBlueprint(ed,
            new GameObject("light"),
            new Visual(SpatialFactory.MUZZLEFLASH),
            new EntityLight(EntityLight.POINT),
            new TransformMode(1, 1, 0),
            new Power(.001f, 50f),
            new Decay(.03f),
            new Alive(Alive.UNAFFECTED),
            new ColorScheme(ColorRGBA.Orange),
            new Relative(
                new ComponentRelation(Decay.class, Power.class, Interpolator.LINEAR)
            )
        ));
    }
    private void createMine() {
        blueprints.put("mine", new EntityBlueprint(ed,
            new GameObject("mine"),
            new Visual(SpatialFactory.MINE),
            new Explosive(6f, .3f),
            new CollisionShape("hitbox"),
            new ContactResponse(ContactMethods.DIE, ContactMethods.KILL_PROJECTILE),
            new CreateOnDeath(EntityFactory.WEAPON_EXPLOSION),
            new Decay(10f),
            new Alive()
        ));
    }
    private void createWall() {
        blueprints.put("wall", new EntityBlueprint(ed, 
            new GameObject("wall"),
            new RigidBody(0f),
            new TransformMode(-3, -3, 0),
            new CollisionShape(),
            new ContactResponse(ContactMethods.RICOCHET)
        ));
    }
    
    /**
     * Creates entities based on objects in a scene.
     * @param scene
     * @return 
     */
    public LinkedList<EntityId> createFromScene(Spatial scene) {
        var entities = new LinkedList<EntityId>();
        var iterator = new SceneGraphIterator(scene);
        for (var spatial : iterator) {
            String data = spatial.getUserData(ENTITY_USERDATA);
            if (data != null) {
                var entity = examineSpatial(spatial, data);
                if (entity != null) {
                    entities.add(entity);
                    iterator.ignoreChildren();
                }
            }
        }
        return entities;
    }
    public EntityId examineSpatial(Spatial spatial, String data) {
        return switch (data) {
            case TANK -> createTank(spatial);
            case AITANK -> createAITank(spatial);
            case WALL -> EntityFactory.this.createWall(spatial);
            default -> null;
        };
    }
    public EntityId createTank(Spatial spatial) {
        String data = spatial.getUserData(PROPERTIES);
        if (data == null) {
            return null;
        }
        Integer team = spatial.getUserData(TEAM);
        return createTank(spatial.getWorldTranslation(), (team != null ? team : 1), new PropertySource(data.split(">")));
    }
    public EntityId createAITank(Spatial spatial) {
        String data = spatial.getUserData(PROPERTIES);
        if (data == null) {
            return null;
        }
        Integer team = spatial.getUserData(TEAM);
        return createAITank(spatial.getWorldTranslation(), (team != null ? team : 1), new PropertySource(data.split(">")));
    }
    public EntityId createWall(Spatial spatial) {
        var wall = createWall(spatial.getWorldTransform(), spatial);
        factory.getState(VisualState.class).link(wall, spatial);
        return wall;
    }
    
    /**
     * Creates an entity based on saved data.
     * @param save
     * @return 
     */
    public EntityId createFromSave(SavedEntity save) {
        return switch (save.getSaveComponent().getRestore()) {
            case TANK -> createTank(save);
            case AITANK -> createAITank(save);
            default -> null;
        };
    }
    public EntityId createTank(SavedEntity save) {
        return createTank(save.get(EntityTransform.class).getTranslation(), save.get(Team.class).getTeam(), save.get(PropertySource.class));
    }
    public EntityId createAITank(SavedEntity save) {
        return createAITank(save.get(EntityTransform.class).getTranslation(), save.get(Team.class).getTeam(), save.get(PropertySource.class));
    }
    
    public EntityId createTank(Vector3f position, int team, PropertySource source) {
        return createTank(position, team, source, source.load(assetManager).getJ3map("tank"));
    }
    public EntityId createTank(Vector3f position, int team, PropertySource source, J3map properties) {
        //var properties = source.load(assetManager).getJ3map("tank");
        var muzzle = ed.createEntity();
        var colors = new ColorScheme(
                properties.getProperty(ColorRGBA.class, "color1", ColorRGBA.Blue),
                properties.getProperty(ColorRGBA.class, "color2", ColorRGBA.DarkGray));
        var tank = blueprints.get(TANK).create(
            source,
            new EntityTransform().setTranslation(position),
            new Team(team),
            new MuzzlePointer(muzzle),
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
        return tank;
    }
    public EntityId createAITank(Vector3f position, int team, PropertySource source) {
        var properties = source.load(assetManager);
        var tank = createTank(position, team, source, properties.getJ3map("tank"));
        Algorithm.applyProperties(ed, tank, properties);
        return tank;
    }    
    public EntityId createProjectile(EntityId owner, Vector3f position, Vector3f direction, float speed, int bounces) {
        if (ProjectileState.isMissile(speed)) {
            return createMissile(owner, position, direction.mult(speed), bounces)[0];
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
    public EntityId createWall(Transform transform, Spatial spatial) {
        var wall = ed.createEntity();
        ed.setComponents(wall,
            new GameObject("wall"),
            new Visual(),
            new RigidBody(0f),
            new EntityTransform(transform),
            new TransformMode(1, 1, 1),
            new CollisionShape(),
            new ContactResponse(ContactMethods.RICOCHET)
        );
        factory.getVisualState().link(wall, spatial);
        return wall;
    }
    public EntityId createWall(String visual, Vector3f position, float angle) {
        var wall = ed.createEntity();
        ed.setComponents(wall,
            new GameObject("wall"),
            new Visual(visual),
            new RigidBody(0f),
            new EntityTransform()
                .setTranslation(position)
                .setRotation(angle, Vector3f.UNIT_Y),
            new TransformMode(1, 1, 0),
            new CollisionShape(),
            new ContactResponse(ContactMethods.RICOCHET)
        );
        return wall;
    }
    public EntityId createGateway(Vector3f position, float angle, int lockValue, EntityId... doors) {
        var gateway = ed.createEntity();
        ed.setComponents(gateway,
            new GameObject("gateway"),
            new Visual(SpatialFactory.GATEWAY),
            new Gateway(true, doors),
            new EntityTransform()
                .setTranslation(position)
                .setRotation(angle, Vector3f.UNIT_Y),
            new TransformMode(1, 1, 0),
            new Lock(true, lockValue),
            new CollisionShape(),
            new ContactResponse(ContactMethods.KILL_PROJECTILE)
        );
        return gateway;
    }
    public EntityId createSlidingDoor(String visual, Vector3f position, float angle, float openAmount, float speed) {
        var door = ed.createEntity();
        var rotation = new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Y);
        ed.setComponents(door,
            new GameObject("door"),
            new Visual(visual),
            new RigidBody(0f),
            new EntityTransform()
                .setTranslation(position)
                .setRotation(rotation),
            new TransformMode(-3, -3, 0),
            new Door(rotation.mult(Vector3f.UNIT_Z).multLocal(openAmount*FastMath.sign(speed))),
            new MaxSpeed(FastMath.abs(speed)),
            new CollisionShape(),
            new ContactResponse(ContactMethods.RICOCHET)
        );
        return door;
    }
    
    public void createAfterEffect(String model, EntityId id) {
        var room = ed.getComponent(id, RoomIndex.class);
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
    private EntityId createTankShards(Vector3f position, ColorRGBA color) {
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
    private EntityId createTankExplosion(Vector3f position, ColorRGBA color) {
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
    private EntityId createWeaponExplosion(EntityId id) {
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
