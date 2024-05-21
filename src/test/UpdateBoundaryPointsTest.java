package test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import domain.animation.MagicalStaff;
import domain.animation.Vector;

public class UpdateBoundaryPointsTest {
	
	@Test
	public void noMoveTest() {
		MagicalStaff ms = new MagicalStaff();
		
		Vector center = new Vector(ms.getCenterPoint().getX(), ms.getCenterPoint().getY());
		Vector[] bps = new Vector[ms.getBoundaryPoints().length];
		for (int i = 0; i < bps.length; i++) {
			Vector originalPoint = ms.getBoundaryPoints()[i];
			bps[i] = new Vector(originalPoint.getX(), originalPoint.getY());
		}
		
		ms.updateBoundaryPoints(Vector.of(0,0), 0, 1, 1);
		
		for (int i = 0; i < bps.length; i++) {
			Vector vector = bps[i];
			assert vector.equals(ms.getBoundaryPoints()[i]) : "Not all boundary points are equal.";						
		}
		
		assert ms.getCenterPoint().equals(center) : "Center has moved.";
	}

	@Test
	public void displacementTest() {
		MagicalStaff ms = new MagicalStaff();
		
		Vector center = new Vector(ms.getCenterPoint().getX(), ms.getCenterPoint().getY());
		Vector[] bps = new Vector[ms.getBoundaryPoints().length];
		for (int i = 0; i < bps.length; i++) {
			Vector originalPoint = ms.getBoundaryPoints()[i];
			bps[i] = new Vector(originalPoint.getX(), originalPoint.getY());
		}
		
		float displacementX = -100;
		float displacementY = 100;
		
		ms.getCenterPoint().setX(center.getX() + displacementX);
		ms.getCenterPoint().setY(center.getY() + displacementY);
		
		ms.updateBoundaryPoints(Vector.of(displacementX,0), 0, 1, 1);
		
		ms.updateBoundaryPoints(Vector.of(0,displacementY), 0, 1, 1);
		
		center.setX(center.getX() + displacementX);
		center.setY(center.getY() + displacementY);
		for (int i = 0; i < bps.length; i++) {
			Vector vector = bps[i];
			vector.setX(vector.getX() + displacementX);
			vector.setY(vector.getY() + displacementY);
			assert vector.equals(ms.getBoundaryPoints()[i]) : "Not all boundary points moved correctly.";						
		}
		
		assert ms.getCenterPoint().equals(center) : "Center has moved.";
	}
	
	@Test
	public void rotationTest() {
		MagicalStaff ms = new MagicalStaff();
		
		Vector center = new Vector(ms.getCenterPoint().getX(), ms.getCenterPoint().getY());
		Vector[] bps = new Vector[ms.getBoundaryPoints().length];
		
		for (int i = 0; i < bps.length; i++) {
			Vector originalPoint = ms.getBoundaryPoints()[i];
			bps[i] = new Vector(originalPoint.getX(), originalPoint.getY());
		}
		
		float drotation = 90;
		
		ms.updateBoundaryPoints(Vector.of(0,0), drotation, 1, 1);
		
		for (int i = 0; i < bps.length; i++) {
			Vector vector = bps[i];
			float newY = center.getY() + vector.getX() - center.getX();
			float newX = center.getX() - (vector.getY() - center.getY());
			
			vector.setX(newX);
			vector.setY(newY);
			
			assertEquals(vector, ms.getBoundaryPoints()[i]);
		}
	
		assertEquals(center, ms.getCenterPoint());
	}
	
	@Test
	public void rotationAndDisplacementTest() {
		MagicalStaff ms = new MagicalStaff();
		
		Vector center = new Vector(ms.getCenterPoint().getX(), ms.getCenterPoint().getY());
		Vector[] bps = new Vector[ms.getBoundaryPoints().length];
		
		for (int i = 0; i < bps.length; i++) {
			Vector originalPoint = ms.getBoundaryPoints()[i];
			bps[i] = new Vector(originalPoint.getX(), originalPoint.getY());
		}
		
		float displacementX = 1;
		
		float drotation = 90;
		
		ms.getCenterPoint().setX(center.getX() + displacementX);
		
		ms.updateBoundaryPoints(Vector.of(displacementX,0), drotation, 1, 1);

		center.setX(center.getX() + displacementX);
		for (int i = 0; i < bps.length; i++) {
			Vector vector = bps[i];

			vector.setX(vector.getX() + displacementX);
			
			float newY = center.getY() + vector.getX() - center.getX();
			float newX = center.getX() - (vector.getY() - center.getY());
			
			vector.setX(newX);
			vector.setY(newY);
					
			assertEquals(vector, ms.getBoundaryPoints()[i]);
		}
		
	
		assertEquals(center, ms.getCenterPoint());
	}
	
	@Test
	public void expansionTest() {
		MagicalStaff ms = new MagicalStaff();
		
		Vector center = new Vector(ms.getCenterPoint().getX(), ms.getCenterPoint().getY());
		Vector[] bps = new Vector[ms.getBoundaryPoints().length];
		
		for (int i = 0; i < bps.length; i++) {
			Vector originalPoint = ms.getBoundaryPoints()[i];
			bps[i] = new Vector(originalPoint.getX(), originalPoint.getY());
		}
		
		float expansionX = 0.5f;
		float expansionY = 2;
		
		ms.updateBoundaryPoints(Vector.of(0,0), 0, expansionX, expansionY);
		
		for (int i = 0; i < bps.length; i++) {
			Vector vector = bps[i];
			float newX = center.getX() + (vector.getX() - center.getX()) * expansionX;
			float newY = center.getY() + (vector.getY() - center.getY()) * expansionY;

			vector.setX(newX);
			vector.setY(newY);
			
			assertEquals(vector, ms.getBoundaryPoints()[i]);
		}
	
		assertEquals(center, ms.getCenterPoint());
	}
}
