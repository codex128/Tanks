/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks;

import codex.boost.Timer;
import codex.boost.TimerListener;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.scene.Node;

/**
 *
 * @author codex
 */
public class Missile extends Bullet implements TimerListener {
    
    private final Material material;
    private final Timer timer = new Timer(0.03f);
    
    public Missile(Node root, Material material, BulletInfo info) {
        super(root, info);
        this.material = material;
        timer.setCycleMode(Timer.CycleMode.INFINITE);
        timer.addListener(this);
        timer.start();
    }
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        timer.update(tpf);
    }
    @Override
    public void onTimerFinish(Timer timer) {
        material.setFloat("Seed", FastMath.nextRandomFloat()*340.324f);        
    }
    
}
