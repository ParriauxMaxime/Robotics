package robotics;

import lejos.hardware.Button;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.robotics.subsumption.*;

public class AbstractBehaviorRobot extends AbstractRobot {
	Behavior[] behavioursArray;
	Arbitrator arbitrator;
	
	public AbstractBehaviorRobot(BaseRegulatedMotor left, BaseRegulatedMotor right, double widthTrack, double wheelWidth, Behavior[] bList) {
		super(left, right, widthTrack, wheelWidth);
		this.behavioursArray = bList;
	//	this.arbitrator = new Arbitrator(this.behavioursArray);
		/*this.arbitrator.go();
		while (!Button.LEFT.isDown()) {
			try {
				Thread.sleep(100);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.arbitrator.stop();*/
	}

}
