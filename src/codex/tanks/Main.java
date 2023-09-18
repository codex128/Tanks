package codex.tanks;

import codex.tanks.systems.TankState;
import codex.tanks.systems.PhysicsState;
import codex.tanks.systems.ContactState;
import codex.j3map.J3mapFactory;
import codex.j3map.processors.FloatProcessor;
import codex.j3map.processors.IntegerProcessor;
import codex.j3map.processors.StringProcessor;
import codex.tanks.ai.*;
import codex.tanks.factories.FactoryState;
import codex.tanks.dungeon.DungeonMaster;
import codex.tanks.systems.*;
import codex.tanks.util.ColorProcessor;
import codex.tanks.systems.GunState;
import codex.tanks.util.debug.DebugState;
import com.jme3.app.DetailedProfilerState;
import com.jme3.app.LostFocusBehavior;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        var settings = new AppSettings(true);
        settings.setWidth(1024);
        settings.setHeight(768);
        settings.setFrameRate(120);
        settings.setVSync(false);
        Main app = new Main();
        app.setSettings(settings);
        app.setLostFocusBehavior(LostFocusBehavior.Disabled);
        //app.setDisplayFps(false);
        //app.setDisplayStatView(false);
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
            new GatewayState(),
            new DoorState(),
            new LockState(),
            new FaceVelocityState(),
            new FadeState(),
            new DecayState(),
            new DebugState(),
            new LifeState(),
            new OrphanState(),
            new OwnerState(),
            new MaterialState(),
            new AIManager(),
            new DungeonMaster()
        );        
        stateManager.attach(new GameState());
        
        DetailedProfilerState dps = new DetailedProfilerState();
        dps.setEnabled(false);
        getStateManager().attach(dps);
        
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
