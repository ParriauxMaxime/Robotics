package behaviors;

import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
import robotics.AbstractBehaviorRobot;
import robotics.InfraredAdapter;

public class BehaviorAvoid implements Behavior {
	AbstractBehaviorRobot robot;
	boolean backing_up = false;

	public BehaviorAvoid(AbstractBehaviorRobot robot) {
		this.robot = robot;
	}

	public boolean takeControl() {
		return this.robot.irAdapter.getObjectDistance() < 50;
	}

	public void action() {
		backing_up = true;
		this.robot.leftWheel.rotate(-600, true);
		this.robot.rightWheel.rotate(-600);
		this.robot.leftWheel.rotate(450, true);
		this.robot.rightWheel.rotate(-450);
		backing_up = false;
	}

	public void suppress() {
		while (backing_up)
			Thread.yield();
	}

}
