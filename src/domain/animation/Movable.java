package domain.animation;

public interface Movable {
	Vector getPosition();
	Vector getVelocity();
	float getRotation();
	Vector getNextPosition(float dtime);
	Vector move(float dtime);
	void setVelocity(Vector newVelocity);
	boolean isCollidable();
}
