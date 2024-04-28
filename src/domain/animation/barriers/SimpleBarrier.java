package domain.animation.barriers;

import domain.animation.BarrierGrid;

public class SimpleBarrier extends Barrier{

	public SimpleBarrier(BarrierGrid grid, int gridPositionX, int gridPositionY) {
		super(grid, gridPositionX, gridPositionY, "simple");
	}

	public SimpleBarrier(BarrierGrid grid) {
        super(grid);

    }
	public SimpleBarrier(BarrierGrid grid, String type) {
		super(grid, type);
	}

}
