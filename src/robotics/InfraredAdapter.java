package robotics;
import lejos.hardware.port.*;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.SampleProvider;
import lejos.hardware.lcd.LCD;

public class InfraredAdapter extends Thread {
	int objectDistance = 1000;
	EV3IRSensor irSensor;
	SampleProvider sp;

	public InfraredAdapter(Port  irSensor) {
		this.irSensor = new EV3IRSensor( irSensor);
		this.sp =  this.irSensor.getDistanceMode();
		this.setDaemon(true);
		this.start();
		
	}
	
	public void run() {
		while(true) {
			float [] sample = new float[sp.sampleSize()];
			sp.fetchSample(sample, 0);
			if((int)sample[0]==0) 
				this.objectDistance=0;
			else 
				this.objectDistance=(int)sample[0];
			//LCD.clear();
			//LCD.drawInt(this.objectDistance, 2, 2);
			Thread.yield();
		}
	}
	public int getObjectDistance() {return this.objectDistance;}
}

