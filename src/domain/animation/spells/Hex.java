package domain.animation.spells;

import java.util.Timer;
import java.util.TimerTask;

import domain.Game;
import domain.animation.AnimationObject;
import domain.animation.Animator;
import domain.animation.MagicalStaff;
import domain.animation.Vector;

public class Hex extends Spell {
	// HEX info:
	// 2 fireballs are thrown from the staff every second
	// they also decrease performance A LOT on 150 fps
	// thus, I reduced the fps to 60, which is playable
	// also, since there are no hex images provided(?)
	// they use smallPurpleBarrier images, which can be changed
	// in the SpatialObject class
	// also picking up another hex spell doesnt do anything at the moment
	// also hexfireball spawn points are not exact when the staff is rotated
	// TODO? performance improvements
	// TODO? change HexFireBall images
	// TODO reuse spell while active
	
	private static final long serialVersionUID = 6889631466234664321L;

	public Hex(Vector position) {
		super(position);
	}
	
	@Override
	public int getType() {
		return HEX;
	}
	
	@Override
	public void activate(Game game) {
		setActivated(true);
		Animator animator = game.getAnimator();
		MagicalStaff staff = animator.getStaff();
		int timeBetweenShots = 1000; // fires a fireball every 1000 ms
		int max = spellDurationLong / timeBetweenShots;
		new Timer().scheduleAtFixedRate(new TimerTask() {
			int count = 0;
			@Override
			public void run() {
				if (count < max) {
					// Calculate the spawn points based on the rotation of the staff
					HexFireBall fireBall1 = new HexFireBall((int) staff.getPosition().getX(), (int) staff.getPosition().getY());
					HexFireBall fireBall2 = new HexFireBall((int) ((int) staff.getPosition().getX() + staff.getLength()), (int) staff.getPosition().getY());
					fireBall1.setVelocity(Vector.fromDegrees(90 - staff.getRotation()).scale(400));
					fireBall2.setVelocity(Vector.fromDegrees(90 - staff.getRotation()).scale(400));
					animator.addAnimationObject(fireBall1);
					animator.addAnimationObject(fireBall2);
					count++;
				} else {
					for (AnimationObject obj : animator.animationObjects) {
						if (obj instanceof HexFireBall) {
							animator.removeAnimationObject(obj);
						}
					}
					this.cancel();
					setActivated(false);
				}
			}
		}, 0, timeBetweenShots);
	}

}