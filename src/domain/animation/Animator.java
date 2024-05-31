package domain.animation;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.ImageIcon;

import domain.Game;
import domain.MultiplayerGame;
import domain.animation.barriers.*;
import domain.animation.collision.*;
import domain.animation.spells.*;
import exceptions.InvalidBarrierNumberException;
import network.Message;
import ui.GameApp;

public class Animator implements Serializable, YmirObserver{
	private static final long serialVersionUID = -3426545588581994135L;
	public static int RIGHT = 1;
	public static int LEFT = -1;
	public static int RROTATE = 1;
	public static int LROTATE = -1;

	private final float FPS = 60;
	private final long dTime = (long) (1000 / FPS);
	private FireBall ball;
	private MagicalStaff staff;
	protected BarrierGrid barrierGrid;
	private Wall rightWall, leftWall, upperWall, lowerWall;
	public CopyOnWriteArraySet<AnimationObject> animationObjects;
	private Thread animationThread;
	private CollisionStrategy collisionCalculator;
	private boolean staffMovesRight = false, staffMovesLeft = false;
	private boolean staffRotatesRight = false, staffRotatesLeft = false;
	private Game game;
	boolean paused = false;
	private long startTimeMilli = 0;
	private SpellDepot spellDepot = new SpellDepot();
	private static final SpellFactory spellFactory = SpellFactory.getInstance();
	public boolean hexActive = false;
	private Ymir ymir;
	private Thread ymirThread;
	
	public Animator(Game game) {
		this.game = game;
		ball = new FireBall();
		staff = new MagicalStaff();
		ymir = new Ymir();
		ymir.registerObserver(this);
	    ymirThread = new Thread(ymir);
		try {
			barrierGrid = new BarrierGrid(BarrierGrid.MIN_SIMPLE_BARRIERS, BarrierGrid.MIN_FIRM_BARRIERS,
					BarrierGrid.MIN_EXPLOSIVE_BARRIERS, BarrierGrid.MIN_GIFT_BARRIERS);
		} catch (InvalidBarrierNumberException e) {
			e.printStackTrace();
		}
		barrierGrid.getTotalBarrierNumber();
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
			if (!(game instanceof MultiplayerGame)) {
				ymirThread.start();
			}
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
						
						forceDirection = ballCollisionInfo.getNextDirection()
										.add(ballSolidCollision.getNextDirection()
										.add(ballFrozenCollision.getNextDirection())).unit();
					} else {
						
						forceDirection = ballSolidCollision.getNextDirection()
								.add(ballFrozenCollision.getNextDirection()).unit();
					}

					if (forceDirection.dot(ball.getVelocity()) > 0) {
						velocityChange = forceDirection.scale(ball.getSpeed() * 0.05f);
					} else {
						velocityChange = forceDirection.scale(-2 * ball.getVelocity().dot(forceDirection));
					}
					
					ball.setVelocity(ball.getVelocity().add(velocityChange));
					
					ball.move(dTime);
					
					HashSet<Barrier> brokenBarriers = new HashSet<>();
					ArrayList<Collidable> ballBarrierCollision = ballCollisionInfo.getCollidedObjects();
					ballBarrierCollision.addAll(ballFrozenCollision.getCollidedObjects());
					
					
					barrierBehavior(ballBarrierCollision, brokenBarriers);
							
					
					// HEX BALLS

					ArrayList<HexFireBall> hexBarriers = (ArrayList<HexFireBall>) animationObjects.stream()
							.filter(x -> x instanceof HexFireBall)
							.map(x -> (HexFireBall) x)
							.collect(Collectors.toList());
					
					hexBarriers.forEach(x -> x.move(dTime));
					
					// Hex Balls Collision
					
					for (AnimationObject obj : hexBarriers) {
						HexFireBall fireBall = (HexFireBall) obj;
						fireBall.move(dTime);

						// Check for collision with barriers
						CollisionInfo fireBallCollisionInfo = collisionCalculator.checkCollision(fireBall, getAnimationObjects().stream()
								.filter(x -> x instanceof Barrier)
								.map(x -> (Collidable) x)
								.toList());

						if (!fireBallCollisionInfo.getCollidedObjects().isEmpty()) {
							// If a collision is detected, remove the HexFireBall and the Barrier
							for (Collidable collidedObject : fireBallCollisionInfo.getCollidedObjects()) {
								if (collidedObject instanceof Barrier) {
									ballBarrierCollision.add(collidedObject);
								}
							}
							barrierBehavior(ballBarrierCollision, brokenBarriers);
							removeAnimationObject(fireBall);
						}
						
					}
					
					// Spell creation and score increase
					
