package domain.animation;

public class FireBall implements Movable {
	private Vector position;
	private Vector velocity;
	
	public FireBall() {
		this(500, 950);
	}
	
	public FireBall(int posX, int posY) {
		position = new Vector(posX, posY);
		velocity = Vector.fromDegrees(90).scale(10);
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
	
	@Override
	public boolean isCollidable() {
		return true;
	}

}
