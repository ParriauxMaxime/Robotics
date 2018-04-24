package behaviors;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.*;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.subsumption.Behavior;
import robotics.AbstractBehaviorRobot;

public class BehaviorRelease implements Behavior {
	AbstractBehaviorRobot robot;
	boolean suppressed = false;

	public BehaviorRelease(AbstractBehaviorRobot robot) {
		this.robot = robot;
	}

	@Override
	public boolean takeControl() {
		return this.robot.colorSensor.getColorID() == Color.RED &&
				this.robot.isObjectGrabbed() == true;				
	}

	@Override
	public void action() {
		suppressed = false;
		int angle = 3 * 360;
		this.robot.clamp.rotate(angle, false);
		this.robot.setClampState(false);
		this.robot.setObjectGrabbed(false);
		this.robot.pilot.travel(-20);
		this.robot.pilot.rotate(180);
		this.robot.initClamp();
		this.suppress();
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}