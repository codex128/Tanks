/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.util.debug;

import codex.tanks.collision.ShapeFilter;
import codex.tanks.components.CollisionShape;
import codex.tanks.es.EntityAccess;

/**
 *
 * @author codex
 */
public abstract class ShapeFilterDebugger implements ShapeFilter {
    
    private ShapeFilter filter;
    
    public ShapeFilterDebugger() {
        this(null);
    }
    public ShapeFilterDebugger(ShapeFilter filter) {
        this.filter = filter;
    }
    
    @Override
    public boolean filter(EntityAccess access, CollisionShape shape) {
        boolean decision = filter == null || filter.filter(access, shape);
        listenToFilter(access, shape, decision);
        return decision;
    }
    protected abstract void listenToFilter(EntityAccess access, CollisionShape shape, boolean decision);
    
}
