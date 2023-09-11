/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.dungeon;

import codex.tanks.es.FunctionFilter;
import com.simsilica.es.ComponentFilter;
import com.simsilica.es.EntityComponent;

/**
 *
 * @author codex
 */
public class RoomIndex implements EntityComponent {
    
    public static final int NONE = -1, UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3;
    public static final RoomIndex
            NULL = new RoomIndex(-1, -1);
    
    public final int x;
    public final int y;
    private boolean matchActive = false;
    
    public RoomIndex(boolean matchActive) {
        x = y = 0;
        this.matchActive = matchActive;
    }
    public RoomIndex(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public RoomIndex(RoomIndex index) {
        x = index.x;
        y = index.y;
        matchActive = index.matchActive;
    }
    public RoomIndex(RoomIndex index, boolean matchActive) {
        x = index.x;
        y = index.y;
        this.matchActive = matchActive;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean isMatchActive() {
        return matchActive;
    }
    @Override
    public String toString() {
        return "RoomIndex{" + "x=" + x + ", y=" + y + '}';
    }
    
    public RoomIndex set(RoomIndex index) {
        return new RoomIndex(index, matchActive);
    }
    public RoomIndex add(int direction) {
        return switch (direction) {
            case UP    -> new RoomIndex(x, y-1);
            case RIGHT -> new RoomIndex(x+1, y);
            case DOWN  -> new RoomIndex(x, y+1);
            case LEFT  -> new RoomIndex(x-1, y);
            default -> throw new IllegalArgumentException("No such direction!");
        };
    }
    public RoomIndex subtract(int direction) {
        return add(getOppositeDirection(direction));
    }
    public boolean isNextTo(RoomIndex index) {
        for (int i = UP; i <= LEFT; i++) {
            if (add(i).equals(index)) return true;
        }
        return false;
    }
    public int getDirectionTo(RoomIndex index) {
        if (!isNextTo(index)) return NONE;
        else if (y > index.y) return UP;
        else if (x < index.x) return RIGHT;
        else if (y < index.y) return DOWN;
        else if (x > index.x) return LEFT;
        return NONE;
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
        return new FunctionFilter<>(RoomIndex.class, c -> c.x == index.x && c.y == index.y);
    }
    
}
