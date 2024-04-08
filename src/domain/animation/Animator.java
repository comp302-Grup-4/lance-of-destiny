package domain.animation;

import java.util.HashSet;
import exceptions.InvalidBarrierNumberException;

public class Animator {
	private final float FPS = 30;
	private final long dTime = (long) (1000 / FPS);
	
	private FireBall ball;
	private MagicalStaff staff;
	protected BarrierGrid barrierGrid;
	private HashSet<Movable> movableObjects;
	private Thread animationThread;
	
	public Animator() {
		ball = new FireBall();
		staff = new MagicalStaff();
		try {
			barrierGrid = new BarrierGrid(BarrierGrid.MIN_SIMPLE_BARRIERS,
										  BarrierGrid.MIN_FIRM_BARRIERS,
										  BarrierGrid.MIN_EXPLOSIVE_BARRIERS,
										  BarrierGrid.MIN_GIFT_BARRIERS);
		} catch (InvalidBarrierNumberException e) {
			e.printStackTrace();
		}
		
		movableObjects = initializeMovableObjects();
		
	}
	
	public void run() {
		animationThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) { // TODO
					Vector nextPosition, nextVelocity;
					for (Movable object : movableObjects) {
						nextPosition = object.getNextPosition(1 / FPS);
						nextVelocity = checkCollision(object, nextPosition);
						object.setVelocity(nextVelocity);
						object.move(dTime);
						try {
							Thread.sleep(dTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
		animationThread.start();
	}
	
	public void pause() throws InterruptedException {
		animationThread.wait();
	}
	
	public void resume() {
		animationThread.notify();
	}
	
	private Vector checkCollision(Movable object, Vector nextPosition) {
		Vector nextVelocity = new Vector(0, 0);
		for (Movable otherObject : movableObjects) {
			if (object.equals(otherObject) &&  object.isCollidable()) {
				
			}
		}
		return nextVelocity;
	}
	
	private HashSet<Movable> initializeMovableObjects() {
		HashSet<Movable> movableObjects = new HashSet<Movable>();
		addMovableObject(ball);
		addMovableObject(staff);
		for (Movable barrier : barrierGrid.getBarrierList()) {
			addMovableObject(barrier);
		}
		return movableObjects;
	}
	
	public BarrierGrid getBarrierGrid() {
		return barrierGrid;
	}
	
	public void addMovableObject(Movable movable) {
		this.movableObjects.add(movable);
	}
	
	public void removeMovableObject(Movable movable) {
		this.movableObjects.remove(movable);
	}
	
	public HashSet<Movable> getMovableObjects() {
		return movableObjects;
	}
}
