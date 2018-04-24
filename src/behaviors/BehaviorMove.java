package behaviors;

import lejos.hardware.motor.*;
import lejos.robotics.subsumption.Behavior;

public class BehaviorMove implements Behavior {
	BaseRegulatedMotor left;
	BaseRegulatedMotor right;
	boolean suppressed = false;
	
	public BehaviorMove(BaseRegulatedMotor l, BaseRegulatedMotor r) {
		this.left = l;
		this.right = r;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean takeControl() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action() {
		suppressed = false;
		BaseRegulatedMotor[] syncList = {this.left};
		this.right.synchronizeWith(syncList);
		this.right.startSynchronization();
		this.left.forward();
		this.right.forward();
		this.right.endSynchronization();
		while(!suppressed) Thread.yield();
		this.right.startSynchronization();
		this.left.stop();
		this.right.stop();
		this.right.endSynchronization();
	}

	@Override
	public void suppress() {
		suppressed = true;

	}

}
