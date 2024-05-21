package domain.animation.spells;

import java.util.Timer;

import domain.Game;
import domain.animation.MagicalStaff;
import domain.animation.Vector;

public class MagicalStaffExpansion extends Spell {
	private static final long serialVersionUID = 193880197544713420L;
	private static boolean isActivated;
	
	public MagicalStaffExpansion(Vector position) {
		super(position);
	}

	@Override
	public int getType() {
		return MAGICAL_STAFF_EXPANSION;
	}

	@Override
	public void activate(Game game) {
		setActivated(true);
		MagicalStaff staff = game.getAnimator().getStaff();
		// start spell
		staff.setLength(staff.getLength() * 2);
		if (staff.getPosition().getX() <= 15) {
			staff.setPlacement(Vector.of(15, MagicalStaff.MS_HORIZON), staff.getRotation());
		} else if (staff.getPosition().getX() >= 985 - staff.getLength()) {
			staff.setPlacement(Vector.of(985 - staff.getLength(), MagicalStaff.MS_HORIZON), staff.getRotation());
		}
		// stop spell
		new Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				staff.setLength(staff.getLength() / 2);
				if (staff.getPosition().getX() <= 15) {
					staff.setPlacement(Vector.of(15, MagicalStaff.MS_HORIZON), staff.getRotation());
				} else if (staff.getPosition().getX() >= 985 - staff.getLength()) {
					staff.setPlacement(Vector.of(985 - staff.getLength(), MagicalStaff.MS_HORIZON), staff.getRotation());
				}
				setActivated(false);
			}
		}, spellDurationLong);
	}
}

