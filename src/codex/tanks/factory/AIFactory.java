/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.factory;

import codex.tanks.ai.AIModel;
import codex.tanks.ai.Sniper;
import com.jme3.math.FastMath;

/**
 *
 * @author codex
 */
public class AIFactory {
    
    public static final String
            BROWN = "brown",
            GREY = "grey",
            GREEN = "green",
            YELLOW = "yellow",
            RED = "red",
            LIGHTGREEN = "light-green",
            PURPLE = "purple",
            WHITE = "white",
            BLACK = "black";
    
    public AIFactory() {}
    
    public AIModel create(String name) {
        return switch (name) {
            case BROWN -> createBrown();
            case GREY -> createGrey();
            case GREEN -> createGreen();
            case YELLOW -> createYellow();
            case RED -> createRed();
            case LIGHTGREEN -> createLightGreen();
            case PURPLE -> createPurple();
            case WHITE -> createWhite();
            case BLACK -> createBlack();
            default -> createBrown();
        };
    }
    
    private AIModel createBrown() {
        var model = new AIModel();
        model.add(new Sniper(FastMath.PI*0.01f));
        return model;
    }
    private AIModel createGrey() {
        return createBrown();
    }
    private AIModel createGreen() {
        return createBrown();
    }
    private AIModel createYellow() {
        return createBrown();
    }
    private AIModel createRed() {
        return createBrown();
    }
    private AIModel createLightGreen() {
        return createBrown();
    }
    private AIModel createPurple() {
        return createBrown();
    }
    private AIModel createWhite() {
        return createBrown();
    }
    private AIModel createBlack() {
        return createBrown();
    }
    
}
