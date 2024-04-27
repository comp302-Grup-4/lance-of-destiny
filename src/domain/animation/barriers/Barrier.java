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
		this.position = new Vector(0, 0);
		this.gridPositionX = gridPositionX;
		this.gridPositionY = gridPositionY;
		
		velocity = new Vector(0, 0);
		
		isCollidable = true;
		parentGrid = grid;
		
		sizeX = 20;
		sizeY = 20;
		
		initializeCenterPoint();
		initializeBoundaryPoints();
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
	
	@Override
	public void initializeBoundaryPoints() {
		boundaryPoints = new Vector[4];
		boundaryPoints[0] = new Vector(position.getX(), position.getY());
		boundaryPoints[1] = new Vector(position.getX() + sizeX, position.getY());
		boundaryPoints[2] = new Vector(position.getX() + sizeX, position.getY() + sizeY);
		boundaryPoints[3] = new Vector(position.getX(), position.getY() + sizeY);
	}
	
	@Override
	public void initializeCenterPoint() {
		center = new Vector(position.getX() + sizeX / 2, position.getY() + sizeY / 2);
	}
	
	public abstract String getName();
}
