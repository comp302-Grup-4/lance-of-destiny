package domain.animation;


public class Vector {
	float x, y;
	
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public static Vector fromDegrees(float x) {
		return new Vector((float) Math.cos(-Math.toRadians(x)), (float) Math.sin(-Math.toRadians(x)));
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
		return this.scale(1 / this.length());
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
}
