package domain.animation;

import java.io.Serializable;

public class FireBall extends AnimationObject implements Serializable {
	private static final long serialVersionUID = 6744117051981779488L;
	protected int RADIUS = 8;
	private boolean isOverwhelming = false;
	private boolean spedUp = false;
	
	public FireBall() {
		this(492, 600);
	}
	
	public FireBall(int posX, int posY) {
		super();
		position = new Vector(posX, posY);
		velocity = Vector.fromDegrees(90).scale(300);
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
	public Vector[] initializeBoundaryPoints() {
		return initializeBoundaryPoints(10);
	}

	public Vector[] initializeBoundaryPoints(int precisionAngle) {
		int numPoints = 360 / precisionAngle;
		boundaryPoints = new Vector[numPoints];
		
		for (int i = 0; i < numPoints; i++) {
			boundaryPoints[i] = Vector.fromDegrees(- i * precisionAngle, RADIUS).add(center);
		}
		
		return boundaryPoints;
	}

	@Override
	public void initializeCenterPoint() {
		this.center = new Vector(this.position.x + RADIUS, this.position.y + RADIUS);
	}
	
	public void reset() {
		this.setPlacement(Vector.of(492, 600), 0);
		this.setVelocity(Vector.fromDegrees(90).scale(300));
	}
	
	public boolean isOverwhelming() {
		return isOverwhelming;
	}
	
	public void setOverwhelming(boolean isOverwhelming) {
		this.isOverwhelming = isOverwhelming;
	}

	public void setSpedUp(boolean spedUp) {
		this.spedUp = spedUp;
	}

	public boolean isSpedUp() {
		return spedUp;
	}

}
