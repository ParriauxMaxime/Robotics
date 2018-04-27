package robotics;

import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.subsumption.*;

public class AbstractBehaviorRobot extends AbstractRobot {
	Behavior[] behavioursArray = {};
	protected Arbitrator arbitrator;
	protected boolean objectFound = false;
	protected boolean objectGrabbed = false;
	protected String currentBehavior = "";
	
	public String getCurrentBehavior() {
		return currentBehavior;
	}

	public void setCurrentBehavior(String currentBehavior) {
		this.currentBehavior = currentBehavior;
	}

	public AbstractBehaviorRobot(BaseRegulatedMotor[] wheels,  Port irPort, Port colorPort, BaseRegulatedMotor clamp, double wheelDiameter, double widthTrack) throws Exception {
		super(wheels, irPort, colorPort, clamp, wheelDiameter, widthTrack);
	}
	
	public AbstractBehaviorRobot(BaseRegulatedMotor[] wheels,  Port irPort, Port colorPort, BaseRegulatedMotor clamp) throws Exception {
		super(wheels, irPort, colorPort, clamp);
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
	
	public void startArbitrator()  {
			this.initClamp();
			this.arbitrator.go();
	}
	
	public void stopArbitrator() {
		this.arbitrator.stop();
	}

}
