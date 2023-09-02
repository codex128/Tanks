/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.factory;

import codex.boost.GameAppState;
import codex.boost.scene.UserDataIterator;
import codex.tanks.systems.EntityState;
import com.jme3.app.Application;
import com.jme3.scene.Spatial;

/**
 *
 * @author codex
 */
public class FactoryState extends GameAppState {
    
    public static final UserDataIterator[] BASIC_PREPROCESSORS = {
        new UserDataIterator<>("CullHint", String.class) {
            @Override
            public void accept(Spatial spatial, String userdata) {
                spatial.setCullHint(Spatial.CullHint.valueOf(userdata));
            }
        }
    };
    
    private EntityFactory entityFactory;
    private SpatialFactory spatialFactory;

    @Override
    protected void init(Application app) {
        entityFactory = new EntityFactory(getState(EntityState.class, true).getEntityData());
        spatialFactory = new SpatialFactory(assetManager);
        provideBasicSpatialPreProcessors(spatialFactory);
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
    
    public static void provideBasicSpatialPreProcessors(SpatialFactory factory) {
        for (var p : BASIC_PREPROCESSORS) {
            factory.addSpatialPreProcessor(p);
        }
    }
}
