package domain.animation.barriers;

import domain.animation.BarrierGrid;
import domain.animation.collision.SpellFactory;
import domain.animation.spells.Spell;

public class RewardingBarrier extends Barrier{
	private static final long serialVersionUID = 2665711170614117331L;
	
	static SpellFactory spellFactory = SpellFactory.getInstance();
	Spell barrierSpell;
	
	public RewardingBarrier(BarrierGrid grid, int gridPositionX, int gridPositionY) {
		super(grid, gridPositionX, gridPositionY, "gift");
		this.barrierSpell = spellFactory.createRandomSpellForBarriers(this);
	}
	
	public RewardingBarrier(BarrierGrid grid) {
		super(grid);
	}

	public RewardingBarrier(BarrierGrid grid, String type) {
		super(grid, type);
	}
}

