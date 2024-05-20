package domain.animation;

import java.io.Serializable;

public abstract class AnimationObject implements Movable, Collidable, Serializable {
	private static int objectIDCounter = 0;
	protected int objectID;
	protected Vector position = Vector.zero();
	protected Vector velocity; // pixel per millisecond
	protected boolean isCollidable;
	protected float sizeX;
	protected float sizeY;
	protected Vector[] boundaryPoints;
	protected Vector center;
	protected float rotationAngle = 0;
	protected float angularVelocity = 0;
	

	public AnimationObject() {
		objectID = objectIDCounter++;
	}
	
	@Override
	public float getRotation() {
		return rotationAngle;
	}
	
	@Override
	public float getAngularVelocity() {
		return angularVelocity;
	}
	
	@Override
	public float getNextRotation(float dtime) {
		return rotationAngle + angularVelocity * dtime / 1000;
	}

	public Vector getPosition() {
		return position;
	}

	protected void setPlacement(Vector newPosition, float newRotation) {
		Vector disp = newPosition.subtract(this.position);
		float dRot = newRotation - this.rotationAngle;

		updateCenterPoint(disp);
		updateBoundaryPoints(disp, dRot);
		
		this.position = newPosition;
		this.rotationAngle = newRotation;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public float getSpeed() {
		return getVelocity().length();
	}

	public void setVelocity(Vector velocity) {
		this.velocity = velocity;
	}
	
	protected void setAngularVelocity(float angularVelocity) {
		this.angularVelocity = angularVelocity;
	}

	public int getObjectID() {
		return objectID;
	}

	@Override
	public Vector getNextPosition(float dTimeMilisecond) {
		return position.add(velocity.scale(dTimeMilisecond / 1000));
	}

	private Vector rotateCalculation(Vector point, float dRot) {
		double radians = Math.toRadians(dRot);
		float sin = (float) Math.sin(radians);
		float cos = (float) Math.cos(radians);

		float x = point.x - center.x;
		float y = point.y - center.y;
		float newX = x * cos - y * sin;
		float newY = x * sin + y * cos;
		newX += center.x;
		newY += center.y;
		return new Vector(newX, newY);
	}
	
	@Override
	public void move(float dTimeMilisecond) {
		/**
		 * Calculates the linear displacement by linear velocity,
		 * the rotational change by rotational velocity.
		 * 
		 * Updates the position and rotation.
		 * 
		 */
		Vector disp = new Vector(velocity.x * dTimeMilisecond / 1000, velocity.y * dTimeMilisecond / 1000);
		float dRot = angularVelocity * dTimeMilisecond / 1000;
		
		setPlacement(position.add(disp), rotationAngle + dRot);
	}

	public void setSize(float sizeX, float sizeY) {
		float expansionX = sizeX / this.sizeX;
		float expansionY = sizeY / this.sizeY;
		this.position = Vector.of(center.x - sizeX / 2, center.y - sizeY / 2);
		updateCenterPoint(Vector.zero());
		updateBoundaryPoints(Vector.zero(), expansionX, expansionY);
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}

	public float getSizeX() {
		return sizeX;
	}

	public float getSizeY() {
		return sizeY;
	}

	protected Vector[] initializeBoundaryPoints() {
		boundaryPoints = new Vector[4];
		boundaryPoints[0] = new Vector(position.getX(), position.getY());
		boundaryPoints[1] = new Vector(position.getX() + sizeX, position.getY());
		boundaryPoints[2] = new Vector(position.getX() + sizeX, position.getY() + sizeY);
		boundaryPoints[3] = new Vector(position.getX(), position.getY() + sizeY);
		updateBoundaryPoints(rotationAngle);
		return boundaryPoints;
	}

	protected abstract void initializeCenterPoint();

	protected void updateBoundaryPoints(float dRot) {
		updateBoundaryPoints(Vector.zero(), dRot, 1, 1);
	}
	
	protected void updateBoundaryPoints(Vector displacement) {
		updateBoundaryPoints(displacement, rotationAngle, 1, 1);
	}
	
	protected void updateBoundaryPoints(Vector displacement, float dRot) {
		updateBoundaryPoints(displacement, dRot, 1, 1);
	}
	
	protected void updateBoundaryPoints(Vector displacement, float expansionX, float expansionY) {
		updateBoundaryPoints(displacement, 0, expansionX, expansionY);
	}

	public void updateBoundaryPoints(Vector displacement, float dRot, float expansionX, float expansionY) {
		/**
		 * MODIFIES: boundaryPoints
		 * REQUIRES: boundaryPoints are initialized, center are moved with the amount of displacement
		 * EFFECTS: Updates boundary points in this order: (1) Moves center by the amount of displacement, 
		 * (2) expands the positions of each element of boundaryPoints while center remains still, and 
		 * (3) rotates by the amount of dRot.
		 * 
		 * displacement: the change in the position of the element. 
		 * expansionX: ratio of getting larger in the direction of x
		 * expansionY: ratio of getting larger in the direction of x
		 */	
		
		for (Vector vector : boundaryPoints) {
			vector.setX((vector.getX() - center.x) * expansionX + center.x + displacement.x);
			vector.setY((vector.getY() - center.y) * expansionY + center.y + displacement.y);
		}
		
		for (int i = 0; i < boundaryPoints.length; i++) {
			boundaryPoints[i] = rotateCalculation(boundaryPoints[i], dRot);
		}
	}

	protected void updateCenterPoint(Vector displacement) {
		this.center.setX(this.center.x + displacement.x);
		this.center.setY(this.center.y + displacement.y);
	};
	
	@Override
	public Vector[] getBoundaryPoints() {
		return boundaryPoints;
	}
	
	

	public void setBoundaryPoints(Vector[] boundaryPoints) {
		this.boundaryPoints = boundaryPoints;
	}

	@Override
	public Vector getCenterPoint() {
		return center;
	}

	@Override
	public boolean contains(Vector point) {
		/*
		 * Requires:
		 * 1. boundaryPoints contains 4 points.
		 * 2. points in boundaryPoints array is not null.
		 * 3. points in boundaryPoints array is given in counterclockwise order.
		 * 4. the shape of AnimationObject is not concave.
		 * 
		 * Effects:
		 * Returns true if the given point inside of the AnimationObject, false otherwise.
		 * 
		 */
		if (boundaryPoints == null || boundaryPoints.length != 4) {
            throw new IllegalArgumentException("Invalid Shape: Boundary points array must contain exactly 4 non-null points.");
        }
		
		for(Vector p : boundaryPoints) {
			if(p==null) {
				throw new IllegalArgumentException("Invalid Shape: Boundary points array must contain exactly 4 non-null points.");
			}
		}
		
		for (int i = 0; i < boundaryPoints.length; i++) {
			Vector collisionPoint = boundaryPoints[i];
			Vector nextCollisionPoint = boundaryPoints[(i + 1) % boundaryPoints.length];

			Vector boundaryLine = nextCollisionPoint.subtract(collisionPoint);
			float crossProdVal = boundaryLine.getY() * (point.getX() - collisionPoint.getX()) 
					- boundaryLine.getX() * (point.getY() - collisionPoint.getY());

			if (crossProdVal > 0)
				return false;
		}
		return true;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AnimationObject) {
			return ((AnimationObject) obj).getObjectID() == this.objectID;
		} else {
			return false;
		}
	}
}
