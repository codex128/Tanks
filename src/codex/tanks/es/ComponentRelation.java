/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.es;

import codex.tanks.es.FloatComponent;
import codex.tanks.util.Interpolator;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;

/**
 *
 * @author codex
 */
public class ComponentRelation {
    
    private final Class<? extends FloatComponent> input;
    private final Class<? extends FloatComponent> output;
    private final Interpolator interpolator;
    
    public ComponentRelation(Class<? extends FloatComponent> input, Class<? extends FloatComponent> output, Interpolator interpolator) {
        this.input = input;
        this.output = output;
        this.interpolator = interpolator;
    }
    
    public void update(EntityData ed, EntityId id) {
        var in = ed.getComponent(id, input);
        if (in == null) return;
        var out = ed.getComponent(id, output);
        if (out == null) return;
        ed.setComponent(id, out.setPercent(interpolator.interpolate(in.getPercent())));
    }

    public Class<? extends FloatComponent> getInput() {
        return input;
    }
    public Class<? extends FloatComponent> getOutput() {
        return output;
    }
    public Interpolator getInterpolator() {
        return interpolator;
    }
    
}
