package server.model;

import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * @author Anna Selstam
 * Class that controlls the playing of the music. 
 */
public class Music extends Thread {

    private Random rand = new Random();
    private Player player;

    // Variable for the Java-Server to listen to! 
    private boolean musicOn = true;

    /**
     * Constructor that starts a new inner class and calls on its run-method.
     */
    public Music() {

        player = new Player();
        player.start();

    }

    // Runs when the controller (Java-Server) initializes the music.
    public void run() {
        randomizer();
    }

    /**
     * Method that listens to the inner class. 
     * Randomizes the amount of time the music (thread) should play (be active) 
     * or not play (be inactive).
     */
    public void randomizer() {
        while (!Thread.interrupted()) {

            while (musicOn) {

                startMusic();
                try {
                    int x = rand.nextInt(6);
                    if (x >= 1) {
                        TimeUnit.SECONDS.sleep(x);
                        musicOn = false;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            stopMusic();
            try {
                int x = rand.nextInt(5);
                if (x >= 2) {
                    TimeUnit.SECONDS.sleep(x);
                    musicOn = true;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void startMusic() {
        player.setPlaying(true);
    }

    public void stopMusic() {
        player.setPlaying(false);
    }

    public Player getPlayer() {
        return player;
    }

    public void stopGame() {
        player.setClipStop();
        stopMusic();
        player.interrupt();
        interrupt();
    }

    /////////////// INNER CLASS, Only plays the music ///////////////

    public class Player extends Thread {
        private boolean playing;
        private Clip clip = null;

        public void run() {
            try {

                // File musicPath = new File("sounds/Tequila.wav");

                File musicPath = new File("sounds/butchy.wav");

                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                this.clip = AudioSystem.getClip();
                clip.open(audioInput);

                while (!Thread.interrupted()) {

                    while (playing) {
                        clip.start();
                    }
                    clip.stop();
                }
            } catch (Exception e) {
            }
        }

        public boolean getPlaying() {
            return playing;
        }

        public void setPlaying(boolean playing) {
            this.playing = playing;
        }

        public void setClipStop() {
            this.clip.close();
        }
    }

    /////////////// INNER CLASS, Only plays the music ///////////////

    public void startSound() {
        try {
            File startPath = new File("sounds/start.wav");
            AudioInputStream startaudio = AudioSystem.getAudioInputStream(startPath);
            Clip startclip = AudioSystem.getClip();
            startclip.open(startaudio);
            startclip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playLoose() {
        try {
            File loosePath = new File("sounds/gameover.wav");
            AudioInputStream looseaudio = AudioSystem.getAudioInputStream(loosePath);
            Clip looseclip = AudioSystem.getClip();
            looseclip.open(looseaudio);
            looseclip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playFinish() {
        try {
            File winPath = new File("sounds/finish.wav");
            AudioInputStream winaudio = AudioSystem.getAudioInputStream(winPath);
            Clip winclip = AudioSystem.getClip();
            winclip.open(winaudio);
            winclip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Random getRand() {
        return this.rand;
    }

    public void setRand(Random rand) {
        this.rand = rand;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isMusicOn() {
        return this.musicOn;
    }

    public boolean getMusicOn() {
        return this.musicOn;
    }

    public void setMusicOn(boolean musicOn) {
        this.musicOn = musicOn;
    }

}