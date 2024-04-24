package ui.playview;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import domain.animation.AnimationObject;
import domain.animation.Animator;
import domain.animation.Vector;

public class AnimatorAdapter implements AnimationInterface{
	private Animator animator;
	private Dimension windowSize;
	private Vector parentPosition;
	
	public AnimatorAdapter(Animator animator, Dimension windowSize, Vector parentPosition) {
		this.animator = animator;
		this.windowSize = windowSize;
		this.parentPosition = parentPosition;
	}
	
	@Override
	public HashMap<Integer, ObjectSpatialInfo> getObjectSpatialInfoList() {
		
		CopyOnWriteArraySet<AnimationObject> movables = (CopyOnWriteArraySet<AnimationObject>) animator.getAnimationObjects();
		return movables.stream().collect(Collectors.toMap(x -> x.getObjectID(),
												                     x -> {
																		try {
																			return new ObjectSpatialInfo(x, windowSize);
																		} catch (Exception e) {
																			e.printStackTrace();
																		}
																		return null;
																	},  
												   					(prev, next) -> next, 
												   					HashMap::new));
	}

	@Override
	public boolean setBarrierPosition(Vector position) {
		Vector cvtPosition = new Vector((position.getX() - parentPosition.getX()) * windowSize.getWidth() / 1000,
										(position.getY() - parentPosition.getY()) * windowSize.getHeight() / 800);
		animator.getBarrierGrid().
		return false;
	}
}
