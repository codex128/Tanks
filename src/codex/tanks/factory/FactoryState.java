/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.factory;

import codex.boost.GameAppState;
import codex.tanks.systems.EntityState;
import com.jme3.app.Application;

/**
 *
 * @author codex
 */
public class FactoryState extends GameAppState {
    
    private EntityFactory entityFactory;
    private SpatialFactory spatialFactory;

    @Override
    protected void init(Application app) {
        entityFactory = new EntityFactory(getState(EntityState.class, true).getEntityData());
        spatialFactory = new SpatialFactory(assetManager);
    }
    @Override
    protected void cleanup(Application app) {}
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}

    public EntityFactory getEntityFactory() {
        return entityFactory;
    }
    public SpatialFactory getSpatialFactory() {
        return spatialFactory;
    }
    
}
