package domain.animation;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import domain.animation.barriers.Barrier;
import domain.animation.barriers.BarrierFactory;
import exceptions.InvalidBarrierNumberException;
import exceptions.InvalidBarrierPositionException;

public class BarrierGrid implements Serializable{
	private final int COL_NUMBER = 37;
	private final int ROW_NUMBER = 25;
	
	public static int MIN_SIMPLE_BARRIERS = 75;
	public static int MIN_FIRM_BARRIERS = 10;
	public static int MIN_EXPLOSIVE_BARRIERS = 5;
	public static int MIN_GIFT_BARRIERS = 10;
	
	
	private float MARGIN = (float) 0.15;
	
	int totalBarrierNumber;
	
	private Barrier[][] barrierArray;
	private LinkedList<Barrier> barrierList;
	
	private Vector position;
	
	private BarrierFactory factory;

	public BarrierGrid(int simple, int firm, int explosive, int gift) throws InvalidBarrierNumberException {
		checkBarrierNumberValidity(simple, firm, explosive, gift);
		this.factory = new BarrierFactory();

		totalBarrierNumber = simple + firm + explosive + gift;
	//	ROW_NUMBER = totalBarrierNumber / COL_NUMBER + 1;
		
		position = new Vector(20, 40);
		
		barrierList = createRandomizedBarrierList(simple, firm, explosive, gift);
		barrierArray = createBarrierArray(barrierList);

		barrierListToString(); //TODO REMOVE
	}

	public void importBarrierGrid(String barrierString) {
		this.barrierList = stringToBarrierList(barrierString);
		this.factory = new BarrierFactory();

		position = new Vector(20, 40);

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
				System.out.printf("%s, ", barrierArray[i][j].getType());
				}
				else {
					System.out.print("null, ");
				}
			}
			System.out.println();
			System.out.println();
		}

	}
	
	public boolean changeBarrierPosition(Barrier b,Vector newPosition, Vector initialPosition) throws InvalidBarrierPositionException {
		float cellSize = 20 * (1 + 2 * MARGIN);
		int oldCol = (int)((initialPosition.getX() - 20 * MARGIN) / cellSize);
        int oldRow = (int)((initialPosition.getY( )- 20 * MARGIN) / cellSize);
		
		
        int col = (int)((newPosition.getX() - 20 * MARGIN) / cellSize);
        int row = (int)((newPosition.getY() - 20 * MARGIN) / cellSize);
        System.out.printf("row_num: %d, col_num: %d\n",row,col);

		if(this.getBarrierAt(row,col)==null) {
			
				b.setGridPosition(col, row);
				barrierArray[row][col]=b;
				Vector pos = this.position.add(new Vector(cellSize * (float) col + 20 * MARGIN,
						 cellSize * (float) row + 20 * MARGIN));
				b.setPlacement(pos,0);
				barrierArray[oldRow][oldCol]=null;
				return true;
		
		}
        
		return false;
	}

	public int getWidth() {
		return COL_NUMBER;
	}

	public int getHeight() {
		return ROW_NUMBER;
	}

	public boolean getCell(int x, int y) {
		return barrierArray[x][y] != null;
	}

	public String getBarrierType(int x, int y) {
		if (barrierArray[x][y] != null) {
			return barrierArray[x][y].getType();
		} else {
			return null;
		}
	}

	public void setBarrierType(int x, int y, String type) {
		if (barrierArray[x][y] != null) {
			barrierArray[x][y].setType(type);
		}
	}

	public void clearCell(int x, int y) {
		barrierArray[x][y] = null;
	}

	public String barrierListToString() {
		String str = "";
		for (Barrier b : barrierList) {
			str += b.getType() + " ";
		}
		System.out.println(str);
		return str;
	}

	public LinkedList<Barrier> stringToBarrierList(String str) {
		LinkedList<Barrier> bl = new LinkedList<>();
		String[] barrierTypes = str.split(" ");
		for (String type : barrierTypes) {
			if (type.equals("destroyed")) {
				Barrier b = factory.createBarrier("simple", this);
				b.destroy();
				bl.add(b);
			} else {
				bl.add(factory.createBarrier(type, this));
			}
		}
		return bl;
	}
}
