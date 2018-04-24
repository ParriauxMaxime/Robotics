package robotics;

import lejos.hardware.motor.*;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.navigation.*;
import lejos.robotics.BaseMotor;
import lejos.robotics.chassis.*;
import lejos.robotics.pathfinding.*;


public abstract class AbstractRobot implements IRobot {
	protected Wheel[] wheels;
	protected double wheelDiameter, widthTrack;
	protected int DEFAULT_SPEED = 600;
	public BaseRegulatedMotor leftWheel, rightWheel;
	public InfraredAdapter irAdapter;
	public EV3ColorSensor colorSensor;
	public BaseRegulatedMotor clamp;
	public MovePilot pilot;
	protected Chassis chassis;
	public	Navigator navigator;
	protected boolean openClamp = false;

	AbstractRobot(Wheel[] wheels, Port irPort, Port colorPort, BaseRegulatedMotor clamp, double wheelDiameter, double widthTrack) throws Exception {
		this.wheels = wheels;
		this.widthTrack = widthTrack ;
		this.wheelDiameter = wheelDiameter;
		this.irAdapter = new InfraredAdapter(irPort);
		this.colorSensor = new EV3ColorSensor(colorPort);
		this.clamp = clamp;
		if (wheels.length != 2) {
			throw new Exception("Abstract Robot should have 2 wheels");
		}
		this.leftWheel = (BaseRegulatedMotor)this.wheels[0].getMotor();
		this.rightWheel = (BaseRegulatedMotor)this.wheels[1].getMotor();
		this.chassis = new WheeledChassis(wheels, WheeledChassis.TYPE_DIFFERENTIAL);
		this.pilot = new MovePilot(chassis);
		this.navigator = new Navigator(this.pilot);
		this.initClamp();
	}
	
	public void initClamp() {
		while (!this.clamp.isStalled()) {			
			this.clamp.rotate(-90);
		}
	}
	
	public boolean getClampState() {
		return this.openClamp;
	}
	
	public void setClampState(boolean state) {
		this.openClamp = state;
	}
	
	public void resetTachoCount() {
		for(int i = 0; i < this.wheels.length; i++) {
			this.wheels[i].getMotor().resetTachoCount();
		}
	}
	
	public void resetSpeed() {
		for(int i = 0; i < this.wheels.length; i++) {
			this.wheels[i].getMotor().setSpeed(DEFAULT_SPEED);
		}
		this.pilot.setLinearSpeed(DEFAULT_SPEED);
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
