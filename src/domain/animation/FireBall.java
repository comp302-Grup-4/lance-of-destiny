package domain.animation;

public class FireBall extends AnimationObject {
	
	public FireBall() {
		this(492, 600);
	}
	
	public FireBall(int posX, int posY) {
		super();
		position = new Vector(posX, posY);
		velocity = Vector.fromDegrees(90).scale(400);
		sizeX = 16;
		sizeY = 16;
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
