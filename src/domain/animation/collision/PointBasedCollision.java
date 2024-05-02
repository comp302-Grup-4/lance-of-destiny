package domain.animation.collision;

import java.util.Collection;

import domain.animation.Collidable;
import domain.animation.Vector;

public class PointBasedCollision implements CollisionStrategy {
	@Override
	public CollisionInfo checkCollision(Collidable object, Collection<Collidable> otherObjects) {
		CollisionInfo info = new CollisionInfo();
		for (Vector collisionPoint : object.getBoundaryPoints()) {
			for (Collidable collidable : otherObjects) {
				if (collidable.isCollidable() && collidable.contains(collisionPoint)) {
					info.addCollidedObject(collidable);
					info.addToPreviousVelocity(object.getCenterPoint().subtract(collisionPoint));
				}
			}
		}
		
		return info;
	}
	
}
