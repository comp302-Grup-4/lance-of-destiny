package domain.animation;

public class FireBall extends AnimationObject {
	
	public FireBall() {
		this(500, 950);
	}
	
	public FireBall(int posX, int posY) {
		super();
		position = new Vector(posX, posY);
		velocity = Vector.fromDegrees(90).scale(10);
	}
	
	@Override
	public boolean isCollidable() {
		return true;
	}
	
	@Override
	public float getRotation() {
		return 0;
	}

}