					for (Barrier barrier : brokenBarriers) {
						if (! (barrier instanceof PurpleBarrier)) {
							increaseScoreAfterDestroyingBarrier();
						}
						if (barrier instanceof RewardingBarrier) {
							boolean isMultiplayer = game instanceof MultiplayerGame;
							Spell newSpell = spellFactory.createRandomSpellForBarriers(barrier, isMultiplayer);
							addAnimationObject(newSpell);
							spellDepot.addSpell(newSpell);
						}
						
						if(barrier instanceof ExplosiveBarrier) {
							Spell remains1 = spellFactory.createSpell(Spell.REMAINS, barrier.getCenterPoint());
							Spell remains2 = spellFactory.createSpell(Spell.REMAINS, barrier.getBoundaryPoints()[0]);
							Spell remains3 = spellFactory.createSpell(Spell.REMAINS, barrier.getBoundaryPoints()[1]);
							addAnimationObject(remains1);
							spellDepot.addSpell(remains1);
							addAnimationObject(remains2);
							spellDepot.addSpell(remains2);
							addAnimationObject(remains3);
							spellDepot.addSpell(remains3);
						}
					}
					
					if (ballSolidCollision.getCollidedObjects().contains(lowerWall)) {
						ball.reset();
						staff.reset();
						game.getPlayer().decrementChances();
						pause();
						checkGameOver();
						break;
					}
					
					// Magical staff - spell collision
					
					HashSet<Spell> spellsToBeRemoved = new HashSet<>();
					
					
					for (Spell spell : spellDepot.getSpellMap().keySet()) {
						spellCollisionInfo = collisionCalculator.checkCollision(spell, Set.of(staff));
						
						if (spellCollisionInfo.getCollidedObjects().size() != 0) {
							removeAnimationObject(spell);
							spellsToBeRemoved.add(spell);

							if (!spell.isActivated()) {
								if (spell instanceof InfiniteVoid ||
										spell instanceof HollowPurple ||
										spell instanceof DoubleAccel) {
									if (game instanceof MultiplayerGame) {
										GameApp.getInstance().getActiveServer().update(Message.SPELL, spell.getType());// TODO REFACTOR
									}
								} else {
									if (spell instanceof Hex) {
										System.out.println("Hex exists");
										spellDepot.setHexExists(true);
									} else if (spell instanceof MagicalStaffExpansion) {
										System.out.println("MSE exists");
										spellDepot.setMSEExists(true);
									} else {
										spell.activate(game);
									}
								}
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

	public void initializeAnimationObjects() {
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

	public void addAnimationObject(AnimationObject movable) {
		animationObjects.add(movable);
	}

	public void removeAnimationObject(AnimationObject movable) {
		if (movable instanceof Barrier) {
			Barrier barrier = (Barrier) movable;
			barrier.setType("destroyed");
			checkGameOver();
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
		game.setPlayerScore(newScore);
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
	
	public void barrierBehavior(ArrayList<Collidable> ballBarrierCollision,HashSet<Barrier> brokenBarriers) {
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
									brokenBarriers.add((Barrier) collidedObject);
									removeAnimationObject(neighbor);
									if (neighbor instanceof RewardingBarrier) {
										boolean isMultiplayer = game instanceof MultiplayerGame;
										Spell newSpell = spellFactory.createRandomSpellForBarriers(neighbor, isMultiplayer);
										addAnimationObject(newSpell);
										spellDepot.addSpell(newSpell);
									}
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
		
	}

	private long lastExecutionTime = 0;
	public boolean checkGameOver() {
		ImageIcon loserIcon = new ImageIcon("./res/drawable/loser.png");
		ImageIcon winnerIcon = new ImageIcon("./res/drawable/winner.png");
		long currentTime = 0;
		if (this.game.getPlayer().getChances() == 0) {
			currentTime = System.currentTimeMillis();
			if (currentTime - lastExecutionTime >= 5000) { // once every 5 max to prevent multiple pop ups from spawning
				gameOver();
				lastExecutionTime = currentTime;
				game.endGame("You lost the game :p", loserIcon);
				return true;
			}
		}
		else if (getBarrierGrid().checkAllBarriersDestroyed() ) {
			currentTime = System.currentTimeMillis();
			if (currentTime - lastExecutionTime >= 5000) {
				gameOver();
				lastExecutionTime = currentTime;
				game.endGame("Congrats! You win!", winnerIcon);
				return true;
			}
		}
		return false;
	}

	public void gameOver() {
		System.out.println("Game over");
		System.out.println("Score: " + this.game.getPlayer().getScore());
		this.game.writeHighScore(this.game.getPlayer().getScore());
		
	}; // TODO game over stuff

	@Override
	public void update(Spell s) {
		s.activate(game);
	}
	public Ymir getYmir() {
		return ymir;
	}

	public SpellDepot getSpellDepot() {
		return spellDepot;
	}
}