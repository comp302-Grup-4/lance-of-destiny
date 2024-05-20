package test.domain.animation.barrierGridTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.animation.BarrierGrid;
import exceptions.InvalidBarrierNumberException;

class BarrierGridTest {

		private BarrierGrid grid;

		@BeforeEach
		public void setUp(){
			
				try {
					grid = new BarrierGrid(75,10,5,10);
					assertTrue(grid.repOk());
				} catch (InvalidBarrierNumberException e) {
					fail("Setup failed due to InvalidBarrierNumberException");
				}
		}
		
		@Test
		public void testInvalidBarrierGrid() {
	        try {
				grid = new BarrierGrid(50, 10, 5, 10);
				assertFalse(grid.repOk());
			} catch (InvalidBarrierNumberException e) {
				return;
			}
	    }
		
		@Test
	    public void testBarrierGridWithExceedingCapacity() {
	        int capacity = grid.getWidth() * grid.getHeight();
	        try {
	            grid = new BarrierGrid(capacity, capacity, capacity, capacity);
	            assertFalse(grid.repOk());
	        } catch (InvalidBarrierNumberException e) {
	        	return;
	        }
	    }
	}

