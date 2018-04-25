package behaviors;

import lejos.robotics.subsumption.Behavior;
import robotics.AbstractBehaviorRobot;

abstract public  class AbstractSmartBehavior implements Behavior {
	AbstractBehaviorRobot robot;
	boolean suppressed = false;
	
	AbstractSmartBehavior(AbstractBehaviorRobot robot) {
		this.robot = robot;
	}
	
	@Override
	abstract public boolean takeControl();

	@Override
	abstract public void action();
	@Override
	abstract public void suppress(); 
}
