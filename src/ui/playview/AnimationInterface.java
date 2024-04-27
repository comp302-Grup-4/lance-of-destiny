package ui.playview;

import java.util.HashMap;

import domain.animation.Vector;

public interface AnimationInterface {
	
	public HashMap<Integer, ObjectSpatialInfo> getObjectSpatialInfoList();
	
	//public boolean setBarrierPosition(Vector position);

	Vector convertPosition(Vector position);
	
	
}
