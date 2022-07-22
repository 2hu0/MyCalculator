package soft.test;


import java.awt.*;
import java.io.File;
import java.io.IOException;

public class openFile {

    public static void main(String[] args) throws IOException {
        //text file, should be opening in default text editor
        File file = new File("D:\\test.txt");

        //first check if Desktop is supported by Platform or not
        if(!Desktop.isDesktopSupported()){
            System.out.println("Desktop is not supported");
            return;
        }

        Desktop desktop = Desktop.getDesktop();
        if(file.exists()) desktop.open(file);

    }

}
