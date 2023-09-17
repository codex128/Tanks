/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import codex.tanks.es.AbstractFloatComponent;

/**
 *
 * @author codex
 */
public class Decay extends AbstractFloatComponent {

    public Decay(float value) {
        setValue(value);
        setRange(0f, value);
    }
    public Decay(float value, float max) {
        setValue(value);
        setRange(0f, max);
    }
    
    public boolean isExhausted() {
        return value <= 0;
    }
    public Decay increment(float tpf) {
        return new Decay(Math.max(value-tpf, range.x), range.y);
    }
    @Override
    public Decay setPercent(float percent) {
        return new Decay(interpolate(percent), range.y);
    }
    
}
