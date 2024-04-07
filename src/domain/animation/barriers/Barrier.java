package domain.animation.barriers;

import domain.animation.BarrierGrid;
import domain.animation.Movable;
import domain.animation.Vector;

public abstract class Barrier implements Movable {
	public static final String SIMPLE_BARRIER = "simple";
	public static final String FIRM_BARRIER = "firm";
	public static final String EXPLOSIVE_BARRIER = "explosive";
	public static final String GIFT_BARRIER = "gift";
	
	protected BarrierGrid parentGrid;
	protected Vector position;
	protected Vector velocity;
	protected boolean isCollidable;
	
	private int gridPositionX;
	private int gridPositionY;
		
	public Barrier(BarrierGrid grid) {
		this(grid, -1, -1);
	}
	
	public Barrier(BarrierGrid grid, int gridPositionX, int gridPositionY) {
		this.gridPositionX = gridPositionX;
		this.gridPositionY = gridPositionY;
		isCollidable = true;
		parentGrid = grid;
	}
	
	@Override
	public Vector move(float dtime) {
		position = getNextPosition(dtime);
		return position;
	}
	
	@Override
	public void setVelocity(Vector newVelocity) {
		this.velocity = newVelocity;
	}
	
	@Override
	public boolean isColliable() {
		return isCollidable;
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
	
	@Override
	public Vector getNextPosition(float dtime) {
		return position.add(velocity.scale(dtime));
	}
}
