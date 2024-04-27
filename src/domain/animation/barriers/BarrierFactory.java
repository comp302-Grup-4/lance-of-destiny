package domain.animation.barriers;

import domain.animation.BarrierGrid;

public class BarrierFactory {
	
	public Barrier createBarrier(String name, BarrierGrid bg) {
		if(name.equals("simple")) {
			return new SimpleBarrier(bg);
		}
		else if (name.equals("firm")) {
			return new ReinforcedBarrier(bg);
		}
		else if (name.equals("explosive")) {
			return new ExplosiveBarrier(bg);
		}
		else if (name.equals("gift")) {
			return new RewardingBarrier(bg);
		}
		else {
			return null;
		}
	}
}
