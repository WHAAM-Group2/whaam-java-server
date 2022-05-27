package server.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/** 
 * @author Malin Ramkull & Hedda Eriksson 
 *  Program that runs on a thread and connects as a client to an Arduino Uno device. 
 *  Connection is established by using TCP and is used to transmit data in both directions.    
 */
public class ArduinoHandler extends Thread {
    private int port = 1337;
    private String ipArduino = "192.168.0.12";
    private DataInputStream dis;
    private DataOutputStream dos;
    private boolean sensorCovered;
    private boolean music;
    private boolean musicBefore;
    private String messageToArduino;
    private byte messageFromArduino;
    private GameController controller;

    public ArduinoHandler(GameController controller) throws IOException {
        System.out.println("Started Arduino Client");
        this.controller = controller;
        connectToArduino();
    }

    public void connectToArduino() throws IOException {
        Socket socket = new Socket(ipArduino, port);
        System.out.println("Gamehandler started");
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
        musicBefore = false;
        start();
    }

    @Override
    public void run() {
        try {
            while (true) {

                messageFromArduino = dis.readByte();
                // 84 = T
                if (messageFromArduino == 84) {
                    setSensorCovered(true);
                    
                }
                // 70 = F
                if (messageFromArduino == 70) {
                    setSensorCovered(false);
                }

                try {
                    music = controller.getMusicStatus();

                    if (music) {
                        // if true, = music is playing, set message to "a"
                        setMessageToArduino("a");
                    }
                    if (!music) {
                        // else, = music is not playing, set message to "b"
                        setMessageToArduino("b");
                    }

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

    /* GETTERS & SETTERS */
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
