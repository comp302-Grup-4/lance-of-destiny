package domain.animation.spells;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;

import domain.Game;
import domain.animation.BarrierGrid;
import domain.animation.Vector;
import domain.animation.barriers.Barrier;

public class InfiniteVoid extends Spell {
	private static final long serialVersionUID = -4349142759511666670L;

	public InfiniteVoid(Vector position) {
		super(position);
	}

	@Override
	public int getType() {
		return INFINITE_VOID;
	}

	@Override
	public void activate(Game game) {
		setActivated(true);
		BarrierGrid barrierGrid = game.getAnimator().getBarrierGrid();
		// start spell
		LinkedList<Barrier> barriers = (LinkedList<Barrier>) barrierGrid.getBarrierList().clone();
		List<Barrier> barriersLeft = new ArrayList <> (barriers.stream()
				.filter(x -> !x.isFrozen() && x.getType() != "purple")
				.toList());
		Collections.shuffle(barriersLeft);
		for (int i = 0; i < Math.min(8, barriersLeft.size()); i++) {
			barriersLeft.get(i).setFrozen(true);
		}
		// stop spell
		new Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				for (int i = 0; i < Math.min(8, barriersLeft.size()); i++) {
					barriersLeft.get(i).setFrozen(false);
				}
				setActivated(false);
			}
		}, spellDurationShort);
	}
}
