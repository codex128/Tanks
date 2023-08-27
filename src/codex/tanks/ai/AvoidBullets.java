/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.tanks.components.Owner;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

/**
 *
 * @author gary
 */
public class AvoidBullets implements Algorithm {
    
    private final float alert;
    private final float directional;
    
    public AvoidBullets(float alert, float directional) {
        this.alert = alert;
        this.directional = directional;
    }

    @Override
    public void update(AlgorithmUpdate update) {}
    @Override
    public boolean move(AlgorithmUpdate update) {
        var dodge = getDodgeDirection(update);
        if (dodge != null) {
            update.getTank().move(dodge.normalizeLocal());
            return true;
        }
        return false;
    }
    @Override
    public boolean aim(AlgorithmUpdate update) {
        return false;
    }
    @Override
    public boolean shoot(AlgorithmUpdate update) {
        return false;
    }
    @Override
    public boolean mine(AlgorithmUpdate update) {
        return false;
    }    
    @Override
    public void cleanup(AlgorithmUpdate update) {}
    
    public Vector3f getDodgeDirection(AlgorithmUpdate update) {
        Vector3f dirToBullet = new Vector3f();
        Vector3f dodge = null;
        for (var b : update.getBullets()) {
            dirToBullet.set(b.getPosition().subtract(update.getTank().getPosition()).setY(0f).normalizeLocal());
            var owner = update.getEntityData().getComponent(b.getEntity().getId(), Owner.class);
            if (b.getBouncesMade() == 0 && owner != null && owner.isOwner(update.getTankId())) {
                continue;
            }
            float dist = b.getPosition().distance(update.getTank().getPosition());
            if (dist < alert) {
                if (dodge == null) dodge = new Vector3f();
                dodge.addLocal(dirToBullet.multLocal(dist/alert).negateLocal());
            }
        }
        return dodge;
    }
    
}
