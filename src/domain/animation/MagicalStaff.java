package domain.animation;

public class MagicalStaff implements Movable {
	private Vector position;
	private Vector velocity;
	public static int MS_HORIZON = 950; // MS will not move up and down 
	
	public MagicalStaff() {
		position = new Vector(500, MS_HORIZON);
		velocity = new Vector(0, 0);
	}
	
	@Override
	public Vector getNextPosition(float dtime) {
		position = position.add(velocity.scale(dtime));
		return position;
	}

	@Override
	public Vector move(float dtime) {
		this.position = getNextPosition(dtime);
		return this.position;
	}

	@Override
	public void setVelocity(Vector newVelocity) {
		this.velocity = newVelocity;
	}
	
	@Override
	public boolean isCollidable() {
		return true;
	}

}
