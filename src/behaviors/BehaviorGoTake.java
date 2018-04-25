package behaviors;

import lejos.hardware.lcd.LCD;
import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.subsumption.Behavior;
import robotics.AbstractBehaviorRobot;

public class BehaviorGoTake extends AbstractSmartBehavior {
	Navigator navigator;
	LineMap mapping;
	int distance;
	
	public BehaviorGoTake(AbstractBehaviorRobot robot) {
		super(robot);
	}

	public boolean takeControl() {
		this.distance = this.robot.irAdapter.getObjectDistance();
		if ( this.distance <= 45 && this.distance >= 5 && this.robot.isObjectGrabbed() == false) 
			return true;
		return false;
	}
	
	public float fineTune() {
		int initDistance = this.robot.irAdapter.getObjectDistance();
		float initHeading = this.robot.navigator.getPoseProvider().getPose().getHeading();
		float endHeading = initHeading;
		int maxRotation = 10;
		int step = 2;
		int angle = 0;
		while (this.robot.irAdapter.getObjectDistance() < (initDistance + 1) && angle < maxRotation) {
			this.robot.pilot.rotate(-1);
			angle+=step;
		}
		if (angle < maxRotation) {
			initHeading = this.robot.navigator.getPoseProvider().getPose().getHeading();
		}
		this.robot.pilot.rotate(angle);
		angle = 0;
		while (this.robot.irAdapter.getObjectDistance() < (initDistance + 1) && angle < maxRotation) {
			this.robot.pilot.rotate(1);
			angle+=step;
		}
		if (angle < maxRotation) {
			endHeading = this.robot.navigator.getPoseProvider().getPose().getHeading();
		}
		float wantedHeading = (endHeading - initHeading) / 2 + initHeading;
		return wantedHeading;
	}

	public void action() {
		suppressed = false;
		if (this.robot.getClampState() == false) {
			this.robot.setClampState(true);
			this.robot.clamp.rotate(3 * 360, false);
		}
		int distance = this.robot.irAdapter.getObjectDistance();
		this.distance = distance;
		this.robot.navigator.rotateTo(this.fineTune());
		while (distance > 5 && distance <= this.distance) {
			this.robot.pilot.travel(distance / 5);
			distance = this.robot.irAdapter.getObjectDistance();
		}
		if (distance > this.distance) {
			this.robot.setObjectFound(false);
		}
		this.suppress();
	}

	public void suppress() {
		suppressed = true;
	}

}
