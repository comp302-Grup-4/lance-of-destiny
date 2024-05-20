/**
 * 
 */
package test.domain.animation.barrierGridTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.animation.*;
import domain.animation.barriers.Barrier;
import domain.animation.barriers.SimpleBarrier;
import exceptions.InvalidBarrierNumberException;
import exceptions.InvalidBarrierPositionException;

/**
 * 
 */
class BarrierGridTest {
	private BarrierGrid grid;

	@BeforeEach
	public void setUpBeforeClass() {
		
			try {
				grid = new BarrierGrid(75,10,5,10);
				//assertTrue(grid.repOk());
			} catch (InvalidBarrierNumberException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	@Test
	public void testInvalidBarrierGrid() {
        try {
			new BarrierGrid(50, 10, 5, 10);
		} catch (InvalidBarrierNumberException e) {
			// TODO Auto-generated catch block
			return;
		}
    }
	
	@Test
    public void testBarrierGridWithExceedingCapacity() {
        int capacity = grid.getWidth() * grid.getHeight();
        try {
            new BarrierGrid(capacity, capacity, capacity, capacity);
            fail("Expected an InvalidBarrierNumberException to be thrown");
        } catch (InvalidBarrierNumberException e) {
        	return;
        }
    }
	

	


}
