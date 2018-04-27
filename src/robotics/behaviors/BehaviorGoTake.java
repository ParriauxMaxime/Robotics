package robotics.behaviors;

import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.Navigator;
import robotics.AbstractBehaviorRobot;

public class BehaviorGoTake extends SmartBehavior {
	static final  int WEIRD_CONSTANT = 1, MIN_DISTANCE = 5, MAX_DISTANCE = 45;
	Navigator navigator;
	LineMap mapping;
	int distance;
	
	public BehaviorGoTake(AbstractBehaviorRobot robot) {
		super(robot);
	}

	@Override
	public final boolean takeControl() {
		this.distance = this.robot.irAdapter.getObjectDistance();
		return  this.distance <= MAX_DISTANCE && this.distance >= MIN_DISTANCE && this.robot.isObjectGrabbed() == false;
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

	@Override
	public final void action() {
		super.action();
		if (this.robot.getClampState() == false) {
			this.robot.setClampState(true);
			this.robot.clamp.rotate(3 * 360, false);
		}
		int distance = this.robot.irAdapter.getObjectDistance();
		this.distance = distance;
		//this.robot.navigator.rotateTo(this.fineTune());
		while (distance > MIN_DISTANCE && distance <= this.distance && distance < MAX_DISTANCE) {
			this.robot.pilot.travel(distance / 2);
			distance = this.robot.irAdapter.getObjectDistance();
		}
		if (distance > this.distance) {
			this.robot.setObjectFound(false);
		}
		this.suppress();
	}
	
	@Override
	public final void suppress() {
		super.suppress();
		this.robot.pilot.stop();
	}
}
