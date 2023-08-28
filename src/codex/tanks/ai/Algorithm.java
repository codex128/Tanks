/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
import codex.tanks.components.Brain;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import java.lang.System.Logger.Level;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import org.bushe.swing.event.Logger;

/**
 *
 * @author codex
 */
public interface Algorithm {
    
    public static final HashMap<String, Class<? extends Algorithm>> classes = new HashMap<>();
    
    public void update(AlgorithmUpdate update);
    public boolean move(AlgorithmUpdate update);
    public boolean aim(AlgorithmUpdate update);
    public boolean shoot(AlgorithmUpdate update);
    public boolean mine(AlgorithmUpdate update);
    public void cleanup(AlgorithmUpdate update);
    
    public static void addAlgorithmClass(Class<? extends Algorithm> clazz) {
        System.out.println("add class: "+clazz.getSimpleName());
        classes.put(clazz.getSimpleName(), clazz);
    }
    public static void addAlgorithmClasses(Class<? extends Algorithm>... classes) {
        for (var c : classes) {
            addAlgorithmClass(c);
        }
    }
    public static void applyProperties(EntityData ed, EntityId id, J3map source) {
        var brains = new ArrayList<Algorithm>();
        for (var p : source.getOrderedPropertyList()) { 
            if (!(p.property instanceof J3map)) {
                continue;
            }
            var clazz = classes.get(p.key);
            if (clazz != null) {
                var a = applyToClass(ed, id, (J3map)p.property, clazz);
                if (a != null) {
                    brains.add(a);
                }
            }
        }
        ed.setComponent(id, new Brain(brains.toArray(Algorithm[]::new)));
    }
    private static Algorithm applyToClass(EntityData ed, EntityId id, J3map source, Class<? extends Algorithm> clazz) {
        try {
            return clazz.getConstructor(J3map.class).newInstance(source);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException ex) {
            throw new NullPointerException("Could not instantiate new algorithm instance!");
        }
    }
    
}
