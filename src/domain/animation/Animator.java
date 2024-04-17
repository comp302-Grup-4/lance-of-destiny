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
	private boolean staffMovesRight = false, staffMovesLeft = false;
	
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
				Vector forceDirection, velocityChange;
				while (true) {
					forceDirection = collisionCalculator.checkCollision(ball, animationObjects.stream()
																							.filter(x -> !x.equals(ball))
																							.map(x -> (Collidable) x)
																							.toList());
					
					velocityChange = forceDirection.scale(-2 * ball.getVelocity().dot(forceDirection));
					ball.setVelocity(ball.getVelocity().add(velocityChange));
					ball.move(dTime);
					
					if (staffMovesLeft && !staffMovesRight) {
						staff.setVelocity(Vector.of(-200, 0));
					} else if (!staffMovesLeft && staffMovesRight) {
						staff.setVelocity(Vector.of(200, 0));
					} else {
						staff.setVelocity(Vector.zero());
					}
					
					if (staff.getNextPosition(dTime).x < 985 - staff.getLength() &&
						staff.getNextPosition(dTime).x > 15) {
						
						staff.move(dTime);
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
	
	//deneme
	public void setBarrierGrid(int simple, int firm, int explosive, int gift) throws InvalidBarrierNumberException {
			this.barrierGrid = new BarrierGrid(simple, firm, explosive, gift);	
			initializeAnimationObjects();
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
		if (direction == RIGHT) {
			staffMovesRight = true;
		} else if (direction == LEFT) {
			staffMovesLeft = true;
		}
	}
	
	public void stopMagicalStaff(int direction) {
		if (direction == RIGHT) {
			staffMovesRight = false;
		} else if (direction == LEFT) {
			staffMovesLeft = false;
		}
	}
}
