/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks;

import com.jme3.input.KeyInput;
import com.simsilica.lemur.input.Button;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;

/**
 *
 * @author gary
 */
public class Functions {
    
    public static final String
            MAIN_GROUP = "main-group";
    
    public static final FunctionId
            F_VERTICAL = new FunctionId(MAIN_GROUP, "vertical"),
            F_HORIZONTAL = new FunctionId(MAIN_GROUP, "horizontal"),
            F_SHOOT = new FunctionId(MAIN_GROUP, "shoot");
    
    public static void initialize(InputMapper im) {
        im.map(F_VERTICAL, InputState.Positive, KeyInput.KEY_W);
        im.map(F_VERTICAL, InputState.Negative, KeyInput.KEY_S);
        im.map(F_HORIZONTAL, InputState.Negative, KeyInput.KEY_D);
        im.map(F_HORIZONTAL, InputState.Positive, KeyInput.KEY_A);
        im.map(F_SHOOT, Button.MOUSE_BUTTON1);
    }
    
}
