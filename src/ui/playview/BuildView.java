package ui.playview;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.Game;
import domain.animation.Animator;
import ui.playview.AnimatorUIConverter;
import ui.playview.ObjectSpatialInfo;

public class BuildView extends JPanel {
    private static final long serialVersionUID = 7L; // Update the serial version UID
    private AnimatorUIConverter converter;
    private HashMap<Integer, JComponent> drawnObjects;
    private Game game;

    /**
     * Create the panel.
     */
    public BuildView(JPanel parent, Game game) {
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	Dimension parentSize = parent.getSize();
        int windowHeight = parentSize.height;
        int windowWidth = parentSize.width;
        
        this.game = game;
        drawnObjects = new HashMap<>();
        this.converter = new AnimatorUIConverter(game.getAnimator(), new Dimension(windowWidth, windowHeight));

        this.setLayout(null);
        this.setVisible(true);
        this.setFocusable(true);

        rebuildDrawableObjects(converter.getObjectSpatialInfoList());
    }

    
    private void rebuildDrawableObjects(HashMap<Integer, ObjectSpatialInfo> newObjectsInfo) {
        drawnObjects.keySet().retainAll(newObjectsInfo.keySet()); // remove all non-existent objects in new info
        for (Integer id : newObjectsInfo.keySet()) { // adjust each object in drawn objects
            if (drawnObjects.containsKey(id)) { // if new object was already in drawn objects
                updateDrawableObject(newObjectsInfo.get(id));
            } else {
                addDrawableObject(newObjectsInfo.get(id));
            }
        }
    }
    
    private void updateDrawableObject(ObjectSpatialInfo newObjInfo) {
        drawnObjects.get(newObjInfo.ID).setBounds((int) newObjInfo.position.getX(),
                (int) newObjInfo.position.getY(), 
                (int) newObjInfo.getSizeX(),
                (int) newObjInfo.getSizeY());
    }
    
    private void addDrawableObject(ObjectSpatialInfo newObjInfo) {
        JLabel newObj = new JLabel(newObjInfo.getImage());
        newObj.setBounds((int) newObjInfo.position.getX(), 
                (int) newObjInfo.position.getY(), 
                (int) newObjInfo.getSizeX(),
                (int) newObjInfo.getSizeY());
        this.add(newObj);
        this.revalidate();
        this.repaint();
        drawnObjects.put(newObjInfo.ID, newObj);
    }
}
