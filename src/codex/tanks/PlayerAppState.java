/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
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
public class PlayerAppState extends BaseAppState implements
        AnalogFunctionListener, StateFunctionListener {
    
    private GameState game;
    private Camera cam;
    private Tank tank;
    private PointerManager pointer;
    private Vector3f inputdirection = new Vector3f();
    
    public PlayerAppState(GameState game, Tank tank) {
        this.game = game;
        this.tank = tank;
    }
    
    @Override
    protected void initialize(Application app) {
        
        cam = app.getCamera();
        cam.setParallelProjection(true);
        cam.setFrustum(20f, 100f, -30f, 30f, 20f, -20f);
        //cam.setFrustumLeft(100f);
        //cam.setFrustumRight(100f);
        pointer = new PointerManager(tank.getPointerMesh());
        
        InputMapper im = GuiGlobals.getInstance().getInputMapper();
        im.addAnalogListener(this, Functions.F_VERTICAL, Functions.F_HORIZONTAL);
        im.addStateListener(this, Functions.F_SHOOT);
        im.activateGroup(Functions.MAIN_GROUP);
        
    }
    @Override
    protected void cleanup(Application app) {
        pointer.exit();
        InputMapper im = GuiGlobals.getInstance().getInputMapper();
        im.removeAnalogListener(this, Functions.F_VERTICAL, Functions.F_HORIZONTAL);
        im.removeStateListener(this, Functions.F_SHOOT);
        im.deactivateGroup(Functions.MAIN_GROUP);
    }
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        if (!tank.isAlive()) return;
        tank.update(tpf);
        if (!inputdirection.equals(Vector3f.ZERO)) {
            tank.move(inputdirection.normalize(), tpf);
        }
        else {
            tank.stop();
        }
        final float n = 35;
        cam.setLocation(new Vector3f(0f, n, -n));
        cam.lookAt(new Vector3f(), Vector3f.UNIT_Y);
        tank.aimAt(pointer.getPointerLocation());
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
    }
    @Override
    public void valueChanged(FunctionId func, InputState value, double tpf) {
        if (!tank.isAlive()) return;
        if (func == Functions.F_SHOOT && value != InputState.Off) {
            game.addBullet(tank.shoot());
        }
    }
    
    public Tank getTank() {
        return tank;
    }
    
}
