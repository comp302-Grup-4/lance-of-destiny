package test.domain.animation.barrierGridTest;

import domain.animation.BarrierGrid;

import exceptions.InvalidBarrierNumberException;
import org.junit.Test;

import static org.junit.Assert.*;

public class CellTest {

    //test getCell
    @Test
    public void testGetCellTrue() throws InvalidBarrierNumberException {
        BarrierGrid bg = new BarrierGrid(75, 10, 5, 10);
        assertTrue(bg.getCell(0, 0));
    }

    @Test
    public void testGetCellOutOfBounds() throws InvalidBarrierNumberException {
        BarrierGrid bg = new BarrierGrid(75, 10, 5, 10);
        assertThrows(IndexOutOfBoundsException.class, () -> bg.getCell(100, 100));
    }

    @Test
    public void testClearCellOutOfBounds() throws InvalidBarrierNumberException {
        BarrierGrid bg = new BarrierGrid(75, 10, 5, 10);
        assertThrows(IndexOutOfBoundsException.class, () -> bg.clearCell(100, 100));
    }

    @Test
    public void testClearCell() throws InvalidBarrierNumberException {
        BarrierGrid bg = new BarrierGrid(75, 10, 5, 10);
        bg.clearCell(0, 0);
        assertFalse(bg.getCell(0, 0));
    }

    @Test
    public void testClearCellAlreadyEmpty() throws InvalidBarrierNumberException {
        BarrierGrid bg = new BarrierGrid(75, 10, 5, 10);
        bg.clearCell(0, 1);
        assertFalse(bg.getCell(0, 1));
        bg.clearCell(0, 1);
        assertFalse(bg.getCell(0, 1));
    }
}