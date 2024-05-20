package test.domain.animation.barrierGridTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import domain.animation.BarrierGrid;
import exceptions.InvalidBarrierNumberException;

public class BarrierTypeTest {
	 //test getsetCell
	 @Test
	    public void testGetSetBarrierType_Simple() throws InvalidBarrierNumberException {
	        BarrierGrid barrier = new BarrierGrid(75,10,5,10);
	        barrier.setBarrierType(0, 0, "simple");//Set to simple
	        String type = barrier.getBarrierType(0, 0);
	        assertEquals("simple", type);//expect to get type simple
	    }

	    @Test
	    public void testGetSetBarrierType_NoBarrier() throws InvalidBarrierNumberException {
	        BarrierGrid barrier = new BarrierGrid(75,10,5,10);
	        barrier.setBarrierType(0, 0, null);//no barrier is placed in (0,0)
	        String type = barrier.getBarrierType(0, 0);
	        assertNull(type);//expecting null since no barrier is placed
	    }

	    @Test
	    public void testGetSetBarrierType_Explosive() throws InvalidBarrierNumberException {
	        BarrierGrid barrier = new BarrierGrid(75,10,5,10);
	        barrier.setBarrierType(0, 0, "explosive");//set to explosive
	        String type = barrier.getBarrierType(0, 0);
	        assertEquals("explosive", type);//expect to get type explosive
	    }

	    @Test
	    public void testGetSetBarrierType_Gift() throws InvalidBarrierNumberException {
	        BarrierGrid barrier = new BarrierGrid(75,10,5,10);
	        barrier.setBarrierType(0, 0, "gift");//set to gift
	        String type = barrier.getBarrierType(0, 0);
	        assertEquals("gift", type); //expect to get type gift
	    }

}
