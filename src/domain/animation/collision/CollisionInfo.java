package domain.animation.collision;

import java.util.ArrayList;

import domain.animation.Collidable;
import domain.animation.Vector;

public class CollisionInfo {
	private ArrayList<Collidable> collidedObjects;
	private Vector nextDirection;
	
	public CollisionInfo() {
		collidedObjects = new ArrayList<>();
		nextDirection = Vector.zero();
	}
	
	public void addToPreviousVelocity(Vector newVelocity) {
		this.nextDirection.setX(nextDirection.getX() + newVelocity.getX());
		this.nextDirection.setY(nextDirection.getY() + newVelocity.getY());
	}
	
	public void setNextDirection(Vector nextVelocity) {
		this.nextDirection = nextVelocity;
	}
	
	public void addCollidedObject(Collidable collidedObject) {
		collidedObjects.add(collidedObject);
	}
	
	public Vector getNextDirection() {
		return nextDirection.unit();
	}
	
	public ArrayList<Collidable> getCollidedObjects() {
		return collidedObjects;
	}
	
}
