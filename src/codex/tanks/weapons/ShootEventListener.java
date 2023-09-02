/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package codex.tanks.weapons;

import com.simsilica.es.EntityId;

/**
 *
 * @author codex
 */
public interface ShootEventListener {
    
    public boolean approveShootEvent(EntityId id);
    public void onShootEventPassed(EntityId id);
    
}
