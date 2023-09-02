/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.effects;

import com.jme3.shader.VarType;

/**
 *
 * @author codex
 */
public class MatChange {
    
    private final String ownerName;
    private final String paramName;
    private final VarType type;
    private final Object value;

    public MatChange(String paramName, VarType type, Object value) {
        this(null, paramName, type, value);
    }
    public MatChange(String ownerName, String paramName, VarType type, Object value) {
        this.ownerName = ownerName;
        this.paramName = paramName;
        this.type = type;
        this.value = value;
    }

    public String getOwnerName() {
        return ownerName;
    }
    public String getParamName() {
        return paramName;
    }
    public VarType getType() {
        return type;
    }
    public Object getValue() {
        return value;
    }
    @Override
    public String toString() {
        return "MatParamUpdate{" + "name=" + paramName + ", type=" + type + ", value=" + value + '}';
    }
    
}
