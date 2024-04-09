package domain.animation;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import domain.animation.barriers.Barrier;
import domain.animation.barriers.ExplosiveBarrier;
import domain.animation.barriers.ReinforcedBarrier;
import domain.animation.barriers.RewardingBarrier;
import domain.animation.barriers.SimpleBarrier;
import exceptions.InvalidBarrierNumberException;
import exceptions.InvalidBarrierPositionException;

public class BarrierGrid implements Serializable{
	private final int COL_NUMBER = 40;
	private final int ROW_NUMBER;
	
	public static int MIN_SIMPLE_BARRIERS = 75;
	public static int MIN_FIRM_BARRIERS = 10;
	public static int MIN_EXPLOSIVE_BARRIERS = 5;
	public static int MIN_GIFT_BARRIERS = 10;
	
	private float MARGIN = (float) 0.15;
	
	int totalBarrierNumber;
	
	private Barrier[][] barrierArray;
	private LinkedList<Barrier> barrierList;
	
	private Vector position;
	
	public BarrierGrid(int simple, int firm, int explosive, int gift) throws InvalidBarrierNumberException {	
		checkBarrierNumberValidity(simple, firm, explosive, gift);
		
		totalBarrierNumber = simple + firm + explosive + gift;
		ROW_NUMBER = totalBarrierNumber / COL_NUMBER + 1;
		
		position = new Vector(10, 10);
		
		barrierList = createRandomizedBarrierList(simple, firm, explosive, gift);
		barrierArray = createBarrierArray(barrierList);
		
	}
	
	private LinkedList<Barrier> createRandomizedBarrierList(int simple, int firm, int explosive, int gift) {
		LinkedList<Barrier> barrierCollection = new LinkedList<>();
		for (int i = 0; i < simple; i++)
			barrierCollection.add(new SimpleBarrier(this));
		for (int i = 0; i < firm; i++)
			barrierCollection.add(new ReinforcedBarrier(this));
		for (int i = 0; i < explosive; i++)
			barrierCollection.add(new ExplosiveBarrier(this));
		for (int i = 0; i < gift; i++)
			barrierCollection.add(new RewardingBarrier(this));
		
		Collections.shuffle(barrierCollection);
		return barrierCollection;
	}
	
	private Barrier[][] createBarrierArray(Collection<Barrier> barrierCollection) {
		Barrier[][] barrierArray = new Barrier[ROW_NUMBER][COL_NUMBER];
		int i = 0;
		for (Barrier barrier : barrierCollection) {
			int barrierGridColumn = i % COL_NUMBER;
			int barrierGridRow = i / COL_NUMBER;
			float colWidth = 20 * (1 + 2 * MARGIN);
			float rowHeight = 20 * (1 + 2 * MARGIN);
			
			barrier.setGridPosition(barrierGridColumn, barrierGridRow);
			barrierArray[barrierGridRow]
					    [barrierGridColumn] = barrier;
			
			barrier.setPosition(this.position.add(new Vector(colWidth * (float) barrierGridColumn + 20 * MARGIN,
															 rowHeight * (float) barrierGridRow + 20 * MARGIN)));
			barrier.setSize(20, 20);
			i++;
		}
		return barrierArray;
	}
	
	private void checkBarrierNumberValidity(int simple, int firm, int explosive, int gift) throws InvalidBarrierNumberException {
		if (simple < MIN_SIMPLE_BARRIERS) {
			throw new InvalidBarrierNumberException(Barrier.SIMPLE_BARRIER);
		} else if (firm < MIN_FIRM_BARRIERS) {
			throw new InvalidBarrierNumberException(Barrier.FIRM_BARRIER);
		} else if (explosive < MIN_EXPLOSIVE_BARRIERS) {
			throw new InvalidBarrierNumberException(Barrier.EXPLOSIVE_BARRIER);
		} else if (gift < MIN_GIFT_BARRIERS) {
			throw new InvalidBarrierNumberException(Barrier.GIFT_BARRIER);
		}
	}
	
	public Barrier getBarrierAt(int x, int y) throws InvalidBarrierPositionException {
		if (x >= COL_NUMBER || y >= ROW_NUMBER || x < 0 || y < 0)
			throw new InvalidBarrierPositionException(x, y, COL_NUMBER, ROW_NUMBER);
		return barrierArray[x][y];
	}
	
	public LinkedList<Barrier> getBarrierList() {
		return barrierList;
	}
}
