package domain.animation.barriers;

import domain.animation.BarrierGrid;

public class ExplosiveBarrier extends Barrier {
	boolean isExploded;
		
	public ExplosiveBarrier(BarrierGrid grid, int gridPositionX, int gridPositionY, String type) {
		super(grid, gridPositionX, gridPositionY, type);
		isExploded = false;
	}

	public ExplosiveBarrier(BarrierGrid grid, String type) {
		super(grid, type);
		isExploded = false;
	}

	public ExplosiveBarrier(BarrierGrid grid) {
		super(grid);
		isExploded = false;
	}

	public void explode() {
		isExploded = true;
	}

}

