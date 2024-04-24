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
	public void initializeBoundaryPoints() {
		boundaryPoints = new Vector[4];
		boundaryPoints[0] = new Vector(position.getX(), position.getY());
		boundaryPoints[1] = new Vector(position.getX() + sizeX, position.getY());
		boundaryPoints[2] = new Vector(position.getX() + sizeX, position.getY() + sizeY);
		boundaryPoints[3] = new Vector(position.getX(), position.getY() + sizeY);
	}

	@Override
	public void initializeCenterPoint() {
		center = new Vector(position.x + 50, position.y + 10);
	}
	
	public float getLength() {
		return sizeX;
	}
	
	public void setLength(float newLength) {
		sizeX = newLength;
	}
}
