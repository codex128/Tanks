/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import codex.tanks.es.FunctionFilter;
import com.simsilica.es.ComponentFilter;
import com.simsilica.es.EntityComponent;
import com.simsilica.mathd.Vec3i;
import java.util.Arrays;

/**
 *
 * @author codex
 */
public class RoomIndex implements EntityComponent {
    
    public static final int NONE = -1, UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
    
    private Vec3i[] indices = new Vec3i[0];
    
    public RoomIndex(int x, int y) {
        this(new Vec3i(x, y, 0));
    }
    public RoomIndex(Vec3i... indices) {
        this.indices = indices;
    }

    public Vec3i[] getIndices() {
        return indices;
    }
    public Vec3i getPrimaryIndex() {
        if (indices.length == 0) return null;
        else return indices[0];
    }
    public Vec3i getIndexAt(int i) {
        return indices[i];
    }
    public Vec3i getDifferentIndex(Vec3i index) {
        for (var i : indices) {
            if (!i.equals(index)) return i;
        }
        return index;
    }
    
    public boolean contains(Vec3i index) {
        for (var i : indices) {
            if (index.equals(i)) return true;
        }
        return false;
    }
    
    public RoomIndex add(int direction) {
        if (indices.length == 0) {
            return null;
        }
        var i = indices[0];
        return switch (direction) {
            case UP    -> new RoomIndex(i.x, i.y-1);
            case RIGHT -> new RoomIndex(i.x+1, i.y);
            case DOWN  -> new RoomIndex(i.x, i.y+1);
            case LEFT  -> new RoomIndex(i.x-1, i.y);
            default -> throw new IllegalArgumentException("No such direction!");
        };
    }
    public RoomIndex subtract(int direction) {
        return add(getOppositeDirection(direction));
    }
    
    public static int getOppositeDirection(int direction) {
        return switch (direction) {
            case UP    -> DOWN;
            case RIGHT -> LEFT;
            case DOWN  -> UP;
            case LEFT  -> RIGHT;
            default -> throw new IllegalArgumentException("No such direction!");
        };
    }
    public static ComponentFilter<RoomIndex> filter(RoomIndex index) {
        return new FunctionFilter<>(RoomIndex.class, c -> Arrays.equals(c.indices, index.indices));
    }
    
}
