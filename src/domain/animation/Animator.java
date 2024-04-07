package domain.animation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

public class Animator {
	private FireBall ball;
	private MagicalStaff staff;
	private BarrierGrid barrierGrid;
	
	private LinkedList<Movable> movableObjects;
	
	public Animator() {
		ball = new FireBall();
		staff = new MagicalStaff();
		barrierGrid = new BarrierGrid();
		
		movableObjects = initializeMovableObjects();
		
	}
	
	public void run() {
		Thread animationThread = new Thread(new Runnable() {
			@Override
			public void run() {
				for (Movable movable : movableObjects) {
					
				}
			}
		});
		animationThread.start();
	}
	
	public void update() {
		
	}
	
	public void resume() {
		
	}
	
	private LinkedList<Movable> initializeMovableObjects() {
		LinkedList<Movable> movableObjects = new LinkedList<Movable>();
		movableObjects.add(ball);
		movableObjects.add(staff);
		/*
		for (Movable barrier : barrierGrid.getBarriers()) {
			movableObjects.add(barrier);
		}*/
		return movableObjects;
	}
	
	public LinkedList<Movable> getMovableObjects() {
		return movableObjects;
	}
}
