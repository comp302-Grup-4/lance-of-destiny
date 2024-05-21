package domain.animation.spells;

import domain.animation.FireBall;

public class HexFireBall extends FireBall {

    private static final long serialVersionUID = 1L;
    
    public HexFireBall(int i, int y) {
        super(i, y);
        RADIUS = 5;
		sizeX = 2 * RADIUS;
		sizeY = 2 * RADIUS;
    }
}
