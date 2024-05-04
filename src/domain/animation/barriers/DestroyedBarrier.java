package domain.animation.barriers;

import domain.animation.BarrierGrid;

public class DestroyedBarrier extends Barrier{

	public DestroyedBarrier(BarrierGrid grid, int gridPositionX, int gridPositionY) {
		super(grid, gridPositionX, gridPositionY, "destroyed");
	}

	public DestroyedBarrier(BarrierGrid grid) {
        super(grid);

    }
	public DestroyedBarrier(BarrierGrid grid, String type) {
		super(grid, type);
	}

}
