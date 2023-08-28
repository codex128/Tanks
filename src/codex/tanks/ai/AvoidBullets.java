/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
import codex.tanks.components.Owner;
import codex.tanks.components.Velocity;
import codex.tanks.util.GameUtils;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Axis;

/**
 *
 * @author gary
 */
public class AvoidBullets implements Algorithm {
    
    private float alert = 2f;
    private float sidestepFactor = .5f;
    
    public AvoidBullets() {}
    public AvoidBullets(J3map source) {
        alert = source.getFloat("alert", alert);
        sidestepFactor = source.getFloat("sidestepFactor", sidestepFactor);
    }

    @Override
    public void update(AlgorithmUpdate update) {}
    @Override
    public boolean move(AlgorithmUpdate update) {
        var dodge = getDodgeDirection(update);
        if (dodge != null) {
            update.getTank().drive(dodge.normalizeLocal());
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
        var vec = new Vector3f();
        var bulletDir = new Vector3f();
        Vector3f dodge = null;
        for (var b : update.getBullets()) {
            vec.set(b.getPosition()).subtractLocal(update.getTank().getPosition()).setY(0f).normalizeLocal();
            bulletDir.set(b.getEntity().get(Velocity.class).getDirection());
            var owner = update.getEntityData().getComponent(b.getEntity().getId(), Owner.class);
            if (b.getBouncesMade() == 0 && owner != null && owner.isOwner(update.getTankId())) {
                continue;
            }
            float dist = GameUtils.distance2D(b.getPosition(), update.getTank().getPosition(), Axis.Y);
            if (dist < alert) {
                if (dodge == null) dodge = new Vector3f();
                vec.negateLocal();
                var sidestep = new Quaternion().lookAt(vec, Vector3f.UNIT_Y).getRotationColumn(0);
                if (sidestep.dot(bulletDir) > 0) {
                    sidestep.negateLocal();
                }
                FastMath.interpolateLinear(sidestepFactor, vec, sidestep, vec);
                vec.multLocal(dist/alert);
                dodge.addLocal(vec);
                //break;
            }
        }
        return dodge;
    }
    
}
