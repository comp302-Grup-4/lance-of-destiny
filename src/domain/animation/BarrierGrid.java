package domain.animation;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import domain.animation.barriers.Barrier;
import domain.animation.barriers.BarrierFactory;
import domain.animation.barriers.ExplosiveBarrier;
import domain.animation.barriers.ReinforcedBarrier;
import domain.animation.barriers.RewardingBarrier;
import domain.animation.barriers.SimpleBarrier;
import exceptions.InvalidBarrierNumberException;
import exceptions.InvalidBarrierPositionException;

public class BarrierGrid implements Serializable{
	protected final int COL_NUMBER = 37;
	protected final int ROW_NUMBER = 20;
	
	public static int MIN_SIMPLE_BARRIERS = 75;
	public static int MIN_FIRM_BARRIERS = 10;
	public static int MIN_EXPLOSIVE_BARRIERS = 5;
	public static int MIN_GIFT_BARRIERS = 10;
	
	private int simpleBarrierNumber, firmBarriers, explosiveBarriers, giftBarriers;
	
	private float MARGIN = (float) 0.15;
	
	int totalBarrierNumber;
	private Animator animator;
	private Barrier[][] barrierArray;
	private LinkedList<Barrier> barrierList;
	
	private Vector position;
	private float cellSize = 20 * (1 + 2 * MARGIN);
	
	private BarrierFactory factory;

	public BarrierGrid(int simple, int firm, int explosive, int gift) throws InvalidBarrierNumberException {	
		checkBarrierNumberValidity(simple, firm, explosive, gift);
		
		simpleBarrierNumber = simple;
		firmBarriers = firm;
		explosiveBarriers = explosive;
		giftBarriers = gift;
		
		this.factory = new BarrierFactory();
		totalBarrierNumber = simple + firm + explosive + gift;
		
		position = new Vector(20, 40);
		
		barrierList = createRandomizedBarrierList(simple, firm, explosive, gift);
		barrierArray = createBarrierArray(barrierList);
		
	}
	
	private LinkedList<Barrier> createRandomizedBarrierList(int simple, int firm, int explosive, int gift) {
		LinkedList<Barrier> barrierCollection = new LinkedList<>();
		for (int i = 0; i < simple; i++)
			barrierCollection.add(factory.createBarrier("simple", this));
		for (int i = 0; i < firm; i++)
			barrierCollection.add(factory.createBarrier("firm", this));
		for (int i = 0; i < explosive; i++)
			barrierCollection.add(factory.createBarrier("explosive", this));
		for (int i = 0; i < gift; i++)
			barrierCollection.add(factory.createBarrier("gift", this));
		
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
			barrierArray[barrierGridRow][barrierGridColumn] = barrier;
			
			barrier.setPlacement(this.position.add(new Vector(colWidth * (float) barrierGridColumn + 20 * MARGIN,
															 rowHeight * (float) barrierGridRow + 20 * MARGIN)),
					             0);
			i++;
		}
		return barrierArray;
	}
	
	private void checkBarrierNumberValidity(int simple, int firm, int explosive, int gift) throws InvalidBarrierNumberException {
		if (simple + firm + explosive + gift > ROW_NUMBER * COL_NUMBER)
			throw new InvalidBarrierNumberException("total barrierNumber should be <" + ROW_NUMBER * COL_NUMBER);
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
	
	private Barrier getBarrierAt(int x, int y) throws InvalidBarrierPositionException {
		if (x >= ROW_NUMBER || y >= COL_NUMBER || x < 0 || y < 0)
			throw new InvalidBarrierPositionException(x, y, ROW_NUMBER, COL_NUMBER);
		return barrierArray[x][y];
	}
	
	public LinkedList<Barrier> getBarrierList() {
		return barrierList;
	}
	
	public void printBarrierArray(){
		for(int i = 0; i < ROW_NUMBER;i++) {
			for(int j= 0; j< COL_NUMBER;j++) {
				if(barrierArray[i][j]!=null) {
				System.out.printf("%s, ", barrierArray[i][j].getName());
				}
				else {
					System.out.print("null, ");
				}
			}
			System.out.println();
			System.out.println();
		}

	}
	
	private Vector getGridPositionAt(Vector position) {
		/**
		 * position: with respect to animator
		 */
		Vector newPosition;
		newPosition = position.subtract(this.position);
		
        int col = (int) (Math.floor(newPosition.getX() - 20 * MARGIN) / cellSize);
        int row = (int) (Math.floor(newPosition.getY() - 20 * MARGIN) / cellSize);

		return new Vector(row, col);
	}
	
	public boolean changeBarrierPosition(Barrier b, Vector newPosition, Vector initialPosition) throws InvalidBarrierPositionException {
		/**
		 * positions are with respect to animator
		 */
		Vector newGridPos = getGridPositionAt(newPosition);
		Vector initGridPos = getGridPositionAt(initialPosition);
		
		int oldRow = (int) initGridPos.x;
        int oldCol = (int) initGridPos.y;
        
        int row = (int) newGridPos.x;
        int col = (int) newGridPos.y;

		if(this.getBarrierAt(row,col)==null) {
				b.setGridPosition(col, row);
				barrierArray[row][col] = b;
				Vector pos = this.position.add(new Vector(cellSize * (float) col + 20 * MARGIN,
						                                  cellSize * (float) row + 20 * MARGIN));
				b.setPlacement(pos, 0);
				barrierArray[oldRow][oldCol]=null;
				return true;
		
		}
        
		return false;
	}
	
	protected Barrier deleteBarrierAt(Vector position) throws InvalidBarrierNumberException {
		/**
		 * position is with respect to animation
		 */
		Vector gridPos = getGridPositionAt(position);
		Barrier b = null;
		try {
			b = this.getBarrierAt((int) gridPos.x, (int) gridPos.y);
			if (b instanceof SimpleBarrier) {
				checkBarrierNumberValidity(simpleBarrierNumber - 1, firmBarriers, explosiveBarriers, giftBarriers);
				simpleBarrierNumber--; 
			} else if (b instanceof ReinforcedBarrier) {
				checkBarrierNumberValidity(simpleBarrierNumber, firmBarriers - 1, explosiveBarriers, giftBarriers);
				firmBarriers--;
			} else if (b instanceof ExplosiveBarrier) {
				checkBarrierNumberValidity(simpleBarrierNumber, firmBarriers, explosiveBarriers - 1, giftBarriers);
				explosiveBarriers--;
			} else if (b instanceof RewardingBarrier) {
				checkBarrierNumberValidity(simpleBarrierNumber, firmBarriers, explosiveBarriers, giftBarriers - 1);
				giftBarriers--;
			}
			this.barrierList.remove(b);
			barrierArray[(int) gridPos.x][(int) gridPos.y] = null;
		} catch (InvalidBarrierPositionException e) {
			e.printStackTrace();
		}
		return b;
	}
	
	public Vector getPosition() {
		return position;
	}
	
	public Dimension getSize() {
		return new Dimension((int) (COL_NUMBER * 20 * (1 + 2 * MARGIN)), 
				            (int) (ROW_NUMBER * 20 * (1 + 2 * MARGIN)));
	}

	public Barrier[][] getBarrierArray() {
		// TODO Auto-generated method stub
		return barrierArray;
	}
	
}
