/**
 * For handling audio clips
 *
 * @author Shai Zayden and Mehakpreet Jawanda
 * @version (3-31-2025)
 */

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineEvent;

public class audioPlayer implements Runnable {
    private String soundFilePath;

    public audioPlayer(String soundFilePath) 
    {
        this.soundFilePath = soundFilePath;
    }

    @Override
    public void run() 
    {
        playSound(soundFilePath);
    }

    private void playSound(String filePath) 
    {
        try 
        {
            File soundFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            clip.addLineListener(new LineListener()
                {
                    @Override
                    public void update(LineEvent event) 
                    {
                        if (event.getType() == LineEvent.Type.STOP)
                        {
                            clip.close();
                        }
                    }
                });

            while (clip.isOpen()) 
            {
                if (Thread.currentThread().isInterrupted()) 
                {
                    clip.close();
                    Thread.currentThread().interrupt(); 
                    return;
                }
            }
        } catch (Exception ex) 
        {
            ex.printStackTrace();
        }
    }
}

