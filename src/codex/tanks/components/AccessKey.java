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
public class AccessKey implements EntityComponent {
    
    public static final int BASIC = -1;
    
    /**
     * Values greater than 0 can unlock locks with a <= value, but not less than 0.
     * Values less than 0 only unlock their number, nothing else.
     */
    private final int key;
    
    public AccessKey() {
        this(BASIC);
    }
    public AccessKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
    @Override
    public String toString() {
        return "AccessKey{" + "key=" + key + '}';
    }
    
}
