package domain.animation.collision;

import java.util.Collection;

import domain.animation.Collidable;
import domain.animation.Vector;

public class PointBasedCollision implements CollisionStrategy {
	@Override
	public Vector checkCollision(Collidable object, Collection<Collidable> otherObjects) {
		Vector newDirection = Vector.zero();
		for (Vector collisionPoint : object.getBoundaryPoints()) {
			for (Collidable collidable : otherObjects) {
				if (collidable.contains(collisionPoint)) {
					newDirection = newDirection.add(object.getCenterPoint().subtract(collisionPoint));
				}
			}
		}
		return newDirection.unit();
	}
	
}
