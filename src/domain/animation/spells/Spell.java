package domain.animation.spells;

import java.util.HashMap;
import java.util.HashSet;

import domain.Game;
import domain.animation.AnimationObject;
import domain.animation.Vector;

public abstract class Spell extends AnimationObject {
	private static final long serialVersionUID = 2882205343222279632L;
	
	public static final int FELIX_FELICIS = 0;
	public static final int HEX = 1;
	public static final int MAGICAL_STAFF_EXPANSION = 2;
	public static final int OVERWHELMING_FIREBALL = 3;
	public static final int INFINITE_VOID = 4;
	public static final int DOUBLE_ACCEL = 5;
	public static final int HOLLOW_PURPLE = 6;
	public static final int REMAINS = 7;
	
	protected static final int spellDurationShort = 15000;
	protected static final int spellDurationLong = 30000;

	protected int spellType;
	
	static HashSet<String> activatedSpellsSet = new HashSet<>();
	
	public Spell(Vector position) {
		this.position = position;
		this.sizeX = 15;
		this.sizeY = 15;
		
		this.setVelocity(Vector.of(0, 200));
		
		initializeCenterPoint();
		initializeBoundaryPoints();
	}
	
	public abstract void activate(Game game);
	
	public abstract int getType();
	
	protected void setActivated(boolean isActivated) {
		if (isActivated) {
			activatedSpellsSet.add(this.getClass().getName());
		} else {
			activatedSpellsSet.remove(this.getClass().getName());
		}
	}
	
	public boolean isActivated() {
		if (activatedSpellsSet.contains(this.getClass().getName())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public float getRotation() {
		return 0;
	}

	@Override
	public boolean isCollidable() {
		return false;
	}

	@Override
	public Vector[] getBoundaryPoints() {
		return boundaryPoints;
	}
	
	@Override
	public Vector getCenterPoint() {
		return center;
	}
	
	@Override
	public void initializeCenterPoint() {
		center = new Vector(position.getX() + sizeX / 2, position.getY() + sizeY / 2);
	}

	public void setType(int type) {
		this.spellType = type;
	}
}
