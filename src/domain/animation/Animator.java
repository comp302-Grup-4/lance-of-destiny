package domain.animation;

import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArraySet;

import domain.Game;
import domain.animation.barriers.Barrier;
import domain.animation.collision.CollisionInfo;
import domain.animation.collision.CollisionStrategy;
import domain.animation.collision.PointBasedCollision;
import exceptions.InvalidBarrierNumberException;

public class Animator {
	public static int RIGHT = 1;
	public static int LEFT = -1;
	public static int RROTATE = 1;
	public static int LROTATE = -1;

	private final float FPS = 150;
	private final long dTime = (long) (1000 / FPS);

	private FireBall ball;
	private MagicalStaff staff;
	protected BarrierGrid barrierGrid;
	private Wall rightWall, leftWall, upperWall, lowerWall;
	private CopyOnWriteArraySet<AnimationObject> animationObjects;
	private Thread animationThread;
	private CollisionStrategy collisionCalculator;
	private boolean staffMovesRight = false, staffMovesLeft = false;
	private boolean staffRotatesRight = false, staffRotatesLeft = false;
	private Game game;
	boolean paused = false;

	public Animator(Game game) {
		this.game = game;
		ball = new FireBall();
		staff = new MagicalStaff();
		try {
			barrierGrid = new BarrierGrid(BarrierGrid.MIN_SIMPLE_BARRIERS, BarrierGrid.MIN_FIRM_BARRIERS,
					BarrierGrid.MIN_EXPLOSIVE_BARRIERS, BarrierGrid.MIN_GIFT_BARRIERS);
		} catch (InvalidBarrierNumberException e) {
			e.printStackTrace();
		}
		rightWall = new Wall(Wall.VERTICAL, new Vector(985, 0));
		leftWall = new Wall(Wall.VERTICAL, new Vector(-15, 0));
		upperWall = new Wall(Wall.HORIZONTAL, new Vector(0, -15));
		lowerWall = new Wall(Wall.HORIZONTAL, new Vector(0, 800));

		initializeAnimationObjects();
		collisionCalculator = new PointBasedCollision();

		initAnimationThread();
	}

	public void run() throws Exception {
		paused = false;
		switch (animationThread.getState()) {
		case NEW:
			animationThread.start();
			break;

		case TERMINATED:
			initAnimationThread();
			animationThread.start();

		default:
			break;
		}

	}

	private void initAnimationThread() {
		animationThread = new Thread(new Runnable() {
			@Override
			public void run() {
				CollisionInfo ballCollisionInfo;
				Vector forceDirection, velocityChange;
				while (!paused) {
					try {
						Thread.sleep(dTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					ballCollisionInfo = collisionCalculator.checkCollision(ball, getAnimationObjects().stream()
							.filter(x -> !x.equals(ball)).map(x -> (Collidable) x).toList());

					forceDirection = ballCollisionInfo.getNextDirection();
					velocityChange = forceDirection.scale(-2 * ball.getVelocity().dot(forceDirection));

					ball.setVelocity(ball.getVelocity().add(velocityChange));
					ball.move(dTime);

					for (Collidable collidedObject : ballCollisionInfo.getCollidedObjects()) {
						if (collidedObject instanceof Barrier) {
							removeAnimationObject((AnimationObject) collidedObject);
						} else if (collidedObject == lowerWall) {
							ball.reset();
							staff.reset();
							game.getPlayer().decrementChances();
							pause();
							break;
						}
					}

				    if (!staffRotatesLeft && staffRotatesRight) {
				        staff.setAngularVelocity(180);//D throws right
				    } else if (!staffRotatesRight && staffRotatesLeft) {
				    	staff.setAngularVelocity(-180);//A//throws left
				    } else {
				    	// this provides a smooth turn back to original position
				    	// -12 is a magic number
				    	staff.setAngularVelocity(-12 * staff.getRotation());
				    }
				 
				    if (staff.getNextRotation(dTime) > 45 || staff.getNextRotation(dTime) < -45) {
				    	staff.setAngularVelocity(0);
				    }
				    
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
				    
				}}});

	}

	public void pause() {
		paused = true;
	}

	public boolean isPaused() {
		return paused;
	}

	public void resume() throws Exception {
		paused = false;
		run();
	}

	private void initializeAnimationObjects() {
		animationObjects = new CopyOnWriteArraySet<AnimationObject>();
		addAnimationObject(ball);
		addAnimationObject(staff);
		for (AnimationObject barrier : barrierGrid.getBarrierList()) {
			addAnimationObject(barrier);
		}
		addAnimationObject(leftWall);
		addAnimationObject(rightWall);
		addAnimationObject(upperWall);
		addAnimationObject(lowerWall);
	}

//	// deneme
//	public void setBarrierGrid(int simple, int firm, int explosive, int gift) throws InvalidBarrierNumberException {
//		this.barrierGrid = new BarrierGrid(simple, firm, explosive, gift);
//		initializeAnimationObjects();
	
	//deneme
	public void setBarrierGrid(BarrierGrid newbg) throws InvalidBarrierNumberException {
			this.barrierGrid = newbg;	
			initializeAnimationObjects();
	}

	public BarrierGrid getBarrierGrid() {
		return barrierGrid;
	}

	private void addAnimationObject(AnimationObject movable) {
		animationObjects.add(movable);
	}

	private void removeAnimationObject(AnimationObject movable) {
		animationObjects.remove(movable);
	}

	public CopyOnWriteArraySet<AnimationObject> getAnimationObjects() {
		return animationObjects;
	}

	public void moveMagicalStaff(int direction) {
		if (direction == RIGHT) {
			staffMovesRight = true;
		} else if (direction == LEFT) {
			staffMovesLeft = true;
		}
	}

	public void rotateMagicalStaff(int direction) {
		if (direction == RROTATE) {
			staffRotatesRight = true;
		} else if (direction == LROTATE) {
			staffRotatesLeft = true;
		}
	}

	public void stopMagicalStaff(int direction) {
		if (direction == RIGHT) {
			staffMovesRight = false;
		} else if (direction == LEFT) {
			staffMovesLeft = false;
		}
	}

	public void stopRotationOfMagicalStaff(int direction) {
		if (direction == RROTATE) {
			staffRotatesRight = false;
		} else if (direction == LROTATE) {
			staffRotatesLeft = false;
		}
	}
}
