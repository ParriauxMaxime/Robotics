package behaviors;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.*;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.subsumption.Behavior;
import robotics.AbstractBehaviorRobot;

public class BehaviorInspectColor  extends AbstractSmartBehavior  {

	public BehaviorInspectColor(AbstractBehaviorRobot robot) {
		super(robot);
	}

	@Override
	public boolean takeControl() {
		//return this.robot.colorSensor.getColorID() == Color.RED &&
			//	this.robot.clamp.getTachoCount() != 0;				
		return Button.DOWN.isDown();
	}

	@Override
	public void action() {
		suppressed = false;
		int angle = 90;
		this.robot.pilot.rotate(angle);
		this.suppress();
	}

	@Override
	public void suppress() {
		suppressed = true;
	}

}
