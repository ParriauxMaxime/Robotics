package robotics;

import lejos.hardware.motor.*;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.navigation.*;
import lejos.robotics.BaseMotor;
import lejos.robotics.Color;
import lejos.robotics.chassis.*;
import lejos.robotics.pathfinding.*;


public abstract class AbstractRobot implements IRobot {
	protected Wheel[] wheels;
	protected double wheelDiameter = 4.15,  widthTrack = 13.47;
	protected final int LINEAR_SPEED = 10, ANGULAR_SPEED = 60;
	public BaseRegulatedMotor leftMotor, rightMotor;
	public InfraredAdapter irAdapter;
	public ColorAdapter colorAdapter;
	public BaseRegulatedMotor clamp;
	public MovePilot pilot;
	protected Chassis chassis;
	public	Navigator navigator;
	protected boolean openClamp = false;

	AbstractRobot(BaseRegulatedMotor[] wheels, double wheelDiameter, double widthTrack) throws Exception {
		if (wheels.length != 2) {
			throw new Exception("Abstract Robot should have 2 wheels");
		}
		this.leftMotor = wheels[0];
		this.rightMotor = wheels[1];
		this.widthTrack = widthTrack ;
		this.wheelDiameter = wheelDiameter;
		this.initStuff();
	}
	
	AbstractRobot(BaseRegulatedMotor[] wheels) throws Exception {
		if (wheels.length != 2) {
			throw new Exception("Abstract Robot should have 2 wheels");
		}
		this.leftMotor = wheels[0];
		this.rightMotor = wheels[1];
		this.initStuff();
	}
	
	AbstractRobot(BaseRegulatedMotor[] wheels, Port irPort, Port colorPort, BaseRegulatedMotor clamp) throws Exception {
		if (wheels.length != 2) {
			throw new Exception("Abstract Robot should have 2 wheels");
		}
		this.leftMotor = wheels[0];
		this.rightMotor = wheels[1];
		this.clamp = clamp;
		this.irAdapter = new InfraredAdapter(irPort);
		this.colorAdapter = new ColorAdapter(colorPort);
		this.initStuff();
	}
	
	AbstractRobot(BaseRegulatedMotor[] wheels, Port irPort, Port colorPort, BaseRegulatedMotor clamp, double wheelDiameter, double widthTrack) throws Exception {
		if (wheels.length != 2) {
			throw new Exception("Abstract Robot should have 2 wheels");
		}
		this.leftMotor = wheels[0];
		this.rightMotor = wheels[1];
		this.clamp = clamp;
		this.widthTrack = widthTrack ;
		this.wheelDiameter = wheelDiameter;
		this.irAdapter = new InfraredAdapter(irPort);
		this.colorAdapter = new ColorAdapter(colorPort);
		this.initStuff();
	}
	
	public void initStuff() {
		if (this.clamp != null) {
			
			//this.clamp.setStallThreshold(1, 100);
		}
		this.wheels = new Wheel[] {
				new WheeledChassis.Modeler(leftMotor, this.wheelDiameter).offset(this.widthTrack/ 2),
				new WheeledChassis.Modeler(rightMotor, this.wheelDiameter).offset(-this.widthTrack / 2)
		};
		this.chassis = new WheeledChassis(this .wheels, WheeledChassis.TYPE_DIFFERENTIAL);
		this.pilot = new MovePilot(chassis);
		this.pilot.setLinearSpeed(LINEAR_SPEED);
		this.pilot.setAngularSpeed(ANGULAR_SPEED);
		this.navigator = new Navigator(this.pilot);
	}
	
	
	public double getWheelDiameter() {
		return wheelDiameter;
	}


	public void setWheelDiameter(double wheelDiameter) {
		this.wheelDiameter = wheelDiameter;
		this.initStuff();
	}


	public double getWidthTrack() {
		return widthTrack;
	}

	public void setWidthTrack(double widthTrack) {
		this.widthTrack = widthTrack;
		this.initStuff();
	}


	public void initClamp() {
		while (!this.clamp.isStalled()) {			
			this.clamp.rotate(-180);
		}
		this.setClampState(false);
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
			this.wheels[i].getMotor().setSpeed(LINEAR_SPEED);
		}
		this.pilot.setLinearSpeed(LINEAR_SPEED);
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
		
		this.rightMotor.synchronizeWith(new BaseRegulatedMotor[] {this.leftMotor});
		this.rightMotor.setSpeed(speedR);
		this.leftMotor.setSpeed(speedL);
		this.rightMotor.startSynchronization();
		this.rightMotor.forward();
		this.leftMotor.forward();
		this.rightMotor.endSynchronization();
		try {Thread.sleep(time * 1000);} catch (Exception e) {}
		this.rightMotor.startSynchronization();
		this.leftMotor.stop();
		this.rightMotor.stop();
		this.rightMotor.endSynchronization();
	}

	@Override
	public void forward(double distance) {
		int time = 1;
		double wheelDistance = (this.wheelDiameter * Math.PI);
		double nbTurn = (distance / wheelDistance);
		int angle = (int)(nbTurn * 360);
		int speed = (angle / time);
		while (speed > LINEAR_SPEED) {
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
		while (externalSpeed > LINEAR_SPEED || internalSpeed > LINEAR_SPEED) {
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
		this.leftMotor.synchronizeWith(new BaseRegulatedMotor[] {this.rightMotor});
		while (externalSpeed > LINEAR_SPEED || internalSpeed > LINEAR_SPEED) {
			time++;
			internalSpeed = (int)((radiusTurn * angle) / ((wheelDiameter/2) * time));
			externalSpeed = (int)(((radiusTurn + widthTrack) * angle) / ((wheelDiameter/2) * time));
		}
		
		this.differentialDrive(internalSpeed, externalSpeed, time);
	}

}
