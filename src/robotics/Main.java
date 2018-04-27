package robotics;

import lejos.hardware.Sound;

public class Main {
	static int C = 523;
	static int D = 587;
	static int E = 659;
	static int F = 698;
	static int Fs = 739;
	static int G = 783;
	static int A = 880;
	static int B = 987;
	static int[] sounds = Sound.PIANO;

	public static void starWars1() {
		Sound.playNote(sounds, D, 1000);
		Sound.playNote(sounds, A, 1000);
		Sound.playNote(sounds, G, 100);
		Sound.playNote(sounds, Fs, 200);
		Sound.playNote(sounds, E, 100);
		Sound.playNote(sounds, D * 2, 1500);
	}

	public static void starWars2() {
		Sound.playNote(sounds, A, 250);
		Sound.playNote(sounds, G, 250);
		Sound.playNote(sounds, Fs, 500);
		Sound.playNote(sounds, G, 1000);
		Sound.playNote(sounds, E, 500);
		Sound.playNote(sounds, A / 2, 125);
		Sound.playNote(sounds, A / 2, 250);
		Sound.playNote(sounds, A / 2, 125);
		Sound.playNote(sounds, D, 500);
		Sound.playNote(sounds, D, 500);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * Sound.setVolume(5); starWars1(); starWars1(); starWars2(); starWars1();
		 */
		try {
			final BehaviorRobot15 t = new BehaviorRobot15();
			/*Thread d = new Thread() {
				@Override
				public void run() {
			
				}
			};*/
			t.startArbitrator();
			//d.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}