package domain.animation.barriers;

import domain.animation.BarrierGrid;
import domain.animation.spells.Spell;

public class RewardingBarrier extends Barrier{

	Spell barrierSpell;
	
	public RewardingBarrier(BarrierGrid grid, int gridPositionX, int gridPositionY) {
		super(grid, gridPositionX, gridPositionY, "gift");
	}

	public RewardingBarrier(BarrierGrid grid) {
		super(grid);
	}

	public RewardingBarrier(BarrierGrid grid, String type) {
		super(grid, type);
	}

}

