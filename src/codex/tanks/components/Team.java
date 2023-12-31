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
public class Team implements EntityComponent {
    
    private final int team;
    
    public Team(int team) {
        this.team = team;
    }

    public int getTeam() {
        return team;
    }
    @Override
    public String toString() {
        return "Team{" + "team=" + team + '}';
    }
    
}
