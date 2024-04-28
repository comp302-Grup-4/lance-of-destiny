package ui.playview;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.stream.Stream;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import domain.Game;
import domain.animation.Vector;
import domain.animation.barriers.Barrier;
import exceptions.InvalidBarrierNumberException;
import exceptions.InvalidBarrierPositionException;


public class BuildView extends JPanel {
    private static final long serialVersionUID = 7L; 
    private AnimatorUIConverter converter;
    private HashMap<Integer, JComponent> drawnObjects;
    private Game game;
    private Vector offset = null; 
    
    private Vector init;
    
    private int windowHeight;
    private int windowWidth;

    private JPanel parent;
    
	/**
     * Create the panel.
     */
    public BuildView(JPanel parent, Game game) {
    	this.parent=parent;
    	Dimension parentSize = parent.getSize();
        this.windowHeight = parentSize.height;
        this.windowWidth = parentSize.width;
        
               
        this.game = game;
        drawnObjects = new HashMap<>();
        this.converter = new AnimatorUIConverter(game.getAnimator(), new Dimension(windowWidth, windowHeight));
        
        this.setLayout(null);
        this.setVisible(true);
        this.setFocusable(true);
        
        rebuildDrawableObjects(converter.getObjectSpatialInfoList());
    }
    

    
    private void rebuildDrawableObjects(HashMap<Integer, SpatialObject> newObjects) {
		Stream<Integer> toBeDeleted = drawnObjects.keySet()
				.stream()
				.filter(x -> !newObjects.containsKey(x));
		
		toBeDeleted.forEach(x -> removeDrawableObject(x));
		
		drawnObjects.keySet().retainAll(newObjects.keySet()); // remove all non-existent objects in new info
		for (Integer id : newObjects.keySet()) { // adjust each object in drawn objects
			if (drawnObjects.containsKey(id)) { // if new object was already in drawn objects
				updateDrawableObject(newObjects.get(id));
			} else {
				addDrawableObject(newObjects.get(id));
			}
		}
    }	
    
    private void removeDrawableObject(Integer objID) {
		this.remove(drawnObjects.get(objID));
		this.revalidate();
		this.repaint();
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

    	Barrier b = (Barrier) newObjInfo.getAnimationObject();

 
    	obj.addMouseListener(new MouseAdapter() {
           
            @Override
            public void mousePressed(MouseEvent e) {
            	if (e.getButton() == MouseEvent.BUTTON1) {
	                offset = new Vector(e.getX(), e.getY());
	                float a = e.getXOnScreen();
	                float b = e.getYOnScreen();
	                init = new Vector(a,b);
            	} else if (e.getButton() == MouseEvent.BUTTON3) {
	                float a = e.getXOnScreen();
	                float b = e.getYOnScreen();
	                init = new Vector(a,b);
            		int res = JOptionPane.showConfirmDialog(BuildView.this, 
            				"Delete the barrier?", 
            				"Barrier Deletion", 
            				JOptionPane.OK_CANCEL_OPTION);
            		if (res == JOptionPane.OK_OPTION) {
            			try {
							game.getAnimator()
							    .deleteBarrierAt(converter.convertPositionToAnimator(init.subtract(new Vector(parent.getLocationOnScreen().x, 
							                                                      parent.getLocationOnScreen().y))));
						} catch (InvalidBarrierNumberException e1) {
	    					JOptionPane.showMessageDialog(parent, "Min barrier number requirement not satified.", "Error", JOptionPane.ERROR_MESSAGE);
						}
            			rebuildDrawableObjects(converter.getObjectSpatialInfoList());            		}
            	}
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            	if (offset != null) {
            		float i = e.getXOnScreen() - parent.getLocationOnScreen().x;
                    float j = e.getYOnScreen() - parent.getLocationOnScreen().y;

                    try {
    					game.getAnimator()
    					    .getBarrierGrid()
    					    .changeBarrierPosition(b, 
    							converter.convertPositionToAnimator(new Vector(i,j)), 
    							converter.convertPositionToAnimator(init.subtract(new Vector(parent.getLocationOnScreen().x, 
    									                                                parent.getLocationOnScreen().y))));
    				} catch (InvalidBarrierPositionException e1) {
    					JOptionPane.showMessageDialog(parent, "Invalid barrier position", "Error", JOptionPane.ERROR_MESSAGE);
    				}
                    offset = null;
                
                    rebuildDrawableObjects(converter.getObjectSpatialInfoList());
            	}
            }
        });

        obj.addMouseMotionListener(new MouseAdapter() {  
        	 
            @Override
            public void mouseDragged(MouseEvent e) {
                if (offset != null) {


                    float x = e.getXOnScreen() - offset.getX() - parent.getLocationOnScreen().x;
                    float y = e.getYOnScreen() - offset.getY() - parent.getLocationOnScreen().y;

                    obj.setLocation((int)x , (int)y);

                }
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	if (offset != null) {
    		g.setColor(Color.red);
    		g.drawLine(0,
    				(int) game.getAnimator().getBarrierGrid().getSize().getHeight(), 
    				this.getWidth(),
    				(int) game.getAnimator().getBarrierGrid().getSize().getHeight());
    	}
    	this.repaint();
    		
    }
}
    


