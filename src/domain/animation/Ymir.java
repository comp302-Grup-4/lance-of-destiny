package domain.animation;

import java.security.SecureRandom;

import domain.animation.spells.Spell;
import domain.animation.spells.SpellFactory;

public class Ymir implements YmirSubject {
	private YmirObserver observer;
	 
	public Ymir() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					try {
						Thread.sleep(30);
						int spellType[]= {Spell.INFINITE_VOID, Spell.DOUBLE_ACCEL, Spell.HOLLOW_PURPLE};
						SecureRandom rand = new SecureRandom();
						int rand_int = rand.nextInt(spellType.length);
						Spell s = SpellFactory.getInstance().createSpell(spellType[rand_int]);
						notifyObserver(observer,s);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		});
		
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

}
