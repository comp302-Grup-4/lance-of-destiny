package domain.animation.barriers;

import domain.animation.BarrierGrid;

public class ExplosiveBarrier extends Barrier {
	boolean isExploded;
		
	public ExplosiveBarrier(BarrierGrid grid, int gridPositionX, int gridPositionY) {
		super(grid, gridPositionX, gridPositionY);
		isExploded = false;
	}

	public ExplosiveBarrier(BarrierGrid grid) {
		super(grid);
		isExploded = false;
	}

	public void explode() {
		isExploded = true;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		
		return "explosive";
	}
}

