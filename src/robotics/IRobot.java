package robotics;

public interface IRobot {
	public void differentialDrive(int speedL, int speedR, int time);
	public void forward(double distance);
	public void turnLeft(double angle, double radiusTurn);
	public void turnRight(double angle, double radiusTurn);
}
