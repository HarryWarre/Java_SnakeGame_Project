
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class SoundManager {

    private Clip backgroundMusic;
    private Clip gameOverSound;

    public SoundManager() {
        try {
            // Load âm thanh nền
            AudioInputStream backgroundStream = AudioSystem.getAudioInputStream(new File("sound/background.wav"));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(backgroundStream);

            // Load âm thanh game over
            AudioInputStream gameOverStream = AudioSystem.getAudioInputStream(new File("sound/gameover.wav"));
            gameOverSound = AudioSystem.getClip();
            gameOverSound.open(gameOverStream);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
        }
    }

    public void playBackgroundMusic() {
        if (backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
        backgroundMusic.setFramePosition(0);
        backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stopBackgroundMusic() {
        backgroundMusic.stop();
    }

    public void playGameOverSound() {
        gameOverSound.setFramePosition(0);
        gameOverSound.start();
    }
}
