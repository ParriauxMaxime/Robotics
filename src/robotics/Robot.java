package robotics;
import lejos.hardware.motor.*;
import lejos.robotics.navigation.*;
import lejos.robotics.chassis.*;

public abstract class Robot implements IRobot {
	protected double widthTrack;
	protected BaseRegulatedMotor left;
	protected BaseRegulatedMotor right;
	protected double wheelWidth;
	protected int DEFAULT_SPEED = 360;
	MovePilot pilot;
	Chassis chassis;
	Navigator navigator;
	
	Robot(BaseRegulatedMotor left, BaseRegulatedMotor right, double widthTrack, double wheelWidth) {
		this.left = left;
		this.right = right;
		this.widthTrack = widthTrack;
		this.wheelWidth = wheelWidth;
	}
	
	public void resetTachoCount() {
		this.left.resetTachoCount();
		this.right.resetTachoCount();
	}
	
	public void resetSpeed() {
		this.left.setSpeed(DEFAULT_SPEED);
		this.right.setSpeed(DEFAULT_SPEED);
	}
	
	public void calibrateWheelDiameter() {
		int X = 500;
		BaseRegulatedMotor[] syncList = {this.left};
		this.right.synchronizeWith(syncList);
		this.right.setSpeed(X);
		this.left.setSpeed(X);
		this.right.startSynchronization();
		this.right.forward();
		this.left.forward();
		this.right.endSynchronization();
		try{Thread.sleep(1000);}catch(Exception e){}
		this.right.startSynchronization();
		this.right.stop();
		this.left.stop();
		this.right.endSynchronization();
	}
	
	public void assignementMotor() {
		this.forward(0.4);
		this.turnRight(180, 0.16);
		this.forward(0.4);
		this.turnRight(90, 0);
		this.forward(0.32);
		this.turnRight(90, 0);
	}

	public void assignementMovePilot() {
		this.pilot.travel(400);
		this.pilot.arc(-160, 180, false);
		this.pilot.travel(400);
		this.pilot.arc(0, -90, false);
		this.pilot.travel(320);
		this.pilot.arc(0, -90, false);
	}
	
	public void assignementNavigator() {
		this.navigator.clearPath();
		this.navigator.addWaypoint(new Waypoint(400.0, 0.0));
		this.navigator.addWaypoint(new Waypoint(560.0, -160.0));
		this.navigator.addWaypoint(new Waypoint(400.0, -320.0));
		this.navigator.addWaypoint(new Waypoint(0.0, -320.0));
		this.navigator.addWaypoint(new Waypoint(0.0, 0.0));
		this.navigator.followPath();
		this.navigator.waitForStop();
		while(this.navigator.isMoving()) {};
		this.navigator.rotateTo(0);
	}

	@Override
	public abstract int forward(double distance);

	@Override
	public abstract int turnLeft(int angle, double radiusTurn);

	@Override
	public abstract int turnRight(int angle, double radiusTurn);
}
