/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.factories;

import codex.tanks.es.ESAppState;
import com.jme3.app.Application;
import com.jme3.asset.AssetManager;

/**
 *
 * @author codex
 */
public class FactoryState extends ESAppState {
    
    private EntityFactory entityFactory;
    private SpatialFactory spatialFactory;
    private ContactMethods contactMethods;

    @Override
    protected void init(Application app) {
        super.init(app);
        entityFactory = new EntityFactory(this);
        spatialFactory = new SpatialFactory(this, assetManager);
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

    public AssetManager getAssetManager() {
        return assetManager;
    }
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
