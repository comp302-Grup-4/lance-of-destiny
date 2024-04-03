package domain.animation;

import java.util.Iterator;

import exceptions.InvalidBarrierNumberException;

public class BarrierGrid {
	private int COL_NUMBER = 15;
	
	private Barrier[][] barrierPositions;
	
	public BarrierGrid(int simple, int firm, int explosive, int gift) throws InvalidBarrierNumberException {
		checkBarrierNumberValidity(simple, firm, explosive, gift);
		
		int total = simple + firm + explosive + gift;
		int row_number = Math.ceilDiv(total, COL_NUMBER);
		
		barrierPositions = new Barrier[row_number][COL_NUMBER];
	}
	
	private void checkBarrierNumberValidity(int simple, int firm, int explosive, int gift) throws InvalidBarrierNumberException {
		String invalidBarrierType = null;
		if (simple < 75) {
			invalidBarrierType = Barrier.SIMPLE_BARRIER;
		} else if (firm < 10) {
			invalidBarrierType = Barrier.FIRM_BARRIER;
		} else if (explosive < 5) {
			invalidBarrierType = Barrier.EXPLOSIVE_BARRIER;
		} else if (gift < 10) {
			invalidBarrierType = Barrier.GIFT_BARRIER;
		}
		
		if (invalidBarrierType != null)
			throw new InvalidBarrierNumberException(Barrier.SIMPLE_BARRIER);
	}
}
