package domain.animation;

public class FireBall extends AnimationObject {
	private int RADIUS = 8;
	
	public FireBall() {
		this(492, 600);
	}
	
	public FireBall(int posX, int posY) {
		super();
		position = new Vector(posX, posY);
		velocity = Vector.fromDegrees(90).scale(400);
		sizeX = 2 * RADIUS;
		sizeY = 2 * RADIUS;
		
		initializeCenterPoint();
		initializeBoundaryPoints();
	}
	
	@Override
	public boolean isCollidable() {
		return true;
	}
	
	@Override
	public float getRotation() {
		return 0;
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
		initializeCollisionPoints(10);
	}

	public void initializeCollisionPoints(int precisionAngle) {
		int numPoints = 360 / precisionAngle;
		boundaryPoints = new Vector[numPoints];
		
		for (int i = 0; i < numPoints; i++) {
			boundaryPoints[i] = Vector.fromDegrees(- i * precisionAngle, RADIUS).add(center);
		}
	}

	@Override
	public void initializeCenterPoint() {
		this.center = new Vector(this.position.x + RADIUS, this.position.y + RADIUS);
	}
	
	public void reset() {
		this.setPosition(Vector.of(492, 600));
		this.setVelocity(Vector.fromDegrees(90).scale(400));
	}

}
