package javaserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import server.controller.GameController;

/** 
 * @author Malin Ramkull & Hedda Eriksson 
 * Description here! +Comment the whole class! 
 */
public class TestClient {
    private int port = 1337;
    private String ipArduino = "192.168.0.12";
    private DataInputStream dis;
    private DataOutputStream dos;
    private boolean win;
    private boolean music;
    private String messageToArduino;
    private byte messageFromArduino;
    private GameController controller;
    private boolean musicBefore;

    private boolean sensorCovered;

    public TestClient(GameController controller) throws IOException {
        System.out.println("Started Arduino Client");
        this.controller = controller;
        connectToArduino();

    }

    public void connectToArduino() throws IOException {
        Socket socket = new Socket(ipArduino, port);
        System.out.println("Gamehandler started");
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
        new ArduinoHandler();
        musicBefore = false;
        // new MessageReceiver();

    }

    private class ArduinoHandler extends Thread {

        public ArduinoHandler() {
            start();
        }

        @Override
        public void run() {
            try {
                while (true) {

                    messageFromArduino = dis.readByte();
                    // 87 = W
                    if (messageFromArduino == 84) {
                        setSensorCovered(true);
                        // setWin(true);
                    }
                    // 76 = L
                    if (messageFromArduino == 70) {
                        setSensorCovered(false);
                    }

                    try {
                        music = controller.getMusicStatus();

                        // get the music value from the "music simulator"
                        if (music) {
                            // if true, = music is playing, set message to "a"
                            setMessageToArduino("a");
                        }
                        if (!music) {
                            // else, = music is not playing, set message to "b"
                            setMessageToArduino("b");
                        }
                        // send message to Arduino-Server

                        if (controller.getGameStatus() && musicBefore != music) {
                            dos.write(getMessageToArduino().getBytes());
                            dos.flush();
                            musicBefore = music;
                        }

                    } catch (Exception e) {
                    }

                }
            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    // public static void main(String[] args) throws IOException {
    // new TestClient();
    // }


    public boolean isMusicBefore() {
        return this.musicBefore;
    }

    public boolean getMusicBefore() {
        return this.musicBefore;
    }

    public void setMusicBefore(boolean musicBefore) {
        this.musicBefore = musicBefore;
    }

    public boolean isSensorCovered() {
        return this.sensorCovered;
    }

    public boolean getSensorCovered() {
        return this.sensorCovered;
    }

    public void setSensorCovered(boolean sensorCovered) {
        this.sensorCovered = sensorCovered;
    }


    
    public String getMessageToArduino() {
        return messageToArduino;
    }

    public void setMessageToArduino(String messageToArduino) {
        this.messageToArduino = messageToArduino;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIpArduino() {
        return this.ipArduino;
    }

    public void setIpArduino(String ipArduino) {
        this.ipArduino = ipArduino;
    }

    public DataInputStream getDis() {
        return this.dis;
    }

    public void setDis(DataInputStream dis) {
        this.dis = dis;
    }

    public DataOutputStream getDos() {
        return this.dos;
    }

    public void setDos(DataOutputStream dos) {
        this.dos = dos;
    }

    public boolean isMusic() {
        return this.music;
    }

    public boolean getMusic() {
        return this.music;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public byte getMessageFromArduino() {
        return this.messageFromArduino;
    }

    public void setMessageFromArduino(byte messageFromArduino) {
        this.messageFromArduino = messageFromArduino;
    }

    public GameController getController() {
        return this.controller;
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

}
