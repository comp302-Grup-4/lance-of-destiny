package domain.animation.spells;

import domain.Game;
import domain.animation.Vector;

public class Remains extends Spell {
	
	private static final long serialVersionUID = -6121496566762178498L;

	public Remains(Vector position) {
		super(position);
		this.setVelocity(new Vector(this.getVelocity().getX()*1.2, this.getVelocity().getY()*1.2));
	}

	@Override
	public int getType() {
		return REMAINS;
	}

	@Override
	public void activate(Game game) {
		game.getPlayer().decrementChances();
	}
}
