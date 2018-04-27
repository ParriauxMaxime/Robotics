package robotics.behaviors;

import lejos.robotics.subsumption.Behavior;
import robotics.AbstractBehaviorRobot;

abstract public  class SmartBehavior implements Behavior {
	AbstractBehaviorRobot robot;
	boolean suppressed = false;
	
	SmartBehavior(AbstractBehaviorRobot robot) {
		this.robot = robot;
	}
	
	@Override
	abstract public boolean takeControl();

	@Override
	public void action() {
		this.robot.setCurrentBehavior(this.getClass().getName().toString());
		this.suppressed = true;
	}
	
	@Override
	public void suppress() {
		this.robot.setCurrentBehavior("");
		this.suppressed = false;
	}
}
