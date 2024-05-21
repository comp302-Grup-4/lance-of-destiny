package domain.animation;

import domain.animation.spells.Spell;

public interface YmirSubject {
	void registerObserver(YmirObserver observer);
	void removeObserver(YmirObserver observer);
	void notifyObserver(YmirObserver observer, Spell s);

}
