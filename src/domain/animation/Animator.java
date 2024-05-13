package domain.animation;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

import domain.Game;
import domain.animation.barriers.Barrier;
import domain.animation.barriers.ExplosiveBarrier;
import domain.animation.barriers.PurpleBarrier;
import domain.animation.barriers.ReinforcedBarrier;
import domain.animation.barriers.RewardingBarrier;
import domain.animation.barriers.SimpleBarrier;
import domain.animation.collision.CollisionInfo;
import domain.animation.collision.CollisionStrategy;
import domain.animation.collision.PointBasedCollision;
import domain.animation.spells.DoubleAccel;
import domain.animation.spells.Spell;
import domain.animation.spells.SpellFactory;
import exceptions.InvalidBarrierNumberException;
import ui.GameApp;

public class Animator implements Serializable{
	private static final long serialVersionUID = -3426545588581994135L;
	public static int RIGHT = 1;
	public static int LEFT = -1;
	public static int RROTATE = 1;
	public static int LROTATE = -1;

	private final float FPS = 150;
	private final long dTime = (long) (1000 / FPS);
	GameApp g = GameApp.getInstance();
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
	private long startTimeMilli = 0;
	private SpellDepot spellDepot = new SpellDepot();
	private static final SpellFactory spellFactory = SpellFactory.getInstance();
	private int totalBarriers;
	
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
		this.totalBarriers = barrierGrid.getTotalBarrierNumber();
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
		if (startTimeMilli == 0)
			startTimeMilli = System.currentTimeMillis();
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
				CollisionInfo ballSolidCollision, ballCollisionInfo, ballFrozenCollision, spellCollisionInfo;
				Vector forceDirection, velocityChange;
				while (!paused) {
					try {
						Thread.sleep(dTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					// ball collision

					// collision with walls and magical staff
					ballSolidCollision = collisionCalculator.checkCollision(ball, getAnimationObjects().stream()
							.filter(x -> x instanceof Wall || x instanceof MagicalStaff)
							.map(x -> (Collidable) x)
							.toList());
					
					// collision with non frozen barriers 
					ballCollisionInfo = collisionCalculator.checkCollision(ball, getAnimationObjects().stream()
							.filter(x -> x instanceof Barrier && !(((Barrier) x).isFrozen()))
							.map(x -> (Collidable) x)
							.toList());
					
					// collision with frozen barriers
					ballFrozenCollision = collisionCalculator.checkCollision(ball, getAnimationObjects().stream()
							.filter(x -> x instanceof Barrier && (((Barrier) x).isFrozen()))
							.map(x -> (Collidable) x)
							.toList());
					
					if (!ball.isOverwhelming()) {
						
						forceDirection = ballCollisionInfo.getNextDirection().add(ballSolidCollision.getNextDirection().add(ballFrozenCollision.getNextDirection()));
					} else {
						
						forceDirection = ballSolidCollision.getNextDirection().add(ballFrozenCollision.getNextDirection());
					}
					
					velocityChange = forceDirection.scale(-2 * ball.getVelocity().dot(forceDirection));

					ball.setVelocity(ball.getVelocity().add(velocityChange));
					
					ball.move(dTime);
					
					HashSet<Barrier> brokenBarriers = new HashSet<>();
					ArrayList<Collidable> ballBarrierCollision = ballCollisionInfo.getCollidedObjects();
					ballBarrierCollision.addAll(ballFrozenCollision.getCollidedObjects());
					
					for (Collidable collidedObject : ballBarrierCollision) {
						if (collidedObject instanceof Barrier) {
							if (ball.isOverwhelming() || !ball.isOverwhelming() && !(((Barrier) collidedObject).isFrozen())) {
								if (collidedObject instanceof SimpleBarrier || collidedObject instanceof PurpleBarrier ) {
									removeAnimationObject((AnimationObject) collidedObject);
									brokenBarriers.add((Barrier) collidedObject);
								} else if (collidedObject instanceof ReinforcedBarrier) {
									int NumberOfHitsNeeded = ((ReinforcedBarrier) collidedObject).getHitCount();
									if (NumberOfHitsNeeded == 1) {
										removeAnimationObject((AnimationObject) collidedObject);
										brokenBarriers.add((Barrier) collidedObject);
									} else {
										((ReinforcedBarrier) collidedObject).decreaseHitCount();
										break;
									}

								} else if (collidedObject instanceof ExplosiveBarrier) {
									ExplosiveBarrier explosive = (ExplosiveBarrier) collidedObject;
									removeAnimationObject(explosive);
									brokenBarriers.add((Barrier) collidedObject);
									
									BarrierGrid barrierGrid = ((Barrier) collidedObject).getParentGrid();
									Barrier[][] barrierArray = barrierGrid.getBarrierArray();
									int gridX = explosive.getGridPositionX();
									int gridY = explosive.getGridPositionY();
									for (int x = Math.max(0, gridX - 1); x <= Math.min(gridX + 1,
											barrierGrid.COL_NUMBER - 1); x++) {
										for (int y = Math.max(0, gridY - 1); y <= Math.min(gridY + 1,
												barrierGrid.ROW_NUMBER - 1); y++) {
											if (x == gridX && y == gridY) {
												continue; //skip it self already removed
											}
											Barrier neighbor = barrierArray[y][x];
											if (neighbor != null) {
												removeAnimationObject(neighbor);
												brokenBarriers.add((Barrier) collidedObject);
											}
										}
									}

								} else if (collidedObject instanceof RewardingBarrier) {
									brokenBarriers.add((Barrier) collidedObject);
									removeAnimationObject((AnimationObject) collidedObject);
								}
							}

						}
					}
					
					// Spell creation and score increase
					
					for (Barrier barrier : brokenBarriers) {
						if (! (barrier instanceof PurpleBarrier)) {
							increaseScoreAfterDestroyingBarrier();
						}
						if (barrier instanceof RewardingBarrier) {
							Spell newSpell = spellFactory.createRandomSpellForBarriers(barrier);
							addAnimationObject(newSpell);
							spellDepot.addSpell(newSpell);
						}
					}
					
					if (ballSolidCollision.getCollidedObjects().contains(lowerWall)) {
						ball.reset();
						staff.reset();
						game.getPlayer().decrementChances();
						pause();
						if (checkGameOver()) {gameOver();}
						break;
					}
					
					// Magical staff - spell collision
					
					HashSet<Spell> spellsToBeRemoved = new HashSet<>();
					
					
					for (Spell spell : spellDepot.getSpellMap().keySet()) {
						spellCollisionInfo = collisionCalculator.checkCollision(spell, Set.of(staff));
						
						if (spellCollisionInfo.getCollidedObjects().size() != 0) {
							removeAnimationObject(spell);
							spellsToBeRemoved.add(spell);

							final int spellDurationShort = 15000;
							final int spellDurationLong = 30000;
							
							switch (spell.getType()) {
							case Spell.HEX: // lasts for 30 seconds
								//TODO
								break;
								
							case Spell.FELIX_FELICIS:
								game.getPlayer().incrementChances();
								break;
								
							case Spell.MAGICAL_STAFF_EXPANSION: // lasts for 30 seconds
								staff.setLength(staff.getLength() * 2);
								if (staff.getNextPosition(dTime).x <= 15) {
									staff.setPlacement(Vector.of(15, MagicalStaff.MS_HORIZON), staff.getRotation());
								} else if (staff.getPosition().x >= 985 - staff.getLength()) {
									staff.setPlacement(Vector.of(985 - staff.getLength(), MagicalStaff.MS_HORIZON), staff.getRotation());
								}
								break;
								
							case Spell.OVERWHELMING_FIREBALL: // lasts for 30 seconds
								// TODO remove print debug

								System.out.println("Overwhelming fireball spell started");
								ball.setOverwhelming(true);

								new Timer().schedule(new java.util.TimerTask() {
									@Override
									public void run() {
										if (ball.isOverwhelming()) {
											System.out.println("Overwhelming fireball spell ended");
											ball.setOverwhelming(false);
										}
									}
								}, spellDurationLong);

								break;
								
							case Spell.INFINITE_VOID: // lasts for 15 seconds

								LinkedList<Barrier> barriers = (LinkedList<Barrier>) barrierGrid.getBarrierList().clone();
								List<Barrier> barriersLeft = new ArrayList <> (barriers.stream()
										.filter(x -> !x.isFrozen() && x.getType() != "purple")
										.toList());
								Collections.shuffle(barriersLeft);
								for (int i = 0; i < Math.min(8, barriersLeft.size()); i++) {
									barriersLeft.get(i).setFrozen(true);
								}

								break;
								
							case Spell.HOLLOW_PURPLE:

								LinkedList<Barrier> barr = (LinkedList<Barrier>) barrierGrid.getBarrierList().clone();
								List<Barrier> barrLeft = new ArrayList <> (barr.stream()
										.filter(x -> !x.isFrozen() && x.getType() != "purple")
										.toList());
								Collections.shuffle(barrLeft);
								for (int i = 0; i < Math.min(8, barrLeft.size()); i++) {
									barrierGrid.changeBarrier(barrLeft.get(i), barrierGrid);
								}
								initializeAnimationObjects();

								break;
							case Spell.DOUBLE_ACCEL: // lasts for 15 seconds

								ball.setVelocity(ball.getVelocity().scale((float) 10));
								ball.setSpedUp(true);

								new Timer().schedule(new java.util.TimerTask() {
									@Override
									public void run() {
										if (ball.isSpedUp()) {
											ball.setVelocity(ball.getVelocity().scale((float) 0.1));
											ball.setSpedUp(false);
										}
									}
								}, spellDurationShort);

								break;
							default:
								break;
							}
						}
						
						spell.move(dTime);
					}
					
					for (Spell spell : spellsToBeRemoved) {
						spellDepot.popSpell(spell);
					}

					// staff movement
					
					if (!staffRotatesLeft && staffRotatesRight) {
						staff.setAngularVelocity(180);// D throws right
					} else if (!staffRotatesRight && staffRotatesLeft) {
						staff.setAngularVelocity(-180);// A//throws left
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

					if (staff.getNextPosition(dTime).x < 985 - staff.getLength()
							&& staff.getNextPosition(dTime).x > 15) {
						staff.move(dTime);
					}

				}
			}
		});

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
		for (Barrier barrier : barrierGrid.getBarrierList()) {
			addAnimationObject(barrier);
			if (barrier.getType().equals("destroyed")) {
				removeAnimationObject(barrier);
			}
		}
		addAnimationObject(leftWall);
		addAnimationObject(rightWall);
		addAnimationObject(upperWall);
		addAnimationObject(lowerWall);

	}

	public void deleteBarrierAt(Vector position) throws InvalidBarrierNumberException {
		Barrier b = barrierGrid.deleteBarrierAt(position);
		if (b != null) {
			removeAnimationObject(b);
		}
	}

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
		if (movable instanceof Barrier) {
			totalBarriers--;
			Barrier barrier = (Barrier) movable;
			barrier.setType("destroyed");
			if (checkGameOver()) {gameOver();}
		}
		animationObjects.remove(movable);
	}

	public CopyOnWriteArraySet<AnimationObject> getAnimationObjects() {
		return animationObjects;
	}

	private float getPassedTime() {
		long currTime = System.currentTimeMillis();
		return (float) (currTime - startTimeMilli) / 1000;
	}
	
	private void increaseScoreAfterDestroyingBarrier() {
		int oldScore = game.getPlayer().getScore();
		int newScore = (int) (oldScore + 300 / getPassedTime());
		game.getPlayer().setScore(newScore);
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

	public long getStartTimeMilli() {
		return startTimeMilli;
	}

	public void setStartTimeMilli(long startTimeMilli) {
		this.startTimeMilli = startTimeMilli;
	}

	public FireBall getFireball() {
		return ball;
	}

	public void setFireball(FireBall ball) {
		this.ball = ball;
	}

	public MagicalStaff getStaff() {
		return staff;
	}

	public void setStaff(MagicalStaff staff) {
		this.staff = staff;
	}

	public boolean checkGameOver() {
		if (getBarrierGrid().totalBarrierNumber == 0 || this.game.getPlayer().getChances() == 0) {//totalBarriers
			g.openMainMenuScreen();	
			return true;
		}
		return false;
	}

	public void gameOver() {
		System.out.println("Game over"); // TODO REMOVe
		this.game.writeHighScore(this.game.getPlayer().getScore());
		
	}; // TODO game over stuff
}
