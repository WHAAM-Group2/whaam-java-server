package javaserver;

import java.util.concurrent.TimeUnit;

public class MusicSimulator extends Thread{
    private boolean music = false;
    public MusicSimulator(){
        start();
    }
    @Override
    public void run() {
        try {
            //changes the value of a boolean every 4 seconds
            //to simulate music playing/pausing, true resp. false
            while(true) {
                TimeUnit.SECONDS.sleep(2);
                setMusic(!music);
                TimeUnit.SECONDS.sleep(2);
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
