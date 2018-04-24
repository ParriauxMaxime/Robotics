package behaviors;

import lejos.robotics.subsumption.*;
import lejos.hardware.port.*;
import lejos.robotics.SampleProvider;
import lejos.hardware.sensor.*;
import lejos.hardware.Button;

public class BehaviorTouch implements Behavior {
	EV3TouchSensor touch;
	SampleProvider touched;
	float[] sample;

	public BehaviorTouch(Port touchSensor) {
		this.touch = new EV3TouchSensor(touchSensor);
		this.touched = this.touch.getTouchMode();
		this.sample = new float[touched.sampleSize()];
	}

	public boolean takeControl() {
		touched.fetchSample(sample, 0);
		int t = (int) sample[0];
		return t == 1 || Button.ESCAPE.isDown();
	}

	public void action() {
		System.exit(1);
	}

	public void suppress() {
	}
}
