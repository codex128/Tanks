/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import codex.j3map.J3map;
import com.jme3.asset.AssetManager;
import com.simsilica.es.EntityComponent;

/**
 * Contains information on how to load and fetch a J3map.
 * 
 * @author codex
 */
public class PropertySource implements EntityComponent {
    
    private final String[] path;
    
    public PropertySource(String... path) {
        assert path.length > 0;
        this.path = path;
    }

    public String getFilePath() {
        return path[0];
    }
    public String[] getPath() {
        return path;
    }
    @Override
    public String toString() {
        return "PropertySource{" + "path=" + path[0] + (path.length > 1 ? " +"+(path.length-1)+ " more" + (path.length > 2 ? "s" : "") : "") + '}';
    }    
    
    public J3map load(AssetManager assetManager) {
        J3map source = (J3map)assetManager.loadAsset(path[0]);
        for (int i = 1; i < path.length; i++) {
            source = source.getJ3map(path[i]);
            if (source == null) break;
        }
        return source;
    }
    
}
