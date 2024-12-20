package domain.animation.barriers;

import domain.animation.BarrierGrid;
import domain.animation.spells.SpellFactory;

import java.io.Serializable;

public class BarrierFactory implements Serializable {
	private static final long serialVersionUID = 4452747295618332851L;
	
	private static BarrierFactory instance = null; 

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
		else if (name.equals("purple")) {
			return new PurpleBarrier(bg, "purple");
		}
		else {
			return null;
		}
	}
	
	public static BarrierFactory getInstance() {
		if (instance == null) {
			return new BarrierFactory();
		} else {
			return instance;
		}
	}
}
