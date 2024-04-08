package domain.animation;

public class MagicalStaff extends AnimationObject {	
	public static int MS_HORIZON = 950; // MS will not move up and down 
	
	public MagicalStaff() {
		super();
		position = new Vector(500, MS_HORIZON);
		velocity = new Vector(0, 0);
	}
	
	@Override
	public boolean isCollidable() {
		return true;
	}
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	@Override
	public float getRotation() {
		return rotation;
	}

}
