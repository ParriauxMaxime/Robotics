package robotics;

import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.subsumption.*;

public class AbstractBehaviorRobot extends AbstractRobot {
	Behavior[] behavioursArray;
	Arbitrator arbitrator;
	protected boolean objectFound = false;
	protected boolean objectGrabbed = false;
	
	public AbstractBehaviorRobot(Wheel[] wheels,  Port irPort, Port colorPort, BaseRegulatedMotor clamp, double wheelDiameter, double widthTrack) throws Exception {
		super(wheels, irPort, colorPort, clamp, wheelDiameter, widthTrack);
	}
	
	public boolean isObjectGrabbed() {
		return objectGrabbed;
	}

	public void setObjectGrabbed(boolean objectGrabbed) {
		this.objectGrabbed = objectGrabbed;
	}

	public boolean getObjectFound() {
		return this.objectFound;
	}
	
	public void setObjectFound(boolean val) {
		this.objectFound = val;
	}
	
	protected void setBehaviorList(Behavior[] bList) {
		this.behavioursArray = bList;
	}
	
	protected Behavior[] getBehaviorList() {
		return this.behavioursArray;
	}
	
	void startArbitrator() throws Exception {
		if (this.behavioursArray != null) {
			this.arbitrator = new Arbitrator(this.behavioursArray);			
			this.arbitrator.go();
		}
		else
			throw new Exception("You should define a behaviourArray");
	}
	
	void stopArbitrator() {
		this.arbitrator.stop();
	}

}
