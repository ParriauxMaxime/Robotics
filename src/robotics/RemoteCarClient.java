package robotics;

import java.io.*;
import java.net.Socket;

import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;

import java.awt.*;
import java.awt.event.*;

public class RemoteCarClient extends Frame implements KeyListener {

	public static final int PORT = RemoteCarServer.port;
	public static boolean arbitratorOn = false;
	public static final int CLOSE = 0;
	public static int irDistance = 0;
	public static int color = -1;
	public static String behaviour = "";
	public static double wheelDiameter = 4.15;
	public static double trackWidth = 13.47;
	public static final int CALIBRATE_CIRCLE = 10, CALIBRATE_DISTANCE = 11, SEND_CALIBRATION = 12, NOTIFY = 1, START = 42, STOP = 43,
			DEBUG = 78,
			FORWARD = 87, // W = main up
			STRAIGHT = 83, // S = straight
			LEFT = 65, // A = left
			RIGHT = 68, // D = right
			BACKWARD = 88; // X = main down

	Thread dataHandler;

	Label labIrDistance, labColorSensor, labCurrentBehavior;
	Button btnConnect;
	Button btnStart;
	Button btnStop;
	Button btnSendCalibration;
	Button btnCalibrateCircle;
	Button btnCalibrateDistance;
	TextField txtIPAddress;
	TextField txtTrackWidth;
	TextField txtWheelDiameter;
	TextArea messages;

	private Socket socket;
	private DataOutputStream outStream;
	private DataInputStream inStream;

