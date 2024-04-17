package ui.playview;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

import domain.animation.AnimationObject;
import domain.animation.Animator;

public class AnimatorUIConverter {
	private Animator animator;
	private Dimension windowSize;
	
	public AnimatorUIConverter(Animator animator, Dimension windowSize) {
		this.animator = animator;
		this.windowSize = windowSize;
	}
	
	public HashMap<Integer, ObjectSpatialInfo> getObjectSpatialInfoList() {
		
		@SuppressWarnings("unchecked")
		HashSet<AnimationObject> movables = (HashSet<AnimationObject>) animator.getMovableObjects().clone();
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
}
