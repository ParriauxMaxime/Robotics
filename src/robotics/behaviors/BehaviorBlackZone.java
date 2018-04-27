package robotics.behaviors;

import lejos.hardware.lcd.LCD;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
import robotics.AbstractBehaviorRobot;
import lejos.hardware.sensor.EV3ColorSensor;

public class BehaviorBlackZone extends SmartBehavior {
	public BehaviorBlackZone(AbstractBehaviorRobot robot) {
		super(robot);
	}
	
	@Override
	public final boolean takeControl() {
		return this.robot.colorSensor.getColorID() == Color.BLACK;
	}

	@Override
	public final void  action() {
		super.action();
		int angle = (int)Math.round(Math.random() * 40) + 140;
		int reverse  = Math.round(Math.random()) == 0 ? -1 : 1;
		this.robot.pilot.arc(20, reverse * angle);
		this.suppress();
	}
}
