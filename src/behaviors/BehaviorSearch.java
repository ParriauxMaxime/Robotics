package behaviors;

import lejos.hardware.lcd.LCD;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.geometry.*;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
import robotics.AbstractBehaviorRobot;
import robotics.InfraredAdapter;
import lejos.hardware.sensor.EV3ColorSensor;

public class BehaviorSearch extends AbstractSmartBehavior  {
	public Point zone;
	int maxDistance = 45;

	public BehaviorSearch(AbstractBehaviorRobot robot) {
		super(robot);
	}

	public boolean takeControl() {
		return this.robot.isObjectGrabbed() == false && this.robot.getObjectFound() == false; 
	}
	
	public void randomMove() {
		this.robot.pilot.rotate(Math.round(Math.random() * 3) * (Math.round(Math.random()) == 1 ? 1 : -1) * 90);
		this.robot.pilot.travel(50);
	}

	public void action() {
		suppressed = false;
		this.robot.setObjectFound(false);
		int angle = 6;
		int step = 360 / angle;
		for (int i = 0; i < step; i++) {
			this.robot.navigator.rotateTo(this.robot.navigator.getPoseProvider().getPose().getHeading() + angle);
			if (this.robot.irAdapter.getObjectDistance() <= maxDistance) {
				this.robot.setObjectFound(true);
				break;
			}
		}
		if (!this.robot.getObjectFound())
			this.randomMove();
		this.suppress();			
	}

	public void suppress() {
		suppressed = true;
	}

}
