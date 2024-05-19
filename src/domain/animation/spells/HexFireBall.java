package domain.animation.spells;

import domain.animation.FireBall;

public class HexFireBall extends FireBall {

    private static final long serialVersionUID = 1L;
    private int radius = 2;

    public HexFireBall(int i, int y) {
        super(i, y);
    }
}
