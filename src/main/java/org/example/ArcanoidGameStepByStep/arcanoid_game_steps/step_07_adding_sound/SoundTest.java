package org.example.ArcanoidGameStepByStep.arcanoid_game_steps.step_07_adding_sound;
import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class SoundTest {
    public static void main(String[] args) throws Exception {

		System.out.println("1");
		URL url = new URL("C:\\Users\\Oleg_Chumin\\IdeaProjects\\ArcanoidGameStepByStep\\src\\main\\java\\arcanoid_game_steps\\step_07_adding_sound\\sound\\game_over.wav");
		System.out.println("2");
		AudioClip clip = Applet.newAudioClip(url);
		System.out.println("3");
		clip.play();
		System.out.println("4");
		Thread.sleep(1000);

//		URL url = new URL(
//			"file:/C:/Users/Eli/workspace/minitennis/src/com/edu4java/minitennis7/back.wav");

//        URL url = SoundTest.class.getResource("game_over.wav");
//        URL url = new URL("C:\\Users\\Oleg_Chumin\\IdeaProjects\\ArcanoidGameStepByStep\\src\\main\\java\\arcanoid_game_steps\\step_07_adding_sound\\sound\\game_over.wav");
//        AudioClip clip = Applet.newAudioClip(url);
//        AudioClip clip2 = Applet.newAudioClip(url);
//        clip.play();
//        Thread.sleep(1000);
//        clip2.loop();
//        Thread.sleep(20000);
//        clip2.stop();

        System.out.println("end");
    }
}
