import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.*;
import domain.animation.*;
import domain.animation.barriers.*;
import exceptions.InvalidBarrierNumberException;
import exceptions.InvalidBarrierPositionException;

class changeBarrierPositionJTest {
	
	private Barrier barrier;
	private BarrierGrid grid;
	private Vector initP;
	private Vector newP;
	
	@BeforeEach
	public void initialize() throws InvalidBarrierNumberException {
		grid = new BarrierGrid(75,10,5,10);
		barrier = BarrierFactory.getInstance().createBarrier("simple", grid);
		initP = new Vector (100,200);
		newP = new Vector (200,300);
		
		//since grid is created randomly, initial position is set manually here
		Vector v = grid.getGridPositionAt(initP);
		grid.getBarrierArray()[(int) v.getX()][(int) v.getY()] = barrier;
		barrier.setGridPosition((int) v.getX(), (int) v.getY());
	
	}
	
	@Test
	void testInvalidInitialPosition() {
		Vector invalidPosition = new Vector(-10, -10); 
		assertThrows(InvalidBarrierPositionException.class, () -> grid.changeBarrierPosition(barrier, newP, invalidPosition));
	}
	
	@Test
	public void testInvalidNewPosition() {
		Vector invalidNewPosition = new Vector (-10,-10);
		assertThrows(InvalidBarrierPositionException.class, () -> grid.changeBarrierPosition(barrier, invalidNewPosition, initP));

	}
	
	@Test
	public void testNullBarrier() {
		assertThrows(NullPointerException.class, () -> grid.changeBarrierPosition(null, newP, initP));
	}
	
	@Test
	public void testOccupiedNewPosition() throws InvalidBarrierPositionException {
		//to make sure newP is occupied, another barrier is manually added to the grid
		Barrier otherB = BarrierFactory.getInstance().createBarrier("simple", grid);
		Vector gridPos = grid.getGridPositionAt(newP);
		grid.getBarrierArray()[(int) gridPos.getX()][(int) gridPos.getY()] = otherB;
		assertFalse(grid.changeBarrierPosition(barrier, newP, initP));
	}
	
	@Test
	public void testMoveToSamePosition() throws InvalidBarrierPositionException {
        assertFalse(grid.changeBarrierPosition(barrier, initP, initP));
	}
	
	@Test
	public void testValidMove() throws InvalidBarrierPositionException {
		Vector oldGridPos = grid.getGridPositionAt(initP);
		Vector gridPos = grid.getGridPositionAt(newP);
		assertTrue(grid.changeBarrierPosition(barrier, newP, initP));
		assertEquals(barrier, grid.getBarrierArray()[(int) gridPos.getX()][(int) gridPos.getY()]);
		assertNull(grid.getBarrierArray()[(int) oldGridPos.getX()][(int) oldGridPos.getY()]);
	}
	
	@Test
	public void testMoveToEdge() throws InvalidBarrierPositionException {
		Vector edgePos = new Vector(grid.getWidth()*20*(1+2*0.15f), grid.getHeight()*20*(1+2*0.15f));
		assertTrue(grid.changeBarrierPosition(barrier, edgePos, initP));
	}
	
	

}
