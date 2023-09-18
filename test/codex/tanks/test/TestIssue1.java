/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.test;

import codex.tanks.factories.FactoryState;
import codex.tanks.factories.SpatialFactory;
import codex.tanks.components.Visual;
import codex.tanks.es.ESAppState;
import codex.tanks.systems.EntityState;
import codex.tanks.systems.VisualState;
import codex.tanks.util.debug.DebugState;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;

/**
 *
 * @author codex
 */
public class TestIssue1 extends SimpleApplication {

    public static void main(String[] args) {
        var app = new TestIssue1();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        
        assetManager.registerLocator("assets", FileLocator.class);
        
        stateManager.attachAll(
            new EntityState(),
            new VisualState(),
            new FactoryState(),
            new DebugState(),
            new State()
        );
        
    }
    
    private class State extends ESAppState {

        @Override
        protected void init(Application app) {
            super.init(app);
            
            for (int i = 0; i < 24; i++) {
                //var tank = ed.createEntity();
                //ed.setComponents(tank, new Visual(SpatialFactory.TANK));
            }
            
            rootNode.attachChild(factory.getSpatialFactory().createBullet());
            
        }
        @Override
        protected void cleanup(Application app) {}
        @Override
        protected void onEnable() {}
        @Override
        protected void onDisable() {}
        
    }
    
}
