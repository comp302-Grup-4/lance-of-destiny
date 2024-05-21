package domain.animation.spells;

import java.util.Timer;

import domain.Game;
import domain.animation.FireBall;
import domain.animation.Vector;

public class OverwhelmingFireball extends Spell {
	private static final long serialVersionUID = 1637097591031136024L;

	public OverwhelmingFireball(Vector position) {
		super(position);
	}
	
	@Override
	public int getType() {
		return OVERWHELMING_FIREBALL;
	}

	@Override
	public void activate(Game game) {
		setActivated(true);
		FireBall ball = game.getAnimator().getFireball();
		ball.setOverwhelming(true);

		new Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				if (ball.isOverwhelming()) {
					ball.setOverwhelming(false);
				}
				setActivated(false);
			}
		}, spellDurationLong);

	}
}

