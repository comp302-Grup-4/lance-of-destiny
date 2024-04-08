package domain.animation;

public abstract class AnimationObject implements Movable {
	private static int objectIDCounter = 0;
	protected int objectID;
	protected Vector position;
	protected Vector velocity;
	protected float rotation;
	protected boolean isCollidable;
	
	public AnimationObject() {
		objectID = objectIDCounter++;
	}
	
	
	public Vector getPosition() {
		return position;
	}

	public void setPosition(Vector position) {
		this.position = position;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector velocity) {
		this.velocity = velocity;
	}
	
	public int getObjectID() {
		return objectID;
	}

	@Override
	public Vector getNextPosition(float dtime) {
		return position.add(velocity.scale(dtime));
	}

	@Override
	public Vector move(float dtime) {
		this.position = getNextPosition(dtime);
		return this.position;
	}
	
}
