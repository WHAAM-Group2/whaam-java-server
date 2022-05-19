package javaserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import server.controller.GameController;

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
    private GameController controller;
    private boolean musicBefore;

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
                    if (messageFromArduino == 87) {
                        setWin(true);
                    }
                    // 76 = L
                    if (messageFromArduino == 76) {
                        setWin(false);
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

    public boolean getWin() {
        return this.win;
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

    public MusicSimulator getMusicSimulator() {
        return this.musicSimulator;
    }

    public void setMusicSimulator(MusicSimulator musicSimulator) {
        this.musicSimulator = musicSimulator;
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
