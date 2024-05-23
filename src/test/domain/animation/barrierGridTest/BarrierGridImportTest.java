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
    //Black Box Tests : 1- checking the input: "simple firm explosive gift" and expecting output barrierList size 4 and 
    //barrierArray contains 4 non-null barriers.
    //2- Checking the input: "" empty and expecting output  barrierList size is 0 and barrierArray contains 0 non-null barriers.
    //Glass Box Test: 1- checking the barriers are parsed correctly by inspecting the barrierList.
    //2-checking the barrierArray contains barriers at the expected grid positions.                                                                      
    //3-checking the string with invalid barrier types and verifying that an empty string or a null string does not cause any exceptions.
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


