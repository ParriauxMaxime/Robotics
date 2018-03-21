package robotics;

import lejos.hardware.lcd.LCD;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class BehaviorAvoid implements Behavior {
	RegulatedMotor leftMotor;
	RegulatedMotor rightMotor;
	InfraredAdapter irAdapter;
	boolean backing_up = false;

	public BehaviorAvoid(RegulatedMotor left, RegulatedMotor right, InfraredAdapter irA) {
		leftMotor = left;
		rightMotor = right;
		irAdapter = irA;
	}

	public boolean takeControl() {
		return irAdapter.getObjectDistance() < 50;
	}

	public void action() {
		backing_up = true;
		leftMotor.rotate(-600, true);
		rightMotor.rotate(-600);
		leftMotor.rotate(450, true);
		rightMotor.rotate(-450);
		backing_up = false;
	}

	public void suppress() {
		while (backing_up)
			Thread.yield();
	}

}
