package domain.animation.spells;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import domain.Game;
import domain.animation.BarrierGrid;
import domain.animation.Vector;
import domain.animation.barriers.Barrier;

public class HollowPurple extends Spell {
	private static final long serialVersionUID = 938044675247665148L;

	public HollowPurple(Vector position) {
		super(position);
	}

	@Override
	public int getType() {
		return HOLLOW_PURPLE;
	}
	
	@Override
	public void activate(Game game) {
		BarrierGrid barrierGrid = game.getAnimator().getBarrierGrid();
		LinkedList<Barrier> barr = (LinkedList<Barrier>) barrierGrid.getBarrierList().clone();
		List<Barrier> barrLeft = new ArrayList <> (barr.stream()
				.filter(x -> !x.isFrozen() && x.getType() != "purple")
				.toList());
		Collections.shuffle(barrLeft);
		for (int i = 0; i < Math.min(8, barrLeft.size()); i++) {
			barrierGrid.changeBarrier(barrLeft.get(i), barrierGrid);
		}
		game.getAnimator().initializeAnimationObjects();
	}
}
