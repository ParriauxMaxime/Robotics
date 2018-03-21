package robotics;

import lejos.hardware.motor.Motor;

public final class Robot15 extends AbstractRobot {
	static double WHEEL_DIAMETER = 0.038;
	static double WIDTH_TRACK = 0.1418;
	protected int DEFAULT_SPEED = 100;
	public Robot15() {
		super(Motor.B, Motor.C, Robot15.WIDTH_TRACK, Robot15.WHEEL_DIAMETER);
	}
}
