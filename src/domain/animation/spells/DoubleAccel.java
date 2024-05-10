package domain.animation.spells;

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
	public void startSpell() {
		FireBall ball = new FireBall();
		ball.setVelocity(ball.getVelocity().scale((float) 0.5));
		
	}

	@Override
	public void stopSpell() {
		// TODO Auto-generated method stub
		
	}
}