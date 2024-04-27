package ui.playview;


import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import domain.Game;
import domain.animation.Animator;
import domain.animation.BarrierGrid;
import domain.animation.Vector;
import domain.animation.barriers.Barrier;
import domain.animation.barriers.ExplosiveBarrier;
import exceptions.InvalidBarrierPositionException;
import ui.BuildingScreen;
import ui.playview.AnimatorAdapter;
import ui.playview.ObjectSpatialInfo;

public class BuildView extends JPanel {
    private static final long serialVersionUID = 7L; 
    private AnimatorAdapter converter;
    private HashMap<Integer, JComponent> drawnObjects;
    private Game game;
    private Vector offset; 
    
    private Vector init;
    
    private int windowHeight;
    private int windowWidth;

    private float MARGIN = (float) 0.15;
	private int width;
	private int height;
	private JPanel parent;
    
	private Vector position;
    /**
     * Create the panel.
     */
    public BuildView(JPanel parent, Game game) {
    	this.parent=parent;
    	Dimension parentSize = parent.getSize();
        this.windowHeight = parentSize.height;
        this.windowWidth = parentSize.width;
//        System.out.println(parentSize.height);
//        System.out.println(parentSize.width);
//        System.out.println(Toolkit.getDefaultToolkit().getScreenSize().height);
        width= this.WIDTH;
        height= this.HEIGHT;
       
        this.game = game;
        drawnObjects = new HashMap<>();
        this.converter = new AnimatorAdapter(game.getAnimator(), new Dimension(windowWidth, windowHeight), this);
        
        this.position = new Vector(24.0,49.0);

        this.setLayout(null);
        this.setVisible(true);
        this.setFocusable(true);

        rebuildDrawableObjects(converter.getObjectSpatialInfoList());
    }
    
    
    private void rebuildDrawableObjects(HashMap<Integer, ObjectSpatialInfo> newObjectsInfo) {
 
        drawnObjects.keySet().retainAll(newObjectsInfo.keySet()); // remove all non-existent objects in new info
        for (Integer id : newObjectsInfo.keySet()) { // adjust each object in drawn objects
        	ObjectSpatialInfo newObjInfo = newObjectsInfo.get(id);
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
      
        if(newObjInfo.getAnimationObject() instanceof Barrier) {
        	addDragDropFunctionality(newObj, newObjInfo);
        }
 
        
        this.add(newObj);
        this.revalidate();
        this.repaint();
        drawnObjects.put(newObjInfo.ID, newObj);
    }
    
    
    private void addDragDropFunctionality(JLabel obj, ObjectSpatialInfo newObjInfo) {
    	Barrier b = (Barrier) newObjInfo.getAnimationObject();
    	obj.addMouseListener(new MouseAdapter() {
           

            @Override
            public void mousePressed(MouseEvent e) {
            	
                offset = new Vector(e.getX(), e.getY());
                float a = e.getXOnScreen(); //??
                float b = e.getYOnScreen();//??
                init = new Vector(a,b);
//                offset= converter.convertPosition(offset);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            	float i = e.getXOnScreen() - offset.getX();
                float j = e.getYOnScreen() - offset.getY();
                         
                Vector p = converter.convertPosition(new Vector(i,j));
                init = converter.convertPosition(init);
                
                try {
					game.getAnimator().getBarrierGrid().changeBarrierPosition(b, p, init);
				} catch (InvalidBarrierPositionException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(parent, "Invalid barrier position", "Error", JOptionPane.ERROR_MESSAGE);
				}
                offset = null;
            
                rebuildDrawableObjects(converter.getObjectSpatialInfoList());
              //  game.getAnimator().getBarrierGrid().printBarrierArray();
               
              
            }
        });

        obj.addMouseMotionListener(new MouseAdapter() {  
        	 
            @Override
            public void mouseDragged(MouseEvent e) {
                if (offset != null) {
                    float x = e.getXOnScreen() - offset.getX();
                    float y = e.getYOnScreen() - offset.getY();

                    obj.setLocation((int)x , (int)y);
                    
                   
                }
            }
        });
    }
}
