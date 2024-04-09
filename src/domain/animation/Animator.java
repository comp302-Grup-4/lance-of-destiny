package domain.animation;

import java.util.HashSet;
import exceptions.InvalidBarrierNumberException;

public class Animator {
	public static int RIGHT = 1;
	public static int LEFT = -1;
	public static int STOP = 0;
	
	private final float FPS = 150;
	private final long dTime = (long) (1000 / FPS);
	
	private FireBall ball;
	private MagicalStaff staff;
	protected BarrierGrid barrierGrid;
	private HashSet<AnimationObject> animationObjects;
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
		
		initializeAnimationObjects();
		
	}
	
	public void run() {
		animationThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) { // TODO
					Vector nextPosition, nextVelocity;
					for (Movable object : animationObjects) {
						nextPosition = object.getNextPosition(dTime);
						nextVelocity = checkCollision(object, nextPosition);
						object.setVelocity(nextVelocity);
						object.move(dTime);
					}
					try {
						Thread.sleep(dTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
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
		Vector nextVelocity = object.getVelocity();
		for (Movable otherObject : animationObjects) {
			if (object.equals(otherObject) &&  object.isCollidable()) {
				
			}
		}
		return nextVelocity;
	}
	
	private void initializeAnimationObjects() {
		animationObjects = new HashSet<AnimationObject>();
		addMovableObject(ball);
		addMovableObject(staff);
		for (AnimationObject barrier : barrierGrid.getBarrierList()) {
			addMovableObject(barrier);
		}
	}
	
	public BarrierGrid getBarrierGrid() {
		return barrierGrid;
	}
	
	public void addMovableObject(AnimationObject movable) {
		this.animationObjects.add(movable);
	}
	
	public void removeMovableObject(Movable movable) {
		this.animationObjects.remove(movable);
	}
	
	public HashSet<AnimationObject> getMovableObjects() {
		return animationObjects;
	}
	
	public void moveMagicalStaff(int direction) {
		staff.setVelocity(new Vector(200 * direction, 0));
	}
}
