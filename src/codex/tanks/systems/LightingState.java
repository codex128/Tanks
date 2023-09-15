/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.systems;

import codex.tanks.components.Brightness;
import codex.tanks.components.EntityLight;
import codex.tanks.components.EntityTransform;
import codex.tanks.components.InfluenceCone;
import codex.tanks.components.LightColor;
import codex.tanks.components.RoomStatus;
import codex.tanks.es.ESAppState;
import com.jme3.app.Application;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.simsilica.es.Entity;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import java.util.HashMap;

/**
 *
 * @author codex
 */
public class LightingState extends ESAppState {
    
    private EntitySet roomDependent;
    private final EntitySet[] lights = new EntitySet[EntityLight.AMBIENT+1];
    private final HashMap<EntityId, DirectionalLight> directionals = new HashMap<>();
    private final HashMap<EntityId, PointLight> points = new HashMap<>();
    private final HashMap<EntityId, SpotLight> spots = new HashMap<>();
    private final HashMap<EntityId, AmbientLight> ambients = new HashMap<>();
    
    @Override
    protected void init(Application app) {
        super.init(app);
        roomDependent = ed.getEntities(EntityLight.class, RoomStatus.class);
        lights[EntityLight.DIRECTIONAL] = ed.getEntities(EntityLight.filter(EntityLight.DIRECTIONAL), EntityLight.class, EntityTransform.class, LightColor.class);
        lights[EntityLight.POINT] = ed.getEntities(EntityLight.filter(EntityLight.POINT), EntityLight.class, EntityTransform.class, Brightness.class, LightColor.class);
        lights[EntityLight.SPOT] = ed.getEntities(EntityLight.filter(EntityLight.SPOT), EntityLight.class, EntityTransform.class, Brightness.class, InfluenceCone.class, LightColor.class);
        lights[EntityLight.AMBIENT] = ed.getEntities(EntityLight.filter(EntityLight.AMBIENT), EntityLight.class, LightColor.class);
    }
    @Override
    protected void cleanup(Application app) {}
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        for (int i = 0; i < lights.length; i++) {
            var set = lights[i];
            if (set.applyChanges()) {
                for (var e : set.getAddedEntities()) {
                    create(e, i);
                    update(e, i);
                }
                for (var e : set.getChangedEntities()) {
                    update(e, i);
                }
                for (var e : set.getRemovedEntities()) {
                    destroy(e, i);
                }
            }
        }
        if (roomDependent.applyChanges()) {
            roomDependent.getAddedEntities().forEach(e -> editLightInScene(e));
            roomDependent.getChangedEntities().forEach(e -> editLightInScene(e));
        }
    }
    
    private void create(Entity e, int type) {
        Light light = switch (type) {
            case EntityLight.DIRECTIONAL -> createDirectional(e);
            case EntityLight.POINT -> createPoint(e);
            case EntityLight.SPOT -> createSpot(e);
            case EntityLight.AMBIENT -> createAmbient(e);
            default -> null;
        };
        if (light == null) {
            throw new NullPointerException("Light cannot be null!");
        }
        var c = e.get(EntityLight.class);
        if (!c.isAdded() && ed.getComponent(e.getId(), RoomStatus.class) == null) {
            rootNode.addLight(light);
            e.set(c.set(true));
        }
    }
    private DirectionalLight createDirectional(Entity e) {
        var light = new DirectionalLight();
        directionals.put(e.getId(), light);
        return light;
    }
    private PointLight createPoint(Entity e) {
        var light = new PointLight();
        points.put(e.getId(), light);
        return light;
    }
    private SpotLight createSpot(Entity e) {
        var light = new SpotLight();
        spots.put(e.getId(), light);
        return light;
    }
    private AmbientLight createAmbient(Entity e) {
        var light = new AmbientLight();
        ambients.put(e.getId(), light);
        return light;
    }
    
    private void editLightInScene(Entity e) {
        var c = e.get(EntityLight.class);
        if (!c.isAdded() && e.get(RoomStatus.class).getState() == RoomStatus.ACTIVE) {
            rootNode.addLight(getLight(e.getId(), c.getType()));
            e.set(c.set(true));
        }
        else if (c.isAdded() && e.get(RoomStatus.class).getState() == RoomStatus.SLEEPING) {
            rootNode.removeLight(getLight(e.getId(), e.get(EntityLight.class).getType()));
            e.set(c.set(false));
        }
    }
    
    private void update(Entity e, int type) {
        switch (type) {
            case EntityLight.DIRECTIONAL -> updateDirectional(e);
            case EntityLight.POINT -> updatePoint(e);
            case EntityLight.SPOT -> updateSpot(e);
            case EntityLight.AMBIENT -> updateAmbient(e);
        }
    }
    private void updateDirectional(Entity e) {
        var light = getDirectionalLight(e.getId());
        light.setDirection(e.get(EntityTransform.class).getRotation().mult(Vector3f.UNIT_Z));
        light.setColor(e.get(LightColor.class).getColor());
    }
    private void updatePoint(Entity e) {
        var light = getPointLight(e.getId());
        light.setPosition(e.get(EntityTransform.class).getTranslation());
        light.setRadius(e.get(Brightness.class).getValue());
        light.setColor(e.get(LightColor.class).getColor());
    }
    private void updateSpot(Entity e) {
        var light = getSpotLight(e.getId());
        var transform = e.get(EntityTransform.class);
        light.setPosition(transform.getTranslation());
        light.setDirection(transform.getRotation().mult(Vector3f.UNIT_Z));
        light.setColor(e.get(LightColor.class).getColor());     
        light.setSpotRange(e.get(Brightness.class).getValue());
        e.get(InfluenceCone.class).applyToSpotLight(light);
    }
    private void updateAmbient(Entity e) {
        getAmbientLight(e.getId()).setColor(e.get(LightColor.class).getColor());
    }
    
    private void destroy(Entity e, int type) {
        var light = removeLight(e.getId(), type);
        if (light != null) {
            rootNode.removeLight(light);
        }
    }
    private Light removeLight(EntityId id, int type) {
        return switch (type) {
            case EntityLight.DIRECTIONAL -> directionals.remove(id);
            case EntityLight.POINT -> points.remove(id);
            case EntityLight.SPOT -> spots.remove(id);
            case EntityLight.AMBIENT -> ambients.remove(id);
            default -> null;
        };
    }
    
    public Light getLight(EntityId id, int type) {
        return switch (type) {
            case EntityLight.DIRECTIONAL -> directionals.get(id);
            case EntityLight.POINT -> points.get(id);
            case EntityLight.SPOT -> spots.get(id);
            case EntityLight.AMBIENT -> ambients.get(id);
            default -> null;
        };
    }
    public DirectionalLight getDirectionalLight(EntityId id) {
        return directionals.get(id);
    }
    public PointLight getPointLight(EntityId id) {
        return points.get(id);
    }
    public SpotLight getSpotLight(EntityId id) {
        return spots.get(id);
    }
    public AmbientLight getAmbientLight(EntityId id) {
        return ambients.get(id);
    }
    
}
