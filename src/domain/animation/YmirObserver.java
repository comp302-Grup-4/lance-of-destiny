package domain.animation;

import domain.animation.spells.Spell;

public interface YmirObserver {
	void update(Spell s);
}
