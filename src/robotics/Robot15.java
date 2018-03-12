package robotics;

import lejos.robotics.navigation.*;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.robotics.chassis.*;
import lejos.utility.Delay;


public final class Robot15 extends Robot {

	public Robot15(BaseRegulatedMotor left, BaseRegulatedMotor right, double widthTrack, double wheelWidth) {
		super(left, right, widthTrack, wheelWidth);
		Wheel R = WheeledChassis.modelWheel(this.right, wheelWidth * 1000).offset((widthTrack * 1000 / 2));
		Wheel L = WheeledChassis.modelWheel(this.left, wheelWidth * 1000).offset(-(widthTrack * 1000 / 2));
		this.chassis = new WheeledChassis(new Wheel[] {L, R}, WheeledChassis.TYPE_DIFFERENTIAL);
		this.pilot = new MovePilot(chassis);
		//this.pilot.setAngularSpeed(45);
		this.pilot.setLinearSpeed(DEFAULT_SPEED/2);
		this.navigator = new Navigator(this.pilot);
	}
	
	public static void speedUnreachable() {
		LCD.drawString("speed unreachable", 0, 0);
		lejos.hardware.ev3.LocalEV3.get().getAudio().playTone(440, 1);
	}

	@Override
	/**
	 * parameters: 
	 * distance	: double	- distance in meters
	 */
	public int forward(double distance) {
		this.resetSpeed();
		int time = 2;
		double wheelDistance = (this.wheelWidth * Math.PI);
		double nbTurn = (distance / wheelDistance);
		int angle = (int)(nbTurn * 360);
		this.right.synchronizeWith(new BaseRegulatedMotor[] {this.left});
		while ((angle / time) > DEFAULT_SPEED || (angle/time) > DEFAULT_SPEED) {
			time++;
		}
		
		this.right.setSpeed((angle / time));
		this.left.setSpeed((angle / time));
		this.right.startSynchronization();
		this.right.forward();
		this.left.forward();
		this.right.endSynchronization();
		try {Thread.sleep(time * 1000);} catch (Exception e) {}
		this.right.startSynchronization();
		this.left.stop();
		this.right.stop();
		this.right.endSynchronization();
		//this.right.rotateTo(this.left.getTachoCount());
		return this.right.getTachoCount();
	}

	@Override
	public int turnLeft(int angle, double radiusTurn) {
		this.resetSpeed();
		int time = 2;
		int internalSpeed = (int)((radiusTurn * angle) / ((wheelWidth/2) * time));
		int externalSpeed = (int)(((radiusTurn + widthTrack) * angle) / ((wheelWidth/2) * time));
		this.right.synchronizeWith(new BaseRegulatedMotor[] {this.left});
		while (externalSpeed > DEFAULT_SPEED || internalSpeed > DEFAULT_SPEED) {
			time++;
			internalSpeed = (int)((radiusTurn * angle) / ((wheelWidth/2) * time));
			externalSpeed = (int)(((radiusTurn + widthTrack) * angle) / ((wheelWidth/2) * time));
		}
		
		this.right.setSpeed(internalSpeed);
		this.left.setSpeed(externalSpeed);
		this.right.startSynchronization();
		this.right.forward();
		this.left.forward();
		this.right.endSynchronization();
		try {Thread.sleep(time * 1000);} catch (Exception e) {}
		this.right.startSynchronization();
		this.left.stop();
		this.right.stop();
		this.right.endSynchronization();
		return this.right.getTachoCount();
	}

	@Override
	public int turnRight(int angle, double radiusTurn) {
		this.resetSpeed();
		int time = 2;
		int internalSpeed = (int)((radiusTurn * angle) / ((wheelWidth/2) * time));
		int externalSpeed = (int)(((radiusTurn + widthTrack) * angle) / ((wheelWidth/2) * time));
		this.left.synchronizeWith(new BaseRegulatedMotor[] {this.right});
		while (externalSpeed > DEFAULT_SPEED || internalSpeed > DEFAULT_SPEED) {
			time++;
			internalSpeed = (int)((radiusTurn * angle) / ((wheelWidth/2) * time));
			externalSpeed = (int)(((radiusTurn + widthTrack) * angle) / ((wheelWidth/2) * time));
		}
		
		this.left.setSpeed(internalSpeed);
		this.right.setSpeed(externalSpeed);
		this.left.startSynchronization();
		this.left.forward();
		this.right.forward();
		this.left.endSynchronization();
		try {Thread.sleep(time * 1000);} catch (Exception e) {}
		this.left.startSynchronization();
		this.right.stop();
		this.left.stop();
		this.left.endSynchronization();
		return this.left.getTachoCount();
	}

}
