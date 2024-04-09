package domain.animation;

public interface Collidable {
	boolean isCollidable();
	Vector[] getBoundaryPoints();
	Vector getCenterPoint();
	boolean contains(Vector point);
}
