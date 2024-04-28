package domain.animation.barriers;

import domain.animation.AnimationObject;
import domain.animation.BarrierGrid;
import domain.animation.Vector;

import java.io.Serializable;

public abstract class Barrier extends AnimationObject implements Serializable {
	public static final String SIMPLE_BARRIER = "simple";
	public static final String FIRM_BARRIER = "firm";
	public static final String EXPLOSIVE_BARRIER = "explosive";
	public static final String GIFT_BARRIER = "gift";
	
	protected BarrierGrid parentGrid;

	private String type;
	private int gridPositionX;
	private int gridPositionY;
		
	public Barrier(BarrierGrid grid) {
		this(grid, -1, -1, null);
	}

	public Barrier(BarrierGrid grid, String type) {
		this(grid, -1, -1, type);
	}

	public Barrier(BarrierGrid grid, int gridPositionX, int gridPositionY, String type) {
		super();
		this.position = new Vector(0, 0);
		this.gridPositionX = gridPositionX;
		this.gridPositionY = gridPositionY;
		this.type = type;
		
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
	public void initializeCenterPoint() {
		center = new Vector(position.getX() + sizeX / 2, position.getY() + sizeY / 2);
	}
	
	public String getType() {
		if (isCollidable) {
			return type;
		} else {
			return "destroyed";
		}
	}

	public void setType(String type) {
		this.type = type;
	}

	public BarrierGrid getParentGrid() {
		// TODO Auto-generated method stub
		return parentGrid;
	}
}
