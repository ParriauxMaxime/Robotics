package robotics.behaviors;

import lejos.hardware.lcd.LCD;
import lejos.robotics.geometry.Point;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.subsumption.Behavior;
import robotics.AbstractBehaviorRobot;

public final class BehaviorGoBack extends SmartBehavior {
	Navigator navigator;
	LineMap mapping;
	Point zone;
	float initHeading; 
	int distance;
	Pose pose;
	
	public BehaviorGoBack(AbstractBehaviorRobot robot) {
		super(robot);
		this.pose = this.robot.navigator.getPoseProvider().getPose();
		this.zone = new Point(pose.getX(), pose.getY());
		this.initHeading = pose.getHeading();
	}

	@Override
	public final boolean takeControl() {
		return this.robot.isObjectGrabbed() && this.zone.distance(pose.getX(), pose.getY()) > 2;
	}

	@Override
	public final void action() {
		super.action();
		this.robot.navigator.goTo(this.zone.x, this.zone.y, 360 - this.initHeading);
		this.suppress();
	}
	
	@Override 
	public final void suppress() {
		this.robot.navigator.stop();
	}
}
