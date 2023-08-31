/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.factory;

import codex.j3map.J3map;
import codex.tanks.Tank;
import codex.tanks.ai.Algorithm;
import codex.tanks.components.*;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

/**
 *
 * @author codex
 */
public class EntityFactory {
    
    private final EntityData ed;
    
    public EntityFactory(EntityData ed) {
        this.ed = ed;
    }
    
    public EntityId createTank(Vector3f position, int team, J3map properties) {
        var tank = ed.createEntity();
        ed.setComponents(tank,
            new GameObject("tank"),
            new Visual(SpatialFactory.TANK),
            new Physics(),
            new EntityTransform().setTranslation(position),
            new TransformMode(-1, 0, 0),
            new CollisionShape("hitbox"),
            new ContactReaction(ContactReaction.DIE),
            new Team(team),
            new Alive()
        );
        Tank.applyProperties(ed, tank, properties);
        return tank;
    }
    public EntityId createAITank(Vector3f position, int team, J3map properties) {
        var tank = createTank(position, team, properties);
        Algorithm.applyProperties(ed, tank, properties);
        return tank;
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
            new Owner(owner),
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
    public EntityId createMuzzleflash(float scale) {
        var flash = ed.createEntity();
        ed.setComponents(flash,
            new GameObject("light"),
            new Visual(SpatialFactory.MUZZLEFLASH),
            new EntityLight(EntityLight.POINT),
            new EntityTransform().setScale(scale),
            new TransformMode(0, 0, 1),
            new Power(50f),
            new Decay(.03f),
            new Alive(),
            new ColorScheme(ColorRGBA.Orange)
        );
        return flash;
    }
    
}
