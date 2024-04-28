package domain.animation.barriers;

import domain.animation.BarrierGrid;

public class ReinforcedBarrier extends Barrier{
	private int hitCount;
	
	public ReinforcedBarrier(BarrierGrid grid) {
		super(grid);
		this.hitCount = 3;
	}
	
	public ReinforcedBarrier(BarrierGrid grid, int hitCount) {
		super(grid);
		this.hitCount = hitCount;
	}

	public ReinforcedBarrier(BarrierGrid grid, String type) {
		super(grid, type);
		this.hitCount = 3;
	}

	public ReinforcedBarrier(BarrierGrid grid, int hitCount, int gridPositionX, int gridPositionY, String type) {
		super(grid, gridPositionX, gridPositionY, type);
		this.hitCount = hitCount;
	}
	
	public void decreaseHitCount() {
		--hitCount;
	}
	
	public int getHitCount() {
		return hitCount;
	}

}

