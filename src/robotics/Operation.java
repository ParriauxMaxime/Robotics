package robotics;

import lejos.hardware.motor.*;
public class Operation implements Runnable {
	BaseRegulatedMotor motor;
	int rotation;

	public Operation(BaseRegulatedMotor motor, int rotation) {
		this.motor = motor;
		this.rotation = rotation;
	}
	
	public void run() {
		this.motor.rotate(rotation);
	}

}
