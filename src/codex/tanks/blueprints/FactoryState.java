/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.blueprints;

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
    private ContactMethods contactMethods;

    @Override
    protected void init(Application app) {
        var ed = getState(EntityState.class, true).getEntityData();
        entityFactory = new EntityFactory(ed);
        spatialFactory = new SpatialFactory(ed, assetManager);
        contactMethods = new ContactMethods(ed);
    }
    @Override
    protected void cleanup(Application app) {
        spatialFactory.clearAllPreProcessors();
    }
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
    public ContactMethods getContactMethods() {
        return contactMethods;
    }
    
}
