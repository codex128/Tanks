/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
import codex.tanks.components.Brain;
import codex.tanks.systems.EntityState;
import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author codex
 */
public interface Algorithm {
    
    public static final HashMap<String, Class<? extends Algorithm>> classes = new HashMap<>();
    
    public default void initialize(Application app) {}
    public default EntityId fetchTargetId(AlgorithmUpdate update) {
        return null;
    }
    
    public void update(AlgorithmUpdate update);
    public boolean move(AlgorithmUpdate update);
    public boolean aim(AlgorithmUpdate update);
    public boolean shoot(AlgorithmUpdate update);
    public boolean mine(AlgorithmUpdate update);
    public void endUpdate(AlgorithmUpdate update);
    
    public default void cleanup(Application app) {}
    
    public default <T extends AppState> T getState(Application app, Class<T> type) {
        return app.getStateManager().getState(type);
    }
    public default EntityData getEntityData(Application app) {
        return getState(app, EntityState.class).getEntityData();
    }
    
    public static void addAlgorithmClass(Class<? extends Algorithm> clazz) {
        classes.put(clazz.getSimpleName(), clazz);
    }
    public static void addAlgorithmClasses(Class<? extends Algorithm>... classes) {
        for (var c : classes) {
            addAlgorithmClass(c);
        }
    }
    public static void applyProperties(EntityData ed, EntityId id, J3map source) {
        ed.setComponent(id, new Brain(source.getInteger("size", 0)));
    }
    public static Algorithm createFromClass(J3map source, Class<? extends Algorithm> clazz) {
        try {
            return clazz.getConstructor(J3map.class).newInstance(source);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException ex) {
            throw new NullPointerException("Could not instantiate new algorithm instance!");
        }
    }
    
}
