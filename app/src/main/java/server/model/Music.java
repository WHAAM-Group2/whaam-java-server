package server.model;

import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music extends Thread{
    private Random rand = new Random();
    private Player player;
    
    // Variable to control a game from the Java-Server! 
    private boolean activeGame = true;

    // Variable for the Java-Server to listen to! 
    private boolean musicOn = true;

    public Music () {
        player = new Player();
        player.start();
    }

    public void run() {
        randomizer();
    }

    // "activeGame" needs to be set to true, when starting a game. 
    // "activeGame" needs to be set to false, when ending a game. 
    public void randomizer () {
        while (!Thread.interrupted()) {

            while (musicOn) {

                startMusic();
                try {
                    int x = rand.nextInt(6);
                    if (x >= 2) {
                        TimeUnit.SECONDS.sleep(x);
                        musicOn = false;
                    }
                } catch (InterruptedException e) { e.printStackTrace(); }
            }

            stopMusic();
            try {
                int x = rand.nextInt(5);
                if (x >= 1) {
                    TimeUnit.SECONDS.sleep(x);
                    musicOn = true;
                }
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    public void startMusic() {
        player.setPlaying(true);
    }

    public void stopMusic() {
        player.setPlaying(false);
    }

    public void setActiveGameFalse() {
        this.activeGame = false;
    }

    public void setActiveGameTrue() {
        this.activeGame = true;
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

        public void run () {
            try {
                File musicPath = new File("/Users/annaselstam/Documents/GitHub/MusicTest2/Tequila.wav");
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                this.clip = AudioSystem.getClip();
                clip.open(audioInput);
                
                while(!Thread.interrupted()) {
                    
                    while(playing) {
                        clip.start();
                    }
                    clip.stop();
                }
            } catch (Exception e) { }
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
            File startPath = new File("/Users/annaselstam/Desktop/MusicTest/start.wav");
            AudioInputStream startaudio = AudioSystem.getAudioInputStream(startPath);
            Clip startclip = AudioSystem.getClip();
            startclip.open(startaudio);
            startclip.start();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void playLoose() {
        try {
            File loosePath = new File("/Users/annaselstam/Desktop/MusicTest/gameover.wav");
            AudioInputStream looseaudio = AudioSystem.getAudioInputStream(loosePath);
            Clip looseclip = AudioSystem.getClip();
            looseclip.open(looseaudio);
            looseclip.start();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void playFinish() {
        try {
            File winPath = new File("/Users/annaselstam/Desktop/MusicTest/finish.wav");
            AudioInputStream winaudio = AudioSystem.getAudioInputStream(winPath);
            Clip winclip = AudioSystem.getClip();
            winclip.open(winaudio);
            winclip.start();
        } catch (Exception e) { e.printStackTrace(); }
    }

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.util.Random;
import java.util.Scanner;

public class MusicSimulator extends Thread {

    private MusicPlayer mp;
    private Random rand = new Random();
    private Scanner input = new Scanner(System.in);

    public MusicSimulator() {
        // startSound();
        mp = new MusicPlayer();
        // mp.start();
        // gameLogic();
    }

    public class MusicPlayer extends Thread {

        private boolean playing;
        private boolean running;
        private Clip clip;

        public void run() {

            playing = true;
            running = true;
            clip = null;
            
            try {
                // Thread.sleep(4000);

                // File musicPath = new
                // File("/Users/annaselstam/Desktop/MusicTest/Tequila.wav");
                File musicPath = new File("sounds/Tequila.wav");
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                this.clip = AudioSystem.getClip();
                clip.open(audioInput);

                while (playing) {

                    while (running) {

                        clip.start();
                        int playTime = rand.nextInt(8);

                        if (playTime >= 2) {
                            Thread.sleep(playTime * 1000);
                            System.out.println("Played for: " + playTime + "s");
                            clip.stop();
                            running = false;
                        }
                    }

                    int stopTime = rand.nextInt(6);
                    if (stopTime >= 1) {
                        Thread.sleep(stopTime * 1000);
                        System.out.println("Stopped for: " + stopTime + "s");
                        running = true;
                    }
                }
                interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void stopClip() {
            this.clip.stop();
        }

        public boolean getRunningStatus() {
            return running;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        public boolean getPlayingStatus() {
            return playing;
        }

        public void setPlaying(boolean playing) {
            this.playing = playing;
        }

    }

    ///////////////////////////////////////////

    private void startSound() {
        try {
            // File startPath = new File("/Users/annaselstam/Desktop/MusicTest/start.wav");
            File startPath = new File("sounds/start.wav");

            AudioInputStream startaudio = AudioSystem.getAudioInputStream(startPath);
            Clip startclip = AudioSystem.getClip();
            startclip.open(startaudio);
            startclip.start();
            
            while (startclip.isActive()) {
                continue;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playLoose() {
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

    public MusicPlayer getMp() {
        return mp;
    }

    public void setMp(MusicPlayer mp) {
        this.mp = mp;
    }

    public Random getRand() {
        return this.rand;
    }

    public void setRand(Random rand) {
        this.rand = rand;
    }

    public Scanner getInput() {
        return this.input;
    }

    public void setInput(Scanner input) {
        this.input = input;
    }

    public void stopPlaying() {
        getMp().setRunning(false);
        getMp().setPlaying(false);
        getMp().stopClip();
        getMp().interrupt();
    }

}