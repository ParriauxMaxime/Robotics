package robotics.behaviors;

import lejos.robotics.mapping.LineMap;
import lejos.robotics.navigation.Navigator;
import robotics.AbstractBehaviorRobot;

public class BehaviorGoTake extends SmartBehavior {

	Navigator navigator;
	LineMap mapping;
	int distance;
	
	public BehaviorGoTake(AbstractBehaviorRobot robot) {
		super(robot);
	}

	@Override
	public final boolean takeControl() {
		this.distance = this.robot.irAdapter.getObjectDistance();
		float heading = this.robot.navigator.getPoseProvider().getPose().getHeading();
		return  this.distance <= MAX_DISTANCE && 
				this.distance >= MIN_DISTANCE && 
				this.robot.isObjectGrabbed() == false && 
				heading < 160 && 
				heading > -160 ;
	}
	
	public float fineTune() {
		int initDistance = this.robot.irAdapter.getObjectDistance();
		float initHeading = this.robot.navigator.getPoseProvider().getPose().getHeading();
		float endHeading = initHeading;
		int maxRotation = 5;
		int step = 1;
		int angle = 0;
		while (this.robot.irAdapter.getObjectDistance() < (initDistance + 2) && angle < maxRotation) {
			this.robot.pilot.rotate(-angle);
			angle+=step;
		}
		if (angle < maxRotation) {
			initHeading = this.robot.navigator.getPoseProvider().getPose().getHeading();
		}
		this.robot.pilot.rotate(angle * step);
		angle = 0;
		while (this.robot.irAdapter.getObjectDistance() < (initDistance + 1) && angle < maxRotation) {
			this.robot.pilot.rotate(angle);
			angle+=step;
		}
		if (angle < maxRotation) {
			endHeading = this.robot.navigator.getPoseProvider().getPose().getHeading();
		}
		this.robot.pilot.rotate(-angle * step);
		float wantedHeading = (endHeading - initHeading) / 2;
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
		this.robot.pilot.rotate(this.fineTune());
		while (distance > MIN_DISTANCE && distance <= this.distance && distance < MAX_DISTANCE) {
			this.robot.pilot.travel((distance - 5) / 2);
			distance = this.robot.irAdapter.getObjectDistance();
			if (suppressed) return;
		}
		if (distance > this.distance) {
			this.robot.setObjectFound(false);
		}
		this.suppress();
	}
	}