	public RemoteCarClient(String title, String ip) {
		super(title);
		this.setSize(800, 600);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("Ending Warbird Client");
				disconnect();
				System.exit(0);
			}
		});
		buildGUI(ip);
		this.setVisible(true);
		btnConnect.addKeyListener(this);
		btnSendCalibration.addKeyListener(this);
		btnCalibrateCircle.addKeyListener(this);
		btnCalibrateDistance.addKeyListener(this);
	}

	public static void main(String args[]) {
		String ip = "10.0.1.1";
		if (args.length > 0)
			ip = args[0];
		System.out.println("Starting R/C Client...");
		new RemoteCarClient("R/C Client", ip);
	}

	public void buildGUI(String ip) {
		Panel mainPanel = new Panel(new BorderLayout());
		ControlListener cl = new ControlListener();

		btnConnect = new Button("Connect");
		btnConnect.addActionListener(cl);
		btnSendCalibration = new Button("Send calibration");
		btnSendCalibration.addActionListener(cl);
		btnCalibrateDistance = new Button("Calibrate (distance)");
		btnCalibrateDistance.addActionListener(cl);
		btnCalibrateCircle = new Button("Calibrate (circle)");
		btnCalibrateCircle.addActionListener(cl);
		btnStart = new Button("Start arbitrator");
		btnStop = new Button("Stop arbitrator");
		btnStart.addActionListener(cl);
		btnStop.addActionListener(cl);
		btnStop.setEnabled(false);
		txtIPAddress = new TextField(ip, 16);
		txtWheelDiameter = new TextField(Double.toString(wheelDiameter), 16);
		txtTrackWidth = new TextField(Double.toString(trackWidth), 16);
		labIrDistance = new Label(String.valueOf(irDistance));
		labColorSensor = new Label(String.valueOf(color));
		labCurrentBehavior = new Label(behaviour);

		dataHandler = new Thread(new DataHandler(labIrDistance, labColorSensor, labCurrentBehavior));

		messages = new TextArea("status: DISCONNECTED");
		messages.setEditable(false);

		Panel north = new Panel(new GridLayout(3, 2));
		north.add(btnConnect);
		north.add(txtIPAddress);
		north.add(new Label("Track width: "));
		north.add(txtTrackWidth);
		north.add(new Label("Wheel Diameter: "));
		north.add(txtWheelDiameter);

		Panel center = new Panel(new GridLayout(5, 1));
		center.add(btnSendCalibration);
		center.add(btnCalibrateCircle);
		center.add(btnCalibrateDistance);
		center.add(btnStart);
		center.add(btnStop);

		Panel center4 = new Panel(new FlowLayout(FlowLayout.LEFT));
		center4.add(messages);

		center.add(center4);

		Panel south = new Panel(new GridLayout(3, 2));
		south.add(new Label("InfraredDistance: "));
		south.add(labIrDistance);
		south.add(new Label("ColorSensor: "));
		south.add(labColorSensor);
		south.add(new Label("Current behavior"));
		south.add(labCurrentBehavior);

		mainPanel.add(north, "North");
		mainPanel.add(center, "Center");
		mainPanel.add(south, "South");
		this.add(mainPanel);
	}

	private void sendCommand(int command) {
		// Send coordinates to Server:
		messages.setText("status: SENDING command.");
		try {
			outStream.writeInt(command);
		} catch (IOException io) {
			messages.setText("status: ERROR Problems occurred sending data.");
		}

		messages.setText("status: Command SENT.");
	}

	private void sendCalibration() {
		sendCommand(SEND_CALIBRATION);
		try {
			outStream.writeDouble(Double.parseDouble(txtTrackWidth.getText()));
			outStream.writeDouble(Double.parseDouble(txtWheelDiameter.getText()));
		} catch (IOException e) {
			messages.setText("status: Failed to send calibration");
		}
		messages.setText("Calibrated");
	}

	private class DataHandler implements Runnable {
		Label ir, color, behavior;

		public DataHandler(Label irDistance, Label color, Label behavior) {
			this.ir = irDistance;
			this.color = color;
			this.behavior = behavior;
		}

		public void run() {
			while (!socket.isClosed()) {
				try {
					outStream.writeInt(NOTIFY);
					int verif = inStream.readInt();
					if (verif == 0) {
						irDistance = inStream.readInt();
						RemoteCarClient.color = inStream.readInt();
						behaviour = inStream.readUTF();
						this.behavior.setText(behaviour);
						this.ir.setText(String.valueOf(irDistance));
						this.color.setText(String.valueOf(RemoteCarClient.color));
					}
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
		}
	}

	/** A listener class for all the buttons of the GUI. */
	private class ControlListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("Connect")) {
				connect();
				dataHandler.start();
			} else if (command.equals("Disconnect")) {
				disconnect();
			} else if (command.equals("Calibrate (circle)")) {
				sendCommand(CALIBRATE_CIRCLE);
			} else if (command.equals("Calibrate (distance)")) {
				sendCommand(CALIBRATE_DISTANCE);
			} else if (command.equals("Send calibration")) {
			//sendCommand(DEBUG);
					sendCalibration();
			} else if (command.equals("Start arbitrator")) {
				sendCommand(START);
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
			} else if (command.equals("Stop arbitrator")) {
				sendCommand(STOP);
				btnStop.setEnabled(false) ;
				btnStart.setEnabled(true);
			}
		}
	}

	public void connect() {
		try {
			socket = new Socket(txtIPAddress.getText(), PORT);
			outStream = new DataOutputStream(socket.getOutputStream());
			inStream = new DataInputStream(socket.getInputStream());
			messages.setText("status: CONNECTED");
			btnConnect.setLabel("Disconnect");

		} catch (Exception exc) {
			messages.setText("status: FAILURE Error establishing connection with server.");
			System.out.println("Error: " + exc);
		}
	}

	public void disconnect() {
		try {
			dataHandler.interrupt();
			sendCommand(CLOSE);
			socket.close();
			btnConnect.setLabel("Connect");
			messages.setText("status: DISCONNECTED");
		} catch (Exception exc) {
			messages.setText("status: FAILURE Error closing connection with server.");
			System.out.println("Error: " + exc);
		}
	}

	public void keyPressed(KeyEvent e) {
		// sendCommand(e.getKeyCode());
		// System.out.println("Pressed " + e.getKeyCode());
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent arg0) {
	}
}
