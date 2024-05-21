package domain.animation.spells;

import java.util.Timer;

import domain.Game;
import domain.animation.FireBall;
import domain.animation.Vector;

public class DoubleAccel extends Spell {
	private static final long serialVersionUID = -1052801607880125401L;

	public DoubleAccel(Vector position) {
		super(position);
	}

	@Override
	public int getType() {
		return DOUBLE_ACCEL;
	}

	@Override
	public void activate(Game game) {
		setActivated(true);
		FireBall ball = game.getAnimator().getFireball();
		ball.setVelocity(ball.getVelocity().scale(.5f));
		ball.setSpedUp(true);

		new Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				if (ball.isSpedUp()) {
					ball.setVelocity(ball.getVelocity().scale(2f));
					ball.setSpedUp(false);
				}
				setActivated(false);
			}
		}, spellDurationShort);
	}
}
