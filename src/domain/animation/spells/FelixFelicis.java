package domain.animation.spells;

import domain.Game;
import domain.animation.Animator;
import domain.animation.Vector;

public class FelixFelicis extends Spell {
	private static final long serialVersionUID = 3645053335123705293L;

	public FelixFelicis(Vector position) {
		super(position);
	}

	@Override
	public int getType() {
		return FELIX_FELICIS;
	}

	@Override
	public void activate(Game game) {
		game.getPlayer().incrementChances();
	}
}

