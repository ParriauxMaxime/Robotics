package robotics;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.SampleProvider;

public class InfraredAdapter extends Thread {
	int objectDistance = 1000;
	EV3IRSensor irSensor = new EV3IRSensor(SensorPort.S2);
	SampleProvider sp = irSensor.getDistanceMode();

	public InfraredAdapter() {
		this.setDaemon(true);
		this.start();
	}
	
	public void run() {
		while(true) {
			float [] sample = new float[sp.sampleSize()];
			sp.fetchSample(sample, 0);
			if((int)sample[0]==0) 
				objectDistance=1000;
			else 
				objectDistance=(int)sample[0];
			Thread.yield();
		}
	}
	public int getObjectDistance() {return objectDistance;}
}

