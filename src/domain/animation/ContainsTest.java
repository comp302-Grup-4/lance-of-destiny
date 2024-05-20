package domain.animation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ContainsTest {
	
	@Test
	// Test1: boundaryPoints of object is = [(0,0), (3,0), (3,3), (0,3)] given point is (1,1), should return true. /inside
    public void testInside() {
        MagicalStaff ms = new MagicalStaff();
        ms.boundaryPoints = new Vector[] {new Vector(0,0), new Vector(3,0), new Vector(3,3), new Vector(0,3)};
        Vector point = new Vector(1, 1);
        assertTrue(ms.contains(point));
    }

    @Test
    // Test2: boundaryPoints of object is = [(0,0), (3,0), (3,3), (0,3)] given point is (3,3), should return true. /edge
    public void testEdge() {
        MagicalStaff ms = new MagicalStaff();
        ms.boundaryPoints = new Vector[] {new Vector(0,0), new Vector(3,0), new Vector(3,3), new Vector(0,3)};
        Vector point = new Vector(3, 3);
        assertTrue(ms.contains(point));
    }

    @Test
    // Test3: boundaryPoints of object is = [(0,0), (3,0), (3,3), (0,3)] given point is (4,4), should return false. /outside
    public void testOutside() {
        MagicalStaff ms = new MagicalStaff();
        ms.boundaryPoints = new Vector[] {new Vector(0,0), new Vector(3,0), new Vector(3,3), new Vector(0,3)};
        Vector point = new Vector(4, 4);
        assertFalse(ms.contains(point));
    }

    @Test
    // Test4: boundaryPoints of object is = [(0,0), (3,0), (0,3)] given point is (1,1), should throw exception. /not 4 points
    public void testInvalidShapeNotFourPoints() {
        MagicalStaff ms = new MagicalStaff();
        ms.boundaryPoints = new Vector[] {new Vector(0,0), new Vector(3,0), new Vector(0,3)};
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            ms.contains(new Vector(1, 1));
        });
        assertEquals("Invalid Shape: Boundary points array must contain exactly 4 non-null points.", thrown.getMessage());
    }
    
    @Test
    // Test5: boundaryPoints of object is = [(0,0), (3,0), (3,3), null] given point is (1,1), should throw exception. /null points
    public void testInvalidShapeNullPoints() {
        MagicalStaff ms = new MagicalStaff();
        ms.boundaryPoints = new Vector[] {new Vector(0,0), new Vector(3,0), new Vector(3,3), null};
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            ms.contains(new Vector(1, 1));
        });
        assertEquals("Invalid Shape: Boundary points array must contain exactly 4 non-null points.", thrown.getMessage());
    }

}
