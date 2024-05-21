package test;

import domain.Game;
import domain.Player;
import domain.animation.AnimationObject;
import domain.animation.MagicalStaff;
import domain.animation.Vector;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
//REQUİRES: dTimeMilisecond must be a non-negative float.
//velocity, position,angularVelocity properly initialized, not null.
//MODİFİES: the position and rotationAngle of the object.
//EFFECTS: calculates the displacement with respect to the velocity and time elapsed.
//calculates the change in roattion based on the angularvelocity and time elapsed.
//updates the object's position and rotation.
//If dTimeMilisecond is 0, the position and rotationAngle remain unchanged.
//BLACKBOX: for dTimeMilisecond = 100 position and rotation should be updated correctly same for dTimeMilisecond = 0(expect 0 disp&rotation) and dTimeMilisecond = 50,
//GALSSBOX: dTimeMilisecond = 1000, velocity = (0, 0), angularVelocity = 1(should only change the rotation position remains the same)
//dTimeMilisecond = 1000, velocity = (10, 10), angularVelocity = 0(only position changes rotation remains the same)
public class MoveTest extends Game {
	private AnimationObject obj = new MagicalStaff();
	Vector v = new Vector(0, 0);

	@Test
	public void testUpdatePositionAndRotation_dTimeMilisecond_100() {
		Vector v = new Vector(0, 0);
		obj.setPlacement(v, 0);
		obj.setAngularVelocity(1); 
		obj.setVelocity(new Vector(10, 10)); 
		obj.move(100);//0.1 second
		assertEquals(1.0, obj.getPosition().getX(), 0.0001);
		assertEquals(1.0, obj.getPosition().getY(), 0.0001);
		assertEquals(0.1, obj.getRotation(), 0.0001);//shpuld be 0.1 radians after 0.1 seconds
	}

	@Test
	public void testUpdatePositionAndRotation_dTimeMilisecond_0() {
		Vector v = new Vector(0, 0);
		obj.setPlacement(v, 0);
		obj.setAngularVelocity(1);
		obj.setVelocity(new Vector(10, 10));
		obj.move(0); //no time change
		assertEquals(0.0, obj.getPosition().getX(), 0.0001);
		assertEquals(0.0, obj.getPosition().getY(), 0.0001);
		assertEquals(0.0, obj.getRotation(), 0.0001);// should be no change in anything
	}

	@Test
	public void testUpdatePositionAndRotation_dTimeMilisecond_50() {
		Vector v = new Vector(0, 0);
		obj.setPlacement(v, 0);
		obj.setAngularVelocity(1);
		obj.setVelocity(new Vector(10, 10));
		obj.move(50); //0.05 second
		assertEquals(0.5, obj.getPosition().getX(), 0.0001);
		assertEquals(0.5, obj.getPosition().getY(), 0.0001);
		assertEquals(0.05, obj.getRotation(), 0.0001); //shpuld be 0.05 radians after 0.05 seconds
	}

	@Test
	public void testUpdatePositionAndRotation_dTimeMilisecond_1000_velocity_0_angularVelocity_1() {
		Vector v = new Vector(0, 0);
		obj.setPlacement(v, 0);
		obj.setAngularVelocity(1);
		obj.setVelocity(new Vector(0, 0));//zero velocity test
		obj.move(1000);
		assertEquals(0.0, obj.getPosition().getX(), 0.0001);
		assertEquals(0.0, obj.getPosition().getY(), 0.0001);
		assertEquals(1.0, obj.getRotation(), 0.0001);//should rotate 1 radian after 1 second keeping velocity unchanged
	}

	@Test
	public void testUpdatePositionAndRotation_dTimeMilisecond_1000_velocity_10_angularVelocity_0() {
		Vector v = new Vector(0, 0);
		obj.setPlacement(v, 0);
		obj.setAngularVelocity(0); //zero angular velocity test
		obj.setVelocity(new Vector(10, 10));

		obj.move(1000);//1 second
		assertEquals(10.0, obj.getPosition().getX(), 0.0001);
		assertEquals(10.0, obj.getPosition().getY(), 0.0001);
		assertEquals(0.0, obj.getRotation(), 0.0001);//zero rotation case
	}

}
