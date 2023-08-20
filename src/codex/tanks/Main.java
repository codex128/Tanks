package codex.tanks;

import codex.j3map.J3mapFactory;
import codex.j3map.processors.FloatProcessor;
import codex.j3map.processors.IntegerProcessor;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.renderer.RenderManager;
import com.simsilica.lemur.GuiGlobals;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        GuiGlobals.initialize(this);
        Functions.initialize(GuiGlobals.getInstance().getInputMapper());
        //GuiGlobals.getInstance().setCursorEventsEnabled(false);
        
        assetManager.registerLoader(J3mapFactory.class, "j3map");
        J3mapFactory.registerAllProcessors(IntegerProcessor.class, FloatProcessor.class);
        
        flyCam.setMoveSpeed(20f);
        
        BulletAppState bulletapp = new BulletAppState();
        //bulletapp.setDebugEnabled(true);
        stateManager.attach(bulletapp);
        
        stateManager.attach(new GameState());
        
    }
    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
}
