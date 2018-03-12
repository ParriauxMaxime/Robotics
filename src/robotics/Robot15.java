package robotics;

import lejos.robotics.navigation.*;
import lejos.hardware.motor.Motor;
import lejos.robotics.chassis.*;



public final class Robot15 extends AbstractRobot {
	static double WHEEL_DIAMETER = 0.1418;
	static double WIDTH_TRACK = 0.038;
	public Robot15() {
		super(Motor.B, Motor.C, Robot15.WHEEL_DIAMETER, Robot15.WIDTH_TRACK);
	}
}
