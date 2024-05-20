package test.domain.animation.barrierGridTest;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import domain.animation.barriers.Barrier;
import domain.animation.BarrierGrid;

public class BarrierGridImportTest {
    private BarrierGrid barrierGrid;

    @Before
    public void setUp() throws Exception {
        barrierGrid = new BarrierGrid(
            BarrierGrid.MIN_SIMPLE_BARRIERS,
            BarrierGrid.MIN_FIRM_BARRIERS,
            BarrierGrid.MIN_EXPLOSIVE_BARRIERS,
            BarrierGrid.MIN_GIFT_BARRIERS
        );
    }

    @Test
    public void testValidBarrierString() {
    	
    	
    	
    	
        String barrierString = "simple firm explosive gift";
        barrierGrid.importBarrierGrid(barrierString);
        assertEquals(4, barrierGrid.getBarrierList().size());
        assertEquals(4, countNonNullBarriers(barrierGrid.getBarrierArray())); 
    }
    private int countNonNullBarriers(Barrier[][] barrierArray) {
        int count = 0;
        for (Barrier[] row : barrierArray) {
            for (Barrier barrier : row) {
                if (barrier != null) {
                    count++;
                }}}
        return count;
    }}


