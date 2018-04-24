package behaviors;

import lejos.hardware.lcd.LCD;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
import robotics.AbstractBehaviorRobot;
import lejos.hardware.sensor.EV3ColorSensor;

public class BehaviorBlackZone implements Behavior {
	AbstractBehaviorRobot robot;
	boolean suppressed = false; 

	public BehaviorBlackZone(AbstractBehaviorRobot robot) {
		this.robot = robot;
	}

	public boolean takeControl() {
		return this.robot.colorSensor.getColorID() == Color.BLACK;
	}

	public void action() {
		suppressed = false;
		int angle = (int)Math.round(Math.random() * 40) + 140;
		int reverse  = Math.round(Math.random()) == 0 ? -1 : 1;
		this.robot.pilot.arc(70, reverse * angle);
		this.suppress();
	}

	public void suppress() {
		suppressed = true;
	}

}
