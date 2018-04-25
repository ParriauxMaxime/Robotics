package behaviors;

import lejos.hardware.lcd.LCD;
import lejos.robotics.geometry.Point;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.subsumption.Behavior;
import robotics.AbstractBehaviorRobot;

public class BehaviorGoBack extends AbstractSmartBehavior {
	Navigator navigator;
	LineMap mapping;
	Point zone;
	float initHeading; 
	int distance;
	
	public BehaviorGoBack(AbstractBehaviorRobot robot) {
		super(robot);
		Pose pose = this.robot.navigator.getPoseProvider().getPose();
		this.zone = new Point(pose.getX(), pose.getY());
		this.initHeading = pose.getHeading();
	}

	public boolean takeControl() {
		return this.robot.isObjectGrabbed();
	}

	public void action() {
		suppressed = false;
		this.robot.navigator.goTo(this.zone.x, this.zone.y, this.initHeading);
		this.suppress();
	}

	public void suppress() {
		suppressed = true;
	}

}
