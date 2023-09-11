/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import codex.tanks.es.FunctionFilter;
import com.simsilica.es.ComponentFilter;
import com.simsilica.es.EntityComponent;

/**
 *
 * @author codex
 */
public class OnSleep implements EntityComponent {
    
    public static final String
            DETACH_SPATIAL = "detach-spatial",
            REMOVE_ENTITY = "remove-entity";
    
    private final String method;

    public OnSleep(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
    @Override
    public String toString() {
        return "OnSleep{" + "method=" + method + '}';
    }
    
    public static ComponentFilter<OnSleep> filter(String method) {
        return new FunctionFilter<>(OnSleep.class, c -> c.getMethod().equals(method));
    }
    
}
