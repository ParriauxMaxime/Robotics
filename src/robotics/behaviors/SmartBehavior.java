package robotics.behaviors;

import lejos.robotics.subsumption.Behavior;
import robotics.AbstractBehaviorRobot;

abstract public  class SmartBehavior implements Behavior {
	protected AbstractBehaviorRobot robot;
	protected boolean suppressed = false;
	static final  int WEIRD_CONSTANT = 1, MIN_DISTANCE = 6, MAX_DISTANCE = 45;
	
	SmartBehavior(AbstractBehaviorRobot robot) {
		this.robot = robot;
	}
	
	@Override
	abstract public boolean takeControl();

	@Override
	public void action() {
		this.robot.setCurrentBehavior(this.getClass().getName().toString());
		this.suppressed = false;
	}
	
	@Override
	public void suppress() {
		this.suppressed = true;
		this.robot.pilot.stop();
		this.robot.navigator.stop();
		this.robot.setCurrentBehavior(this.getClass().getName().toString() + "-suppressed");
	}
}
