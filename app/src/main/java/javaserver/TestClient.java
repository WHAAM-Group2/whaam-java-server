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
    private boolean win = false;
    private boolean music = false;
    private MusicSimulator musicSimulator;
    private String messageToArduino;
    private boolean copy;


    public TestClient() throws IOException {
        System.out.println("started");
        musicSimulator = new MusicSimulator();
        connectToArduino();
        //System.out.println("started");

    }

    public void connectToArduino() throws IOException {
            Socket socket = new Socket(ipArduino, port);
            System.out.println("gamehandler started");
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            new ArduinoHandler();

    }

    public void run(){ // java serverns tråd
    }

    private class ArduinoHandler extends Thread{

        public ArduinoHandler(){
            start();
        }

        @Override
        public void run() {
            try {
                while(true) {
                 
                   
                    /*if(dis.available() > 0){
                        win = dis.readBoolean();  
                        
                    }*/
                    win = dis.readBoolean();
                    
                    if(musicSimulator.isMusic()){
                        setMessageToArduino("a");
                    }
                    else{
                        setMessageToArduino("b");
                    }
                    
                  
                    dos.write(getMessageToArduino().getBytes());
                    dos.flush();
                    System.out.println(messageToArduino);
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

    public String getMessageToArduino() {
        return messageToArduino;
    }

    public void setMessageToArduino(String messageToArduino) {
        this.messageToArduino = messageToArduino;
    }


}

