package javaserver;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class MusicSimulator extends Thread{
    private boolean music = false;
    public MusicSimulator(){
        start();
    }
    @Override
    public void run() {
        // Scanner in = new Scanner(System.in);

        try {
            //changes the value of a boolean every 4 seconds
            //to simulate music playing/pausing, true resp. false
            while(!interrupted()) {
                // String yay = in.nextLine();
                setMusic(!music);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isMusic() {
        return music;
    }
    public void setMusic(boolean music) {
        this.music = music;
    }

}
