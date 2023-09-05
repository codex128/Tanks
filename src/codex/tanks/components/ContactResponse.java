/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import com.simsilica.es.EntityComponent;

/**
 *
 * @author codex
 */
public class ContactResponse implements EntityComponent {
    
    private final String[] pipelines;

    public ContactResponse(String... pipelines) {
        this.pipelines = pipelines;
    }

    public String[] getPipelines() {
        return pipelines;
    }
    @Override
    public String toString() {
        return "ContactReaction{" + "pipeline=" + pipelines.length + '}';
    }
    
}
