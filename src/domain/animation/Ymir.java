package domain.animation;

import java.security.SecureRandom;

import domain.animation.spells.Spell;
import domain.animation.spells.SpellFactory;

public class Ymir extends Thread implements YmirSubject {
	
	private YmirObserver observer;
	private int lastTwoSpells[] = {0,0};
	 
	public Ymir() {
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		SecureRandom rand = new SecureRandom();
		while (true) {
			try {
				Thread.sleep(30000);
				if(rand.nextFloat() < 0.5) {
					int spellType[] = {Spell.INFINITE_VOID, Spell.DOUBLE_ACCEL, Spell.HOLLOW_PURPLE};
					int rand_int = rand.nextInt(spellType.length);
					
					while(spellType[rand_int] == lastTwoSpells[1]) {
						if(spellType[rand_int] == lastTwoSpells[0]) {
							rand_int = rand.nextInt(spellType.length);
						}
						else {
							break;
						}
					}
					lastTwoSpells[0] = lastTwoSpells[1];
					lastTwoSpells[1] = spellType[rand_int];

					Spell s = SpellFactory.getInstance().createSpell(spellType[rand_int]);
					notifyObserver(observer,s);
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void registerObserver(YmirObserver observer) {
		// TODO Auto-generated method stub
		this.observer = observer;
	}

	@Override
	public void removeObserver(YmirObserver observer) {
		// TODO Auto-generated method stub
		this.observer = null;
	}

	@Override
	public void notifyObserver(YmirObserver observer, Spell s) {
		// TODO Auto-generated method stub
		observer.update(s);
	}

	public int[] getLastTwoSpells() {
		return lastTwoSpells;
	}

	public void setLastTwoSpells(int[] lastTwoSpells) {
		this.lastTwoSpells = lastTwoSpells;
	}
}
