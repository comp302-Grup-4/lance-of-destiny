package ui.playview;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Objects;

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
	
	private class ScaleInfo {
		protected float sizeX, sizeY;
		protected ImageIcon image;
		protected float rotation;
		
		public ScaleInfo(float sizeX, float sizeY, ImageIcon image, float rotation) {
			super();
			this.sizeX = sizeX;
			this.sizeY = sizeY;
			this.image = image;
			this.rotation = rotation;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ScaleInfo)
				return sizeX == ((ScaleInfo) obj).sizeX && 
					sizeY == ((ScaleInfo) obj).sizeY &&
					image == ((ScaleInfo) obj).image &&
					rotation == ((ScaleInfo) obj).rotation;
			else
				return false;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(image, sizeX, sizeY, rotation);
		}
	}
	
	public static ImageIcon magicalStaffImage = new ImageIcon("./res/drawable/largeStaff.png");
	public static ImageIcon firmBarrierImage = new ImageIcon("./res/drawable/smallFirm.png");
	public static ImageIcon explosiveBarrierImage = new ImageIcon("./res/drawable/smallRedGem.png");
	public static ImageIcon rewardingBarrierImage = new ImageIcon("./res/drawable/smallGreenGem.png");
	public static ImageIcon simpleBarrierImage = new ImageIcon("./res/drawable/smallBlueGem.png");
	public static ImageIcon fireBallImage = new ImageIcon("./res/drawable/largeFireball.png");
	public static ImageIcon bgImage = new ImageIcon("./res/drawable/largeBackground.png");
	public static ImageIcon horizontalWall = new ImageIcon("./res/drawable/horizontalWall.png");
	public static ImageIcon verticalWall = new ImageIcon("./res/drawable/verticalWall.png");
	private static HashMap<ScaleInfo, ImageIcon> cacheScaledImages = new HashMap<>();
	
	int ID;
	Vector position;
	float rotation;
	ImageIcon image;
	float sizeX;
	float sizeY;
	
	private AnimationObject object;
	
	private final float windowSizeXCoeff, windowSizeYCoeff;
	
	public ObjectSpatialInfo(AnimationObject object, Dimension windowSize) throws Exception {
		this.object=object;
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
		
//		image = new ImageIcon(image.getImage().getScaledInstance((int) sizeX, (int) sizeY, Image.SCALE_FAST));
		image = getScaledImage();
	}
	
	
	public ImageIcon getScaledImage() {
		  ScaleInfo scaleInfo = new ScaleInfo((int) sizeX, (int) sizeY, image, rotation);
		    if (cacheScaledImages.containsKey(scaleInfo)) {
		        return cacheScaledImages.get(scaleInfo);
		    } else {
		        BufferedImage scaledImage = new BufferedImage((int) sizeX, (int) sizeY, BufferedImage.TYPE_INT_ARGB);
		        Graphics2D g2 = scaledImage.createGraphics();
		       // AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(rotation), sizeX / 2.0, sizeY / 2.0);
		        AffineTransform rotate = AffineTransform.getRotateInstance(Math.toRadians(rotation), 
		        		magicalStaffImage.getIconWidth() / 2.0, magicalStaffImage.getIconHeight()/ 2.0);
		       // System.out.println(rotation);
		        g2.setTransform(rotate);
		        g2.drawImage(image.getImage(), 0, 0, (int) sizeX, (int) sizeY, null);
		        g2.dispose();
		        ImageIcon icon = new ImageIcon(scaledImage);
		        cacheScaledImages.put(scaleInfo, icon);
		        return icon;
		    }
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
	
	public AnimationObject getAnimationObject() {
        return object;
       
    }
}
