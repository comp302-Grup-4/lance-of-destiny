package ui.playview;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;

import domain.animation.AnimationObject;
import domain.animation.FireBall;
import domain.animation.MagicalStaff;
import domain.animation.Vector;
import domain.animation.Wall;
import domain.animation.barriers.Barrier;
import domain.animation.barriers.ExplosiveBarrier;
import domain.animation.barriers.ReinforcedBarrier;
import domain.animation.barriers.RewardingBarrier;
import domain.animation.barriers.SimpleBarrier;

public class ObjectSpatialInfo {
	public final ImageIcon magicalStaffImage = new ImageIcon("./res/drawable/largeStaff.png");
	public final ImageIcon firmBarrierImage = new ImageIcon("./res/drawable/smallFirm.png");
	public final ImageIcon explosiveBarrierImage = new ImageIcon("./res/drawable/smallRedGem.png");
	public final ImageIcon rewardingBarrierImage = new ImageIcon("./res/drawable/smallGreenGem.png");
	public final ImageIcon simpleBarrierImage = new ImageIcon("./res/drawable/smallBlueGem.png");
	public final ImageIcon fireBallImage = new ImageIcon("./res/drawable/largeFireball.png");
	public final ImageIcon bgImage = new ImageIcon("./res/drawable/largeBackground.png");
	public final ImageIcon horizontalWall = new ImageIcon("./res/drawable/horizontalWall.png");
	public final ImageIcon verticalWall = new ImageIcon("./res/drawable/verticalWall.png");

	int ID;
	Vector position;
	float rotation;
	ImageIcon image;
	float sizeX;
	float sizeY;
	
	private final float windowSizeXCoeff, windowSizeYCoeff;
	
	public ObjectSpatialInfo(AnimationObject object, Dimension windowSize) throws Exception {
		this.ID = object.getObjectID();
		this.rotation = object.getRotation();
		this.windowSizeXCoeff = (float) windowSize.width / 1000;
		this.windowSizeYCoeff = (float) windowSize.height / 800;
		

		this.position = new Vector(object.getPosition().getX() * windowSizeXCoeff, 
								   object.getPosition().getY() * windowSizeYCoeff);

		
		if (object instanceof Barrier) {
			this.sizeX = object.getSizeX() * windowSizeXCoeff;
			this.sizeY = object.getSizeY();
		} else if (object instanceof MagicalStaff) {
			this.sizeX = object.getSizeX() * windowSizeXCoeff;
			this.sizeY = object.getSizeY();
		} else if (object instanceof FireBall || object instanceof Wall) {
			this.sizeX = object.getSizeX() * windowSizeXCoeff;
			this.sizeY = object.getSizeY() * windowSizeXCoeff;
		} else {
			this.sizeX = object.getSizeX();
			this.sizeY = object.getSizeY();
		}
		
		
		if (object instanceof SimpleBarrier) {
			image = simpleBarrierImage;
		} else if (object instanceof ReinforcedBarrier) {
			image = firmBarrierImage;
		} else if (object instanceof RewardingBarrier) {
			image = rewardingBarrierImage;
		} else if (object instanceof ExplosiveBarrier) {
			image = explosiveBarrierImage;
		} else if (object instanceof MagicalStaff) {
			image = magicalStaffImage;
		} else if (object instanceof FireBall) {
			image = fireBallImage;
		} else if (object instanceof Wall) {
			if (((Wall) object).getOrientation() == Wall.HORIZONTAL) {
				image = horizontalWall;
			} else {
				image = verticalWall;
			}
		} else {
			throw new Exception("Resource not found.");
		}
		image = new ImageIcon(image.getImage().getScaledInstance((int) sizeX, (int) sizeY, Image.SCALE_FAST));

	}
	
	public Vector getPosition() {
		return position;
	}
	
	public void setPosition(Vector position) {
		this.position = position;
	}
	
	public ImageIcon getImage() {
		return image;
	}
	
	public void setImage(ImageIcon image) {
		this.image = image;
	}
	
	public float getRotation() {
		return rotation;
	}
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	public float getSizeX() {
		return sizeX;
	}
	
	public float getSizeY() {
		return sizeY;
	}
}