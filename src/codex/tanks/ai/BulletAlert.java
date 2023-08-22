/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

import codex.j3map.J3map;
import codex.tanks.Bullet;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author gary
 */
public class BulletAlert implements TankAlgorithm {
    
    private J3map source;
    private float alert;
    
    public BulletAlert(J3map source) {
        this.source = source;
        fetchComponents();
    }
    
    private void fetchComponents() {
        alert = source.getFloat("alert", 10f);
    }

    @Override
    public void updateTank(AlgorithmUpdate update) {}
    @Override
    public void moveTank(AlgorithmUpdate update) {
        if (update.isConsumed()) return;
        Vector3f location = update.getTank().getPosition();
        Bullet threat = getThreateningBullet(update.getGame().getBullets(), update.getTank(), alert);
        if (threat != null) {
            Vector3f away = location.subtract(threat.getPosition()).normalizeLocal();
            Quaternion q = new Quaternion().lookAt(away, Vector3f.UNIT_Y);
            Vector3f left = q.getRotationColumn(0);
            int turn = threat.getBulletInfo().getDirection().dot(left) < 0 ? 1 : -1;
            final float diagonalFactor = .25f;
            update.getTank().move(FastMath.interpolateLinear(diagonalFactor, away, left.multLocal(turn)).normalizeLocal());
            //tank.aimAt(threat.getPosition());
            update.consume();
        }
    }
    @Override
    public void aimTank(AlgorithmUpdate update) {}
    @Override
    public void mineTank(AlgorithmUpdate update) {}
    
}
