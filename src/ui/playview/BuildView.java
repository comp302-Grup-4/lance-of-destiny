package ui.playview;


import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.Game;
import domain.animation.Animator;
import domain.animation.BarrierGrid;
import domain.animation.Vector;
import domain.animation.barriers.Barrier;
import domain.animation.barriers.ExplosiveBarrier;
import ui.playview.AnimatorUIConverter;
import ui.playview.SpatialObject;

public class BuildView extends JPanel {
    private static final long serialVersionUID = 7L; 
    private AnimatorUIConverter converter;
    private HashMap<Integer, JComponent> drawnObjects;
    private Game game;
    private Point offset; 
    
    private float MARGIN = (float) 0.15;
    
    /**
     * Create the panel.
     */
    public BuildView(JPanel parent, Game game) {
    	Dimension parentSize = parent.getSize();
        int windowHeight = parentSize.height;
        int windowWidth = parentSize.width;
        
//        System.out.println(parentSize.height);
//        System.out.println(parentSize.width);
//        System.out.println(Toolkit.getDefaultToolkit().getScreenSize().height);
       
        this.game = game;
        drawnObjects = new HashMap<>();
        this.converter = new AnimatorUIConverter(game.getAnimator(), new Dimension(windowWidth, windowHeight));
        
        

        this.setLayout(null);
        this.setVisible(true);
        this.setFocusable(true);

        rebuildDrawableObjects(converter.getObjectSpatialInfoList());
    }

    
    private void rebuildDrawableObjects(HashMap<Integer, SpatialObject> newObjectsInfo) {
        drawnObjects.keySet().retainAll(newObjectsInfo.keySet()); // remove all non-existent objects in new info
        for (Integer id : newObjectsInfo.keySet()) { // adjust each object in drawn objects
        	SpatialObject newObjInfo = newObjectsInfo.get(id);
            if (drawnObjects.containsKey(id)) { // if new object was already in drawn objects
                updateDrawableObject(newObjectsInfo.get(id));
            } else {
                addDrawableObject(newObjectsInfo.get(id));
            }
        }
    }
    
    private void updateDrawableObject(SpatialObject newObjInfo) {
        drawnObjects.get(newObjInfo.ID).setBounds((int) newObjInfo.position.getX(),
                (int) newObjInfo.position.getY(), 
                (int) newObjInfo.getSizeX(),
                (int) newObjInfo.getSizeY());
    }
    
    private void addDrawableObject(SpatialObject newObjInfo) {
        JLabel newObj = new JLabel(newObjInfo.getImage());
        newObj.setBounds((int) newObjInfo.position.getX(), 
                (int) newObjInfo.position.getY(), 
                (int) newObjInfo.getSizeX(),
                (int) newObjInfo.getSizeY());
      
        if(newObjInfo.getAnimationObject() instanceof Barrier) {
        	addDragDropFunctionality(newObj, newObjInfo);
        }
 
        
        this.add(newObj);
        this.revalidate();
        this.repaint();
        drawnObjects.put(newObjInfo.ID, newObj);
    }
    
    
    private void addDragDropFunctionality(JLabel obj, SpatialObject newObjInfo) {
 
    	obj.addMouseListener(new MouseAdapter() {
           

            @Override
            public void mousePressed(MouseEvent e) {
                offset = new Point(e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                offset = null;
             //   newObjInfo.setPosition(new Vector(obj.getX(), obj.getY()));
               
                rebuildDrawableObjects(converter.getObjectSpatialInfoList());
     
              
            }
        });

        obj.addMouseMotionListener(new MouseAdapter() {  
        	 
            @Override
            public void mouseDragged(MouseEvent e) {
                if (offset != null) {
                    int x = e.getXOnScreen() - offset.x;
                    int y = e.getYOnScreen() - offset.y;
                    obj.setLocation(x, y);
                    Barrier b = (Barrier) newObjInfo.getAnimationObject();
                    float colWidth = b.getSizeX() * (1 + 2 * MARGIN);
        			float rowHeight = b.getSizeX() * (1 + 2 * MARGIN);
                    int col = (int) ((x - b.getSizeX() * MARGIN) / colWidth);
                    col--;
                    int row = (int) ((y- b.getSizeX() * MARGIN) / rowHeight);
                    row--;
                 //   Barrier b = (Barrier) newObjInfo.getAnimationObject();
                    b.setGridPosition(col, row);
//                    System.out.println(newObjInfo.getSizeX());
//                    b.setPosition(new Vector((int)(colWidth * col + b.getSizeX() * MARGIN + b.getSizeX()), (int)(rowHeight * row + b.getSizeY() * MARGIN + 2*b.getSizeY())));
                }
            }
        });
    }
}
