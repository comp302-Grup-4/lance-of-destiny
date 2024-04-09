package domain.animation;

import java.util.Collection;
import java.util.HashSet;

import domain.animation.collision.CollisionStrategy;
import domain.animation.collision.PointBasedCollision;
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
	private Wall rightWall, leftWall, upperWall, lowerWall;
	private HashSet<AnimationObject> animationObjects;
	private Thread animationThread;
	private CollisionStrategy collisionCalculator;
	
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
		rightWall = new Wall(Wall.VERTICAL, new Vector(985, 0));
		leftWall = new Wall(Wall.VERTICAL, new Vector(-15, 0));
		upperWall = new Wall(Wall.HORIZONTAL, new Vector(0, -15));
		lowerWall = new Wall(Wall.HORIZONTAL, new Vector(0, 800));
		
		initializeAnimationObjects();
		collisionCalculator = new PointBasedCollision();
	}
	
	public void run() {
		animationThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) { // TODO
					Vector forceDirection, velocityChange;
					for (AnimationObject object : animationObjects) {
						if (!object.getVelocity().isZero()) { // if the object doesn't move, no need to do other stuff
							forceDirection = collisionCalculator.checkCollision(object, animationObjects.stream()
																									.filter(x -> !x.equals(object))
																									.map(x -> (Collidable) x)
																									.toList());
							
							velocityChange = forceDirection.scale(-2 * object.getVelocity().dot(forceDirection));
							object.setVelocity(object.getVelocity().add(velocityChange));
							object.move(dTime);
						}
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
	
	private void initializeAnimationObjects() {
		animationObjects = new HashSet<AnimationObject>();
		addMovableObject(ball);
		addMovableObject(staff);
		for (AnimationObject barrier : barrierGrid.getBarrierList()) {
			addMovableObject(barrier);
		}
		addMovableObject(leftWall);
		addMovableObject(rightWall);
		addMovableObject(upperWall);
		addMovableObject(lowerWall);
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
