package domain.animation.barriers;

import domain.animation.AnimationObject;
import domain.animation.BarrierGrid;
import domain.animation.Vector;

public abstract class Barrier extends AnimationObject {
	public static final String SIMPLE_BARRIER = "simple";
	public static final String FIRM_BARRIER = "firm";
	public static final String EXPLOSIVE_BARRIER = "explosive";
	public static final String GIFT_BARRIER = "gift";
	
	protected BarrierGrid parentGrid;
	
	private int gridPositionX;
	private int gridPositionY;
		
	public Barrier(BarrierGrid grid) {
		this(grid, -1, -1);
	}
	
	public Barrier(BarrierGrid grid, int gridPositionX, int gridPositionY) {
		super();
		this.gridPositionX = gridPositionX;
		this.gridPositionY = gridPositionY;
		
		velocity = new Vector(0, 0);
		
		isCollidable = true;
		parentGrid = grid;
	}
	
	@Override
	public boolean isCollidable() {
		return isCollidable;
	}
	
	@Override
	public float getRotation() {
		return 0;
	}
	
	public void destroy() {
		isCollidable = false;
	}
	
	public void setGridPosition(int gridPositionX, int gridPositionY) {
		this.gridPositionX = gridPositionX;
		this.gridPositionY = gridPositionY;
	}
	
	public int getGridPositionX() {
		return gridPositionX;
	}
	
	public int getGridPositionY() {
		return gridPositionY;
	}
}
