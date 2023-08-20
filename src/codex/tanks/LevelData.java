/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks;

import codex.j3map.J3map;

/**
 *
 * @author gary
 */
public class LevelData {
    
    String map;
    J3map legend;
    
    public LevelData(J3map source) {
        map = source.getString("map");
        legend = source.getJ3map("legend");
    }
    
    public String getMap() {
        return map;
    }
    public J3map getLegend() {
        return legend;
    }
    
}
