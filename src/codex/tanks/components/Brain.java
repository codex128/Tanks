/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.simsilica.es.EntityComponent;
import static groovyjarjarantlr4.v4.runtime.misc.MurmurHash.update;
import java.util.Arrays;

/**
 *
 * @author codex
 */
public class Brain implements EntityComponent {
    
    private final String model;

    public Brain(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }
    @Override
    public String toString() {
        return "Brain{" + "model=" + model + '}';
    }
    
}
