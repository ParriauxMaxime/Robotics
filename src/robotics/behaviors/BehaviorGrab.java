package robotics.behaviors;

import lejos.hardware.motor.*;
import lejos.hardware.port.*;
import lejos.robotics.subsumption.Behavior;
import robotics.AbstractBehaviorRobot;
import robotics.InfraredAdapter;

public class BehaviorGrab  extends SmartBehavior {
	public BehaviorGrab(AbstractBehaviorRobot robot) {
		super(robot);
	}

	@Override
	public boolean takeControl() {
		return this.robot.irAdapter.getObjectDistance() <= 5 && this.robot.isObjectGrabbed() == false;
	}

	@Override
	public void action() {
		super.action();
		int angle = 360;
		this.robot.pilot.travel(5);
		while (!this.robot.clamp.isStalled()) {			
			this.robot.clamp.rotate(-angle);
		}
		this.robot.setClampState(false);
		this.robot.setObjectGrabbed(true);
		this.suppress();
	}
}
