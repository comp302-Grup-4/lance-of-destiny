package domain.animation;


import java.io.Serializable;

public class Vector implements Serializable {
	float x, y;
	
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector(double x, double y) {
		this.x = (float) x;
		this.y = (float) y;
	}

	public static Vector zero() {
		return new Vector(0, 0);
	}
	
	public static Vector of(float x, float y) {
		return new Vector(x, y);
	}
	
	
	public static Vector fromDegrees(float x) {
		return fromDegrees(x, 1);
	}
	
	public static Vector fromDegrees(float x, float radius) {
		return new Vector(radius * (float) Math.cos(-Math.toRadians(x)), radius * (float) Math.sin(-Math.toRadians(x)));
	}
	
	public Vector rotate(float x) {
		return new Vector(length() * (float) Math.cos(-Math.toRadians(x)), length() * (float) Math.sin(-Math.toRadians(x)));
	}
	
	public Vector add(Vector v) {
		return new Vector(x + v.getX(), y + v.getY());
	}
	
	public Vector scale(float scalar) {
		/***
		 *  Multiplies the vector with the scalar.
		 */
		return new Vector(scalar * x, scalar * y);
	}
	
	public Vector subtract(Vector v) {
		return new Vector(x - v.getX(), y - v.getY());
	}
	
	public float dot(Vector v) {
		return v.getX() * x + v.getY() * y;
	}
	
	public Vector projectionOn(Vector v) {
		Vector unitV = v.unit();
		return unitV.scale(this.dot(unitV));
	}
	
	public Vector unit() {
		if (this.isZero()) {
			return this;
		} else {
			return this.scale(1 / this.length());
		}
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public boolean isZero() {
		return (Math.abs(x) < 1e-6) && (Math.abs(y) < 1e-6);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vector) {
			return ((Vector) obj).getX() == getX() 
					&& ((Vector) obj).getY() == getY();
		} else {
			return false;
		}
	}
}
