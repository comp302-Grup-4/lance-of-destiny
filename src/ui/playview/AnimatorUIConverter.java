package ui.playview;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import domain.animation.AnimationObject;
import domain.animation.Animator;
import domain.animation.Vector;

public class AnimatorUIConverter {
	private Animator animator;
	private Dimension windowSize;
	private final float windowSizeXCoeff, windowSizeYCoeff;
	
	public AnimatorUIConverter(Animator animator, Dimension windowSize) {
		this.animator = animator;
		this.windowSize = windowSize;
		this.windowSizeXCoeff = (float) windowSize.width / 1000;
		this.windowSizeYCoeff = (float) windowSize.height / 800;
	}
	
	public HashMap<Integer, SpatialObject> getObjectSpatialInfoList() {
		
		CopyOnWriteArraySet<AnimationObject> movables = (CopyOnWriteArraySet<AnimationObject>) animator.getAnimationObjects();
		return movables.stream().collect(Collectors.toMap(x -> x.getObjectID(),
												                     x -> {
																		try {
																			return new SpatialObject(x, windowSize);
																		} catch (Exception e) {
																			e.printStackTrace();
																		}
																		return null;
																	},  
												   					(prev, next) -> next, 
												   					HashMap::new));
	}
	
	public Vector convertPositionToAnimator(Vector v) {
		return new Vector(v.getX() / windowSizeXCoeff, v.getY() / windowSizeYCoeff);
	}
}
