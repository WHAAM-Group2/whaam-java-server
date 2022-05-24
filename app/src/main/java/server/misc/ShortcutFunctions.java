package server.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/** 
 * @author Wael Mahrous
 */
public class ShortcutFunctions {

    /**
     * This method will load the OpenCV library given the path to the library.
     * @throws IOException
     */
    public static void initializeOpencv() throws IOException {
        File file = new File("dll/dllPath.txt"); // creates a new file instance
        FileReader fr = new FileReader(file); // reads the file
        BufferedReader br = new BufferedReader(fr); // creates a buffering character input stream

        System.load(br.readLine());
        br.close();
    }

}
