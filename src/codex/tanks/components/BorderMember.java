/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.components;

import codex.tanks.dungeon.RoomIndex;
import com.simsilica.es.EntityComponent;

/**
 *
 * @author codex
 */
public class BorderMember implements EntityComponent {
    
    private final RoomIndex[] i;

    public BorderMember(RoomIndex i1, RoomIndex i2) {
        this.i = new RoomIndex[] {i1, i2};
    }
    
    public RoomIndex[] getRooms() {
        return i;
    }
    public RoomIndex getOther(RoomIndex index) {
        if (index.equals(i[0])) return i[1];
        else return i[0];
    }
    public boolean equals(RoomIndex i1, RoomIndex i2) {
        return equals(new BorderMember(i1, i2));
    }
    public boolean equals(BorderMember m) {
        return (m.i[0].equals(i[0]) && m.i[1].equals(i[1]))
                || (m.i[1].equals(i[0]) && m.i[0].equals(i[1]));
    }
    @Override
    public String toString() {
        return "BorderMember{" + "i=" + i[0]+"+"+i[1] + '}';
    }
    
}
