/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.effects;

import com.epagagames.particles.valuetypes.Gradient;
import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.post.Filter;
import com.jme3.post.filters.PosterizationFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import java.io.IOException;

/**
 *
 * @author codex
 */
public class ColorGradientFilter extends Filter {
    
    private ColorRGBA color = ColorRGBA.White;
    private Gradient gradient;

    private ColorGradientFilter() {
        super("Color Gradient");
    }
    public ColorGradientFilter(Gradient gradient) {
        this();
        this.gradient = gradient;
    }

    @Override
    protected Material getMaterial() {
        return material;
    }

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "Common/MatDefs/Post/Overlay.j3md");
        material.setColor("Color", color);
    }
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(color, "color", ColorRGBA.White);
    }
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        color = (ColorRGBA) ic.readSavable("color", ColorRGBA.White);
    }
    
}
