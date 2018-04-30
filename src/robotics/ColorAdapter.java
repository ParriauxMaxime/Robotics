package robotics;
import lejos.hardware.port.*;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class ColorAdapter extends Thread {
	int colorId = -1;
	EV3ColorSensor colorSensor;
	SampleProvider sp;

	public ColorAdapter(Port  colorSensor) {
		this.colorSensor = new EV3ColorSensor( colorSensor);
		this.sp =  this.colorSensor.getColorIDMode();
		this.setDaemon(true);
		this.start();
		
	}
	
	public void run() {
		while(true) {
			float [] sample = new float[sp.sampleSize()];
			sp.fetchSample(sample, 0);
			if((int)sample[0]==0) 
				this.colorId=0;
			else 
				this.colorId=(int)sample[0];
			//LCD.clear();
			//LCD.drawInt(this.objectDistance, 2, 2);
			Thread.yield();
		}
	}
	public int getColorID() {return this.colorId;}
}

