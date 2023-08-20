/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.tanks.ai;

import codex.tanks.Bullet;
import codex.tanks.Tank;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import java.util.Collection;

/**
 *
 * @author gary
 */
public interface TankAlgorithm {
    
    public void updateTank(AlgorithmUpdate update);
    public void moveTank(AlgorithmUpdate update);
    public void aimTank(AlgorithmUpdate update);
    public void mineTank(AlgorithmUpdate update);
    
    public default Vector3f getDirectionToTarget(AlgorithmUpdate update) {
        return update.getGame().getPlayerState().getTank().getPosition().subtractLocal(update.getTank().getPosition()).normalizeLocal();
    }
    public default Bullet getThreateningBullet(Collection<Bullet> bullets, Tank tank, float range) {
        Vector3f position = tank.getPosition();
        float dist = -1f;
        Bullet closest = null;
        for (Bullet b : bullets) {
            if (b.getBulletInfo().owner == tank && b.getBouncesMade() == 0) continue;
            float d = b.getPosition().distanceSquared(position);
            if (dist < 0 || d < dist) {
                dist = d;
                closest = b;
            }
        }
        if (closest != null && FastMath.sqrt(dist) < range) {
            return closest;
        }
        return null;
    }
    
}
