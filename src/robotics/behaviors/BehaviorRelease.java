package robotics.behaviors;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.*;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.subsumption.Behavior;
import robotics.AbstractBehaviorRobot;

public class BehaviorRelease extends SmartBehavior {
	public BehaviorRelease(AbstractBehaviorRobot robot) {
		super(robot);
	}

	@Override
	public boolean takeControl() {
		return this.robot.colorAdapter.getColorID() == 13 &&
				this.robot.isObjectGrabbed() == true;				
	}

	@Override
	public void action() {
		super.action();
		int angle = 3 * 360;
		this.robot.clamp.rotate(angle, false);
		this.robot.pilot.travel(-10);
		this.robot.pilot.rotate(180);
		this.robot.setClampState(true);
		this.robot.setObjectGrabbed(false);
		this.robot.setObjectFound(false);
		this.robot.initClamp();
		this.robot.navigator.clearPath();
		this.suppress();
	}
}
