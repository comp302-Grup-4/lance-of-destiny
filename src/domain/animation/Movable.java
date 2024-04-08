package domain.animation;

public interface Movable {
	Vector getPosition();
	float getRotation();
	Vector getNextPosition(float dtime);
	Vector move(float dtime);
	void setVelocity(Vector newVelocity);
	boolean isCollidable();
}
