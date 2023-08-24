/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.util;

import com.jme3.light.PointLight;
import com.jme3.scene.Spatial;

/**
 *
 * @author codex
 */
public class PointLightNode extends LightNode<PointLight> {

    public PointLightNode(PointLight light, Spatial owner) {
        super(light, owner);
    }

    @Override
    protected void updateLightTransform() {
        light.setPosition(getWorldTranslation());
    }
    
}
