package robotics;
import lejos.hardware.motor.*;
import lejos.robotics.navigation.*;
import lejos.robotics.chassis.*;
import lejos.robotics.pathfinding.*;


public abstract class AbstractRobot implements IRobot {
	protected double widthTrack;
	protected BaseRegulatedMotor leftWheel;
	protected BaseRegulatedMotor rightWheel;
	protected double wheelDiameter;
	protected int DEFAULT_SPEED = 600;
	MovePilot pilot;
	Chassis chassis;
	Navigator navigator;

	
	AbstractRobot(BaseRegulatedMotor left, BaseRegulatedMotor right, double widthTrack, double wheelWidth) {
		this.leftWheel = left;
		this.rightWheel = right;
		this.widthTrack = widthTrack;
		this.wheelDiameter = wheelWidth;
		Wheel R = WheeledChassis.modelWheel(this.rightWheel, this.wheelDiameter * 1000).offset((widthTrack * 1000 / 2));
		Wheel L = WheeledChassis.modelWheel(this.leftWheel, this.wheelDiameter * 1000).offset(-(widthTrack * 1000 / 2));
		this.chassis = new WheeledChassis(new Wheel[] {L, R}, WheeledChassis.TYPE_DIFFERENTIAL);
		this.pilot = new MovePilot(chassis);
		this.navigator = new Navigator(this.pilot);
	}
	
	
	public void resetTachoCount() {
		this.leftWheel.resetTachoCount();
		this.rightWheel.resetTachoCount();
	}
	
	public void resetSpeed() {
		this.leftWheel.setSpeed(DEFAULT_SPEED);
		this.rightWheel.setSpeed(DEFAULT_SPEED);
		this.pilot.setLinearSpeed(DEFAULT_SPEED);
	
	}
	
	public void testCircle() {
		this.turnLeft(360, 0);
	}
	
	public void assignementMotor() {
		this.forward(0.6);
		this.turnRight(180, 0.16);
		this.forward(0.6);
		this.turnRight(90, 0);
		this.forward(0.32);
		this.turnRight(90, 0);
	}

	public void assignementMovePilot() {
		this.pilot.travel(600);
		this.pilot.arc(-160, 180, false);
		this.pilot.travel(600);
		this.pilot.rotate(-90);
		this.pilot.travel(320);
		this.pilot.rotate(-90);
	}
	
	protected Path makeArch(int radius, Waypoint offset, boolean back) {
		Path p = new Path();
		final int nbWaypoint = 4;
		final double PiDivision = (Math.PI / (nbWaypoint * 2));
		for (int i = 1; i <= nbWaypoint; i++) {
			double x;
			if (back == false) 
				x = offset.x + radius * Math.sin(i * PiDivision);
			else 
				x = offset.x - radius * Math.sin(i * PiDivision);
			double y = offset.y + radius * (-Math.cos((nbWaypoint - i) * PiDivision));
			p.add(new Waypoint(x, y));
			}
		return p;
	}
	
	public void assignementNavigator() {
		this.resetSpeed();
		this.navigator.clearPath();
		this.navigator.addWaypoint(new Waypoint(600, 0));
		this.navigator.addWaypoint(new Waypoint(760, -160));
		this.navigator.addWaypoint(new Waypoint(600, - 320));
		this.navigator.addWaypoint(new Waypoint(0, -320));
		this.navigator.addWaypoint(new Waypoint(0, 0));
		this.navigator.followPath();
		while(this.navigator.isMoving()) {};
		this.navigator.rotateTo(0);
	}
	
	public void differentialDrive(int speedL, int speedR, int time) {
		this.resetSpeed();
		this.rightWheel.synchronizeWith(new BaseRegulatedMotor[] {this.leftWheel});
		this.rightWheel.setSpeed(speedR);
		this.leftWheel.setSpeed(speedL);
		this.rightWheel.startSynchronization();
		this.rightWheel.forward();
		this.leftWheel.forward();
		this.rightWheel.endSynchronization();
		try {Thread.sleep(time * 1000);} catch (Exception e) {}
		this.rightWheel.startSynchronization();
		this.leftWheel.stop();
		this.rightWheel.stop();
		this.rightWheel.endSynchronization();
	}

	@Override
	public void forward(double distance) {
		int time = 1;
		double wheelDistance = (this.wheelDiameter * Math.PI);
		double nbTurn = (distance / wheelDistance);
		int angle = (int)(nbTurn * 360);
		int speed = (angle / time);
		while (speed > DEFAULT_SPEED) {
			time++;
			speed = (angle / time);
		}
		this.differentialDrive(speed, speed, time);	
	}

	@Override
	public	void turnLeft(double angle, double radiusTurn) {
		int time = 1;
		int internalSpeed = (int)((radiusTurn * angle) / ((wheelDiameter/2) * time));
		int externalSpeed = (int)(((radiusTurn + widthTrack) * angle) / ((wheelDiameter/2) * time));
		while (externalSpeed > DEFAULT_SPEED || internalSpeed > DEFAULT_SPEED) {
			time++;
			internalSpeed = (int)((radiusTurn * angle) / ((wheelDiameter/2) * time));
			externalSpeed = (int)(((radiusTurn + widthTrack) * angle) / ((wheelDiameter/2) * time));
		}
		this.differentialDrive(externalSpeed, internalSpeed, time);
	}
	
	@Override
	public void turnRight(double angle, double radiusTurn) {
		int time = 1;
		int internalSpeed = (int)((radiusTurn * angle) / ((wheelDiameter/2) * time));
		int externalSpeed = (int)(((radiusTurn + widthTrack) * angle) / ((wheelDiameter/2) * time));
		this.leftWheel.synchronizeWith(new BaseRegulatedMotor[] {this.rightWheel});
		while (externalSpeed > DEFAULT_SPEED || internalSpeed > DEFAULT_SPEED) {
			time++;
			internalSpeed = (int)((radiusTurn * angle) / ((wheelDiameter/2) * time));
			externalSpeed = (int)(((radiusTurn + widthTrack) * angle) / ((wheelDiameter/2) * time));
		}
		
		this.differentialDrive(internalSpeed, externalSpeed, time);
	}

}
