/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
import codex.tanks.Bullet;
import codex.tanks.Tank;
import codex.tanks.components.Velocity;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import java.util.Collection;

/**
 *
 * @author gary
 */
public class AvoidBullets implements Algorithm {
    
    private float alert;
    
    public AvoidBullets(float alert) {
        this.alert = alert;
    }

    @Override
    public void initialize(AlgorithmUpdate update) {}
    @Override
    public boolean move(AlgorithmUpdate update) {
        Vector3f location = update.getTank().getPosition();
        var threat = getThreateningBullet(update.getBullets(), update.getTank(), alert);
        if (threat != null) {
            Vector3f away = location.subtract(threat.getPosition()).normalizeLocal();
            Quaternion q = new Quaternion().lookAt(away, Vector3f.UNIT_Y);
            Vector3f left = q.getRotationColumn(0);
            int turn = threat.getEntity().get(Velocity.class).getDirection().dot(left) < 0 ? 1 : -1;
            final float diagonalFactor = .25f;
            update.getTank().move(FastMath.interpolateLinear(diagonalFactor, away, left.multLocal(turn)).normalizeLocal());
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
    
    public static Bullet getThreateningBullet(Collection<Bullet> bullets, Tank tank, float distance) {
        float minDist = -1f;
        Bullet bullet = null;
        for (var b : bullets) {
            if (b.getBouncesMade() == 0 && tank.ownsBullet(b)) {
                continue;
            }
            float dist = b.getPosition().distanceSquared(tank.getPosition());
            if (dist < distance*distance && (minDist < 0 || dist < minDist)) {
                minDist = dist;
                bullet = b;
            }
        }
        return bullet;
    }
    
}
