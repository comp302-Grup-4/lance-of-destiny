package domain.animation.collision;

import java.util.Collection;
import domain.animation.Collidable;
import domain.animation.Vector;

public interface CollisionStrategy {
	Vector checkCollision(Collidable object, Collection<Collidable> otherObjects);
}
