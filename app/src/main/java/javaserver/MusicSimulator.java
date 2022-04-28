package javaserver;

import java.util.concurrent.TimeUnit;

public class MusicSimulator extends Thread{
    private boolean music = true;
    public MusicSimulator(){
        start();
    }
    @Override
    public void run() {
        try {
            while(true) {
               TimeUnit.SECONDS.sleep(2);
                setMusic(!music);
                
                TimeUnit.SECONDS.sleep(2);
                
                //System.out.println(music);
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
