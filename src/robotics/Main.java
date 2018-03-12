package robotics;

public class Main {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AbstractRobot myRobot = new Robot15();
		myRobot.assignementMotor();
		myRobot.assignementMovePilot();
		myRobot.assignementNavigator();
	}
}