package test.domain.animation.barrierGridTest;

import static org.junit.Assert.*;

import org.junit.Test;

import domain.animation.BarrierGrid;
import exceptions.InvalidBarrierNumberException;

public class BarrierGridConstructorTest {

	@Test
	public void noBarriers() {
		try {
			new BarrierGrid(0, 0, 0, 0);
			
			assert false : "No exeptions thrown for invalid number of barriers";
		} catch (InvalidBarrierNumberException e) {
			return;
		}
	}
	
	@Test
	public void invalidSimpleBarriers() {
		try {
			new BarrierGrid(BarrierGrid.MIN_SIMPLE_BARRIERS - 1, 
					BarrierGrid.MIN_FIRM_BARRIERS, 
					BarrierGrid.MIN_EXPLOSIVE_BARRIERS, 
					BarrierGrid.MIN_GIFT_BARRIERS);

			assert false : "No exeptions thrown for invalid number of simple barriers";
		} catch (InvalidBarrierNumberException e) {
			return;
		}
	}
	
	@Test
	public void invalidFirmBarriers() {
		try {
			new BarrierGrid(BarrierGrid.MIN_SIMPLE_BARRIERS, 
					BarrierGrid.MIN_FIRM_BARRIERS - 1, 
					BarrierGrid.MIN_EXPLOSIVE_BARRIERS, 
					BarrierGrid.MIN_GIFT_BARRIERS);

			assert false : "No exeptions thrown for invalid number of firm barriers";
		} catch (InvalidBarrierNumberException e) {
			return;
		}
	}
	
	@Test
	public void invalidExplosiveBarriers() {
		try {
			new BarrierGrid(BarrierGrid.MIN_SIMPLE_BARRIERS, 
					BarrierGrid.MIN_FIRM_BARRIERS, 
					BarrierGrid.MIN_EXPLOSIVE_BARRIERS - 1, 
					BarrierGrid.MIN_GIFT_BARRIERS);

			assert false : "No exeptions thrown for invalid number of explosive barriers";
		} catch (InvalidBarrierNumberException e) {
			return;
		}
	}
	
	@Test
	public void invalidRewardingBarriers() {
		try {
			new BarrierGrid(BarrierGrid.MIN_SIMPLE_BARRIERS, 
					BarrierGrid.MIN_FIRM_BARRIERS, 
					BarrierGrid.MIN_EXPLOSIVE_BARRIERS, 
					BarrierGrid.MIN_GIFT_BARRIERS - 1);

			assert false : "No exeptions thrown for invalid number of rewarding barriers";
		} catch (InvalidBarrierNumberException e) {
			return;
		}
	}

}
