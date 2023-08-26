package codex.tanks;

import codex.j3map.J3mapFactory;
import codex.j3map.processors.FloatProcessor;
import codex.j3map.processors.IntegerProcessor;
import codex.j3map.processors.StringProcessor;
import codex.tanks.systems.*;
import codex.tanks.util.ColorProcessor;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.Vector3f;
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
        J3mapFactory.registerAllProcessors(
                StringProcessor.class,
                IntegerProcessor.class,
                FloatProcessor.class,
                ColorProcessor.class);
        
        flyCam.setMoveSpeed(20f);
        
        BulletAppState bulletapp = new BulletAppState();
        //bulletapp.setDebugEnabled(true);
        
        stateManager.attachAll(
            new EntityState()
            ,bulletapp
            ,new VisualState()
            ,new TransformUpdateState()
            ,new PhysicsState()
            ,new CollisionState()
            ,new MovementState()
            ,new BulletState()
            ,new TankState()
            ,new FaceVelocityState()
            ,new DecayState()
            ,new LifeState()
            ,new AIManager()
        );
        
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
