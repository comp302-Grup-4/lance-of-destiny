package ui.playview;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Objects;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
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

public class SpatialObject extends JLabel{
	
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
	private Vector center;
	
	private AnimationObject object;
	
	private final float windowSizeXCoeff, windowSizeYCoeff;
	
	public SpatialObject(AnimationObject object, Dimension windowSize) throws Exception {
		this.object=object;
		this.ID = object.getObjectID();
		this.rotation = object.getRotation();
		this.windowSizeXCoeff = (float) windowSize.width / 1000;
		this.windowSizeYCoeff = (float) windowSize.height / 800;
		this.center = new Vector(object.getCenterPoint().getX() * windowSizeXCoeff, 
				object.getCenterPoint().getY() * windowSizeYCoeff);
				
		if (object instanceof Barrier || object instanceof MagicalStaff) {
			this.sizeX = object.getSizeX() * windowSizeXCoeff;
			this.sizeY = object.getSizeY();
		} else if (object instanceof Wall) {
			this.sizeX = object.getSizeX() * windowSizeXCoeff;
			this.sizeY = object.getSizeY() * windowSizeYCoeff;
		} else if (object instanceof FireBall) {
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
		
		image = getScaledImage();

		this.position = new Vector(center.getX() - image.getIconWidth() / 2, 
                center.getY() - image.getIconHeight() / 2);
				
		updateIconPlacement();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
		
        g2d.translate(centerX, centerY);
        
        AffineTransform transform = AffineTransform.getRotateInstance(Math.toRadians(rotation));
        g2d.transform(transform);

        image.paintIcon(this, g2d, -image.getIconWidth() / 2, -image.getIconHeight() / 2);

        g2d.dispose();
	}
	
	public ImageIcon getScaledImage() {
		ScaleInfo scaleInfo = new ScaleInfo((int) sizeX, (int) sizeY, image, rotation);
		if (cacheScaledImages.containsKey(scaleInfo)) {
			return cacheScaledImages.get(scaleInfo);
		} else {
			ImageIcon icon = new ImageIcon(image.getImage().getScaledInstance((int) sizeX, (int) sizeY, Image.SCALE_SMOOTH));
			cacheScaledImages.put(scaleInfo, icon);
			return icon;
		}
	}
	
	public void setCenter(Vector center) {
		this.center = center;
	}
	
	public Vector getCenter() {
		return center;
	}
	
	public ImageIcon getImage() {
		return image;
	}
	
	private void updateIconPlacement() {
		if (image != null) {
		
			double radians = Math.toRadians(rotation);
			float sin = (float) Math.sin(radians);
			float cos = (float) Math.cos(radians);
			
			int newHeight = (int) (Math.abs(image.getIconWidth() * sin) + Math.abs(image.getIconHeight() * cos));
			int newWidth = (int) (Math.abs(image.getIconWidth() * cos) + Math.abs(image.getIconHeight() * sin));
			this.setBounds((int) center.getX() - newWidth / 2,
                    (int) center.getY() - newHeight / 2,
                    (int) newWidth,
                    (int) newHeight);
		}
	}
	
	@Override
	public void setIcon(Icon icon) {
		super.setIcon(icon);
		updateIconPlacement();
	}
	
	public float getRotation() {
		return rotation;
	}
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
		updateIconPlacement();
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
