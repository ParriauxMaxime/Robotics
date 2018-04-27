package robotics.behaviors;

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
		this.robot.leftMotor.rotate(-600, true);
		this.robot.rightMotor.rotate(-600);
		this.robot.leftMotor.rotate(450, true);
		this.robot.rightMotor.rotate(-450);
		backing_up = false;
	}

	public void suppress() {
		while (backing_up)
			Thread.yield();
	}

}
