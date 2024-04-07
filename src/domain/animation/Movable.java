package domain.animation;

public interface Movable {
	Vector getNextPosition(float dtime);
	Vector move(float dtime);
	void setVelocity(Vector newVelocity);
}
