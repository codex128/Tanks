/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks;

import codex.boost.Timer;
import codex.boost.TimerListener;
import codex.tanks.systems.CollisionState;
import codex.tanks.util.GameUtils;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;
import com.simsilica.es.Entity;

/**
 *
 * @author codex
 */
public class Missile extends Bullet implements TimerListener {
    
    private Material material;
    private final Timer timer = new Timer(0.03f);
    
    public Missile(Spatial spatial, Entity entity) {
        super(spatial, entity);
        initialize();
    }
    
    private void initialize() {
        material = GameUtils.fetchMaterial(GameUtils.getChild(getSpatial(), "flame"));
        timer.setCycleMode(Timer.CycleMode.INFINITE);
        timer.addListener(this);
        timer.start();
    }
    
    @Override
    public void update(CollisionState collision, float tpf) {
        super.update(collision, tpf);
        timer.update(tpf);
    }
    @Override
    public void onTimerFinish(Timer timer) {
        material.setFloat("Seed", FastMath.nextRandomFloat()*340.324f);  
    }
    
}
