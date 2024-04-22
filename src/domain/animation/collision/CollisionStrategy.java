package domain.animation.collision;

import java.util.Collection;
import domain.animation.Collidable;

public interface CollisionStrategy {
	CollisionInfo checkCollision(Collidable object, Collection<Collidable> otherObjects);
}
