/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.tanks.util.debug;

import codex.tanks.es.ESAppState;
import com.jme3.app.Application;
import com.jme3.font.BitmapText;
import com.simsilica.es.EntitySet;
import java.util.HashMap;

/**
 *
 * @author codex
 */
public class DebugState extends ESAppState {

    private EntitySet watchers;
    private HashMap<Object, BitmapText> console = new HashMap<>();
    
    @Override
    protected void init(Application app) {
        super.init(app);
        watchers = ed.getEntities(ComponentWatcher.class);
    }
    @Override
    protected void cleanup(Application app) {}
    @Override
    protected void onEnable() {}
    @Override
    protected void onDisable() {}
    @Override
    public void update(float tpf) {
        watchers.applyChanges();
        for (var e : watchers) {
            System.out.println("Watching "+e.getId());
            for (var t : e.get(ComponentWatcher.class).getTypes()) {
                System.out.println("  "+t.getSimpleName()+" = "+ed.getComponent(e.getId(), t));
            }
        }
    }
    
    public void print(Object object, String text) {
        var bitmap = console.get(object);
        if (bitmap == null) {
            bitmap = createPrintLine(object);
        }
        bitmap.setText(object.getClass().getSimpleName()+" : "+text);
    }
    private BitmapText createPrintLine(Object object) {
        var font = assetManager.loadFont("Interface/Fonts/Default.fnt");
        var text = new BitmapText(font);
        text.setSize(15f);
        text.setText("");
        text.setLocalTranslation(5, windowSize.y-console.size()*text.getLineHeight()-5f, 0);
        guiNode.attachChild(text);
        console.putIfAbsent(object, text);
        return text;
    }
    
}
