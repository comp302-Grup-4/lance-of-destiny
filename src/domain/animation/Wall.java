package domain.animation;

public class Wall extends AnimationObject {
	public static int HORIZONTAL = 0;
	public static int VERTICAL = 1;
	
	public static int THICKNESS = 30;
	
	private int orientation;
	
	public Wall(int orientation, Vector position) {
		this.orientation = orientation;
		this.position = position;
		velocity = Vector.zero();
		
		if (orientation == 0) {
			sizeX = 1000;
			sizeY = THICKNESS;
		} else {
			sizeX = THICKNESS;
			sizeY = 800;
		}

		initializeCenterPoint();
		initializeBoundaryPoints();
	}
	
	@Override
	public float getRotation() {
		return 0;
	}

	@Override
	public boolean isCollidable() {
		return true;
	}
	
	public int getOrientation() {
		return orientation;
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
		center = new Vector(position.x + sizeX / 2, position.y + sizeY / 2);
	}

}
