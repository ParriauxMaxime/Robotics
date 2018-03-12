package robotics;

import lejos.hardware.lcd.*;
import lejos.hardware.motor.Motor;
import lejos.utility.*;
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Robot15 myRobot = new Robot15(Motor.B, Motor.C, 0.1418, 0.038);
		myRobot.assignementMotor();
		myRobot.assignementMovePilot();
	}
	
	public static void printSomething(String str, int delay) {
		LCD.drawString(str, 0, 4);
		Delay.msDelay(delay);
	}
}