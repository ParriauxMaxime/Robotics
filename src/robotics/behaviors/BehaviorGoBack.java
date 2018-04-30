package robotics.behaviors;

import lejos.hardware.lcd.LCD;
import lejos.robotics.geometry.Point;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import lejos.robotics.subsumption.Behavior;
import robotics.AbstractBehaviorRobot;

public final class BehaviorGoBack extends SmartBehavior {
	Navigator navigator;
	LineMap mapping;
	Point zone;
	float initHeading; 
	int distance;
	Pose pose;
	Path p = null;
	
	public BehaviorGoBack(AbstractBehaviorRobot robot) {
		super(robot);
		this.pose = this.robot.navigator.getPoseProvider().getPose();
		this.initHeading = pose.getHeading();
	}

	@Override
	public final boolean takeControl() {
		return this.robot.isObjectGrabbed();
	}

	@Override
	public final void action() {
		super.action();
		p = this.robot.navigator.getPath();
		this.robot.navigator.clearPath();
		this.robot.navigator.goTo(new Waypoint(this.robot.navigator.getPoseProvider().getPose().getX(), 0));
		this.robot.navigator.goTo(this.robot.dumpPoint);
		while (this.robot.navigator.isMoving()) {
			if (this.suppressed)
				return;
			Thread.yield();
		}
		this.suppress();
	}
}
