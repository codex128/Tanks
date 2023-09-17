/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks;

import codex.boost.scene.SceneGraphIterator;
import codex.tanks.components.AimDirection;
import codex.tanks.components.Alive;
import codex.tanks.components.EntityTransform;
import codex.tanks.components.MaxSpeed;
import codex.tanks.components.MoveVelocity;
import codex.tanks.es.ESAppState;
import codex.tanks.util.GameUtils;
import codex.tanks.systems.GunState;
import com.jme3.app.Application;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.simsilica.es.EntityId;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.AnalogFunctionListener;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;

/**
 *
 * @author gary
 */
public class PlayerAppState extends ESAppState implements AnalogFunctionListener, StateFunctionListener {
    
    private final EntityId player;
    private Pointer pointer;
    private final Vector3f inputdirection = new Vector3f();
    
    public PlayerAppState(EntityId player) {
        this.player = player;
    }
    
    @Override
    protected void init(Application app) {
        super.init(app);
        
        cam = app.getCamera();
        pointer = new Pointer(GameUtils.getChild(visuals.getSpatial(player), "pointer"));
        
//        var flashlight = ed.createEntity();
//        ed.setComponents(flashlight,
//            new EntityLight(EntityLight.SPOT),
//            new EntityTransform(),
//            new TransformMode(1, 1, 0),
//            new Copy(ed.getComponent(player, MuzzlePointer.class).getId(), Copy.TRANSFORM),
//            new Offset(new Vector3f(0f, -.5f, 0f)),
//            new Brightness(100f),
//            new InfluenceCone(FastMath.PI*0.1f, FastMath.PI*0.3f),
//            new LightColor(new ColorRGBA(0.2f, 0.2f, 0.2f, 1f))
//        );
        
    }
    @Override
    protected void cleanup(Application app) {
        pointer.exit();
    }
    @Override
    protected void onEnable() {
        InputMapper im = GuiGlobals.getInstance().getInputMapper();
        im.addAnalogListener(this, Functions.F_VERTICAL, Functions.F_HORIZONTAL, Functions.F_SHOOT);
        im.addStateListener(this, Functions.F_MINE, Functions.F_DEBUG);
        im.activateGroup(Functions.MAIN_GROUP);
    }
    @Override
    protected void onDisable() {
        InputMapper im = GuiGlobals.getInstance().getInputMapper();
        im.removeAnalogListener(this, Functions.F_VERTICAL, Functions.F_HORIZONTAL, Functions.F_SHOOT);
        im.removeStateListener(this, Functions.F_MINE, Functions.F_DEBUG);
        im.deactivateGroup(Functions.MAIN_GROUP);
    }
    @Override
    public void update(float tpf) {
        if (ed.getComponent(player, Alive.class) == null) {
            setEnabled(false);
            return;
        }
        //tank.update(tpf);
        if (!inputdirection.equals(Vector3f.ZERO)) {
            ed.setComponent(player, new MoveVelocity(inputdirection.normalizeLocal().multLocal(ed.getComponent(player, MaxSpeed.class).getSpeed())));
        }
        final float n = 45;
        var p1 = ed.getComponent(player, EntityTransform.class).getTranslation();
        var p2 = pointer.getGlobalPointer();
        cam.setLocation(p1.add(0f, n, -n));
        cam.lookAt(FastMath.interpolateLinear(.1f, p1, p2), Vector3f.UNIT_Y);
        //cam.setLocation(new Vector3f(0f, n, -n));
        //cam.lookAt(new Vector3f(), Vector3f.UNIT_Y);
        ed.setComponent(player, new AimDirection(pointer.getRelativePointer().normalizeLocal()));
        inputdirection.set(0f, 0f, 0f);
    }
    @Override
    public void valueActive(FunctionId func, double value, double tpf) {
        if (func == Functions.F_VERTICAL) {
            inputdirection.z = (float)Math.signum(value);
        }
        else if (func == Functions.F_HORIZONTAL) {
            inputdirection.x = (float)Math.signum(value);
        }
        else if (func == Functions.F_SHOOT) {
            getState(GunState.class).shoot(player);
        }
    }
    @Override
    public void valueChanged(FunctionId func, InputState value, double tpf) {
        if (func == Functions.F_MINE && value != InputState.Off) {
            // quick'n'dirty
            factory.getEntityFactory().createMine(player, ed.getComponent(player, EntityTransform.class).getTranslation());
        }
        else if (func == Functions.F_DEBUG && value != InputState.Off) {
            long i = 0;
            for (var vp : renderManager.getPreViews()) {
                for (var scene : vp.getScenes()) {
                    for (var spatial : new SceneGraphIterator(scene)) {
                        System.out.println((++i)+": "+spatial.getName());
                    }
                }
            }
            for (var vp : renderManager.getMainViews()) {
                for (var scene : vp.getScenes()) {
                    for (var spatial : new SceneGraphIterator(scene)) {
                        System.out.println((++i)+": "+spatial.getName());
                    }
                }
            }
            for (var vp : renderManager.getPostViews()) {
                for (var scene : vp.getScenes()) {
                    for (var spatial : new SceneGraphIterator(scene)) {
                        System.out.println((++i)+": "+spatial.getName());
                    }
                }
            }
            throw new NullPointerException();
        }
    }
    
}
