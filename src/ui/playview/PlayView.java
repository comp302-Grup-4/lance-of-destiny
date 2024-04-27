package ui.playview;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.stream.Stream;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import domain.Game;
import domain.animation.Animator;

public class PlayView extends JPanel {
	private static float FPS = 80;
	private static final long serialVersionUID = 6L;
	private Animator animator;
	private AnimatorUIConverter converter;
	private Thread drawingThread, focusThread;
	private HashMap<Integer, JComponent> drawnObjects;
	/**
	 * Create the panel.
	 */
	public PlayView(JPanel parent, Game game) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int windowHeight = screenSize.height;
		int windowWidth = screenSize.width;
		
		drawnObjects = new HashMap<>();
		this.animator = game.getAnimator();
		this.converter = new AnimatorUIConverter(animator, new Dimension(windowWidth, windowHeight));

		this.setLayout(null);
		this.setVisible(true);
		this.setFocusable(true);
		
		drawingThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					rebuildDrawableObjects(converter.getObjectSpatialInfoList());
					PlayView.this.repaint();
					try {
						Thread.sleep((long) (1 / FPS));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		focusThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (!(KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() instanceof PlayView)) {
					PlayView.this.grabFocus();
				}
			}
		});
		
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					animator.moveMagicalStaff(Animator.LEFT);
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					animator.moveMagicalStaff(Animator.RIGHT);
				}
				else if (e.getKeyCode() == KeyEvent.VK_A) {
					animator.rotateMagicalStaff(Animator.LROTATE);
				} else if (e.getKeyCode() == KeyEvent.VK_D) {
					animator.rotateMagicalStaff(Animator.RROTATE);
				}
				 else if (e.getKeyCode() == KeyEvent.VK_W) {
					startPlay();
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					if (animator.isPaused()) {
						try {
							animator.resume();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					} else {
						animator.pause();
					}
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					animator.stopMagicalStaff(Animator.LEFT);
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					animator.stopMagicalStaff(Animator.RIGHT);
				}
				else if (e.getKeyCode() == KeyEvent.VK_A) {
					animator.stopRotationOfMagicalStaff(Animator.LROTATE);
					
				}else if (e.getKeyCode() == KeyEvent.VK_D) {
					animator.stopRotationOfMagicalStaff(Animator.RROTATE);
				}
			}
		});
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				startPlay();
			}
		});
		
		focusOnPlayView();

		drawingThread.start();
	}
	
	public void focusOnPlayView() {
		rebuildDrawableObjects(converter.getObjectSpatialInfoList());
		PlayView.this.repaint();

		focusThread.start();
	}
	
	public void startPlay() {
		try {
			animator.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	private void updateDrawableObject(SpatialObject newObj) {
		
		  SpatialObject prevObj = (SpatialObject) drawnObjects.get(newObj.ID);
		  prevObj.setCenter(newObj.getCenter());
		  prevObj.setRotation(newObj.rotation);
		  prevObj.setIcon(newObj.getImage());
		  
	}
	
	private void addDrawableObject(SpatialObject newObj) {
//		JLabel newObj = new JLabel(newObjInfo.getImage());
		
		this.add(newObj);
		this.revalidate();
		this.repaint();
		drawnObjects.put(newObj.ID, newObj);
	}
	
	private void removeDrawableObject(Integer objID) {
		this.remove(drawnObjects.get(objID));
		this.revalidate();
		this.repaint();
	}

}
