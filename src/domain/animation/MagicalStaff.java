package domain.animation;

public class MagicalStaff extends AnimationObject {	
	public static int MS_HORIZON = 650; // MS will not move up and down 
	
	public MagicalStaff() {
		super();
		position = new Vector(450, MS_HORIZON);
		velocity = new Vector(0, 0);
		sizeX = 100;
		sizeY = 20;
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
