package domain.animation.barriers;

import domain.animation.BarrierGrid;
import java.io.Serializable;

public class BarrierFactory implements Serializable {
	private static final long serialVersionUID = 4452747295618332851L;

	public Barrier createBarrier(String name, BarrierGrid bg) {
		if(name.equals("simple")) {
			return new SimpleBarrier(bg, "simple");
		}
		else if (name.equals("firm")) {
			return new ReinforcedBarrier(bg, "firm");
		}
		else if (name.equals("explosive")) {
			return new ExplosiveBarrier(bg, "explosive");
		}
		else if (name.equals("gift")) {
			return new RewardingBarrier(bg, "gift");
		}
		else {
			return null;
		}
	}
}
