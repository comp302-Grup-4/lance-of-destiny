package domain.animation;

public class MagicalStaff extends AnimationObject {
	public static int MS_HORIZON = 650; // MS will not move up and down
	
	public MagicalStaff() {
		super();
		position = new Vector(450, MS_HORIZON);
		velocity = new Vector(0, 0);
		sizeX = 100;
		sizeY = 20;
		
		initializeCenterPoint();
		initializeBoundaryPoints();
	}
	
	@Override
	public boolean isCollidable() {
		return true;
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
		center = new Vector(position.x + 50, position.y + 10);
	}
	
	public float getLength() {
		return sizeX;
	}
	
	public void setLength(float newLength) {
		if (newLength < 980) {
			setSize(newLength, sizeY);			
		}
	}
	
	public void reset() {
		this.setPlacement(Vector.of(500 - getLength() / 2, MS_HORIZON), 0);
		this.angularVelocity = 0;
	}
}
