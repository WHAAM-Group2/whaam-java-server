package javaserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TestClient {
    private int port = 1337;
    private String ipArduino = "192.168.0.12";
    private DataInputStream dis;
    private DataOutputStream dos;
    private boolean win;
    private boolean music;
    private MusicSimulator musicSimulator;
    private String messageToArduino;
    private byte messageFromArduino;
    


    public TestClient() throws IOException {
        System.out.println("Started");
        musicSimulator = new MusicSimulator();
        connectToArduino();

    }

    public void connectToArduino() throws IOException {
            Socket socket = new Socket(ipArduino, port);
            System.out.println("Gamehandler started");
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            new ArduinoHandler();
            //new MessageReceiver();

    }

    private class ArduinoHandler extends Thread{

        public ArduinoHandler(){
            start();
        }

        @Override
        public void run() {
            try {
                while(true) {
                    messageFromArduino = dis.readByte();
                    //87 = W 
                    if(messageFromArduino == 87){
                        setWin(true);
                    }
                    //76 = L 
                    if(messageFromArduino == 76){
                        setWin(false);
                    }
                        System.out.println("message received:" + win);

                    music = musicSimulator.isMusic();
                    //get the music value from the "music simulator"
                    if(music){
                        //if true, = music is playing, set message to "a"  
                        setMessageToArduino("a");
                    }
                    if(!music){
                        //else, = music is not playing, set message to "b" 
                        setMessageToArduino("b");
                    }
                    
                    //send message to Arduino-Server
                    dos.write(getMessageToArduino().getBytes());
                    dos.flush();

                    System.out.println("message sent:" + messageToArduino);
                    //System.out.println(musicSimulator.isMusic());
                }
            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    


    public static void main(String[] args) throws IOException {
        new TestClient();
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public String getMessageToArduino() {
        return messageToArduino;
    }

    public void setMessageToArduino(String messageToArduino) {
        this.messageToArduino = messageToArduino;
    }


}

