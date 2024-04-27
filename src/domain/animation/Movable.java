package domain.animation;

public interface Movable {
	Vector getPosition();
	Vector getVelocity();
	float getRotation();
	float getAngularVelocity();
	float getNextRotation(float dtime);
	Vector getNextPosition(float dtime);
	void move(float dtime);
	void setVelocity(Vector newVelocity);
}
