package parkerbasicchessengine;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class soundManager {

    public void playMoveSound() {
        playSound("/move.wav");
    }

    public void playCaptureSound() {
        playSound("/capture.wav");
    }

    public void playCastleSound() {
        playSound("/castle.wav");
    }

    public void playPromoteSound() {
        playSound("/promote.wav");
    }

    public void playCheckSound() {
        playSound("/check.wav");
    }

    private void playSound(String resourcePath) {
        try {
            URL soundURL = getClass().getResource(resourcePath);
            if (soundURL == null) {
                throw new IllegalArgumentException("Sound file not found: " + resourcePath);
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
