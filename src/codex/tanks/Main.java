package codex.tanks;

import codex.tanks.systems.TankState;
import codex.tanks.systems.PhysicsState;
import codex.tanks.systems.ContactState;
import codex.j3map.J3mapFactory;
import codex.j3map.processors.FloatProcessor;
import codex.j3map.processors.IntegerProcessor;
import codex.j3map.processors.StringProcessor;
import codex.tanks.ai.*;
import codex.tanks.blueprints.FactoryState;
import codex.tanks.systems.*;
import codex.tanks.util.ColorProcessor;
import codex.tanks.systems.GunState;
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
        //app.setDisplayFps(false);
        app.setDisplayStatView(false);
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
        
        Algorithm.addAlgorithmClasses(
                AimSkew.class,
                AvoidBullets.class,
                BasicShooting.class,
                DefensivePoints.class,
                DirectAim.class,
                Lookout.class,
                PlayerFinder.class,
                RandomPoints.class,
                SniperShooting.class,
                Wander.class);
        
        BulletAppState bulletapp = new BulletAppState();
        //bulletapp.setDebugEnabled(true);
        
        stateManager.attachAll(new EntityState(),
            bulletapp,
            new FactoryState(),
            new VisualState(),
            new TransformUpdateState(),
            new RelationState(),
            new PhysicsState(),
            new LightingState(),
            new EmitterState(),
            new ExplosionState(),
            new ContactState(),
            new GunState(),
            new ProjectileState(),
            new TankState(),
            new FaceVelocityState(),
            new DecayState(),
            new LifeState(),
            new OrphanState(),
            new OwnerState(),
            new MaterialState(),
            new AIManager()
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
