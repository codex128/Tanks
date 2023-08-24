/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.ai;

/**
 *
 * @author codex
 */
public class BlackModel extends AIModel {
    
    public BlackModel() {
        initialize();
    }
    
    private void initialize() {
        add(new BulletAlert(10f));
        add(new DefensivePoints(10f, .25f));
        add(new DirectShot(.5f));
    }
    
}
