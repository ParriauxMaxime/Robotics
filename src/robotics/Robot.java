package robotics;

import lejos.hardware.motor.*;

public abstract class Robot implements IRobot {
	protected double widthTrack;
	protected BaseRegulatedMotor left;
	protected BaseRegulatedMotor right;
	protected double wheelWidth;
	protected int DEFAULT_SPEED = 360;
	
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

	@Override
	public abstract int forward(double distance);

	@Override
	public abstract int turnLeft(int angle, double radiusTurn);

	@Override
	public abstract int turnRight(int angle, double radiusTurn);
}
