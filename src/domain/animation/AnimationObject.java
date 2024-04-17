package domain.animation;

public abstract class AnimationObject implements Movable, Collidable {
	private static int objectIDCounter = 0;
	protected int objectID;
	protected Vector position;
	protected Vector velocity; // pixel per milisecond
	protected float rotation;
	protected boolean isCollidable;
	protected float sizeX;
	protected float sizeY;
	protected Vector[] boundaryPoints;
	protected Vector center;
	
	public AnimationObject() {
		objectID = objectIDCounter++;
	}
	
	public Vector getPosition() {
		return position;
	}

	public void setPosition(Vector newPosition) {
		Vector disp = newPosition.subtract(this.position);
		updateBoundaryPoints(disp);
		updateCenterPoint(disp);
		this.position = newPosition;
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
	
	public int getObjectID() {
		return objectID;
	}

	@Override
	public Vector getNextPosition(float dTimeMilisecond) {
		return position.add(velocity.scale(dTimeMilisecond / 1000));
	}

	@Override
	public Vector move(float dTimeMilisecond) {
		Vector disp = new Vector(velocity.x * dTimeMilisecond / 1000,
								 velocity.y * dTimeMilisecond / 1000);
		
		this.position.setX(position.x + disp.x);
		this.position.setY(position.y + disp.y);
		
		updateBoundaryPoints(disp);
		updateCenterPoint(disp);
		
		return position;
	}	
	
	public void setSize(float sizeX, float sizeY) {
		float expansionX = sizeX / this.sizeX;
		float expansionY = sizeY / this.sizeY;
		updateBoundaryPoints(Vector.zero(), 
				expansionX, 
				expansionY);
		updateCenterPoint(Vector.zero(), 
				expansionX, 
				expansionY);
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}
	
	public float getSizeX() {
		return sizeX;
	}
	
	public float getSizeY() {
		return sizeY;
	}
	
	public abstract void initializeBoundaryPoints();
	
	public abstract void initializeCenterPoint();
	
	public void updateBoundaryPoints(Vector displacement) {
		updateBoundaryPoints(displacement, 1, 1);
	}
	
	public void updateBoundaryPoints(Vector displacement, float expansionX, float expansionY) {
		/**
		 * displacement: the change in the position of the element.
		 * expansionX: ratio of getting larger in the direction of x
		 * expansionY: ratio of getting larger in the direction of x
		 */
		for (Vector vector : boundaryPoints) {
			vector.setX(vector.getX() + (displacement.getX() * expansionX));
			vector.setY(vector.getY() + (displacement.getY() * expansionY));
		}
	}
	
	public void updateCenterPoint(Vector displacement) {
		updateCenterPoint(displacement, 1, 1);
	};
	
	public void updateCenterPoint(Vector displacement, float expansionX, float expansionY) {
		this.center.setX(this.center.x + (displacement.x * expansionX));
		this.center.setY(this.center.y + (displacement.y * expansionY));
	};
	
	@Override
	public Vector[] getBoundaryPoints() {
		return boundaryPoints;
	}
	
	@Override
	public Vector getCenterPoint() {
		return center;
	}
	
	@Override
	public boolean contains(Vector point) {
		for (int i = 0; i < boundaryPoints.length; i++) {
			Vector collisionPoint = boundaryPoints[i];
			Vector nextCollisionPoint = boundaryPoints[(i + 1) % boundaryPoints.length];
			
			Vector boundaryLine = nextCollisionPoint.subtract(collisionPoint);
			float crossProdVal = boundaryLine.getY() * (point.getX() - collisionPoint.getX()) -
							boundaryLine.getX() * (point.getY() - collisionPoint.getY());
			
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
