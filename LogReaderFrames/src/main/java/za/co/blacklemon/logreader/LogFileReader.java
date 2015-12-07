package za.co.blacklemon.logreader;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class LogFileReader implements Runnable {
    private final LogReaderWindow window;

    public LogFileReader(LogReaderWindow window) {
        this.window = window;
    }

    private static final String fileName = "D:\\Server\\wildfly-8.1.0.Final\\standalone\\log\\server.log";

    public void run() {
        FileInputStream in = null;

        try {
            in = new FileInputStream(fileName);

            in.skip(in.available() - 10000);
            int c;
            StringBuilder sb;
            String currentStyle = "info";
            while (1 == 1) {
                sb = new StringBuilder();
                while ((c = in.read()) != -1) {
                    byte[] b = new byte[]{(byte) c};
                    sb.append(new String(b, Charset.forName("UTF-8")));
                }

                if (sb.length() > 0) {
                    String[] lines = sb.toString().split("\n");
                    for (String str : lines) {
                        if (str.indexOf(" INFO ") == 23) {
                            currentStyle = "info";
                        } else if (str.indexOf(" WARN ") == 23) {
                            currentStyle = "warn";
                        } else if (str.indexOf(" FATAL ") == 23) {
                            currentStyle = "fatal";
                        } else if (str.indexOf(" SEVERE ") == 23) {
                            currentStyle = "severe";
                        } else if (str.indexOf(" ERROR ") == 23) {
                            currentStyle = "error";
                        }

                        if (currentStyle.equals("warn"))
                            window.warn(str);
                        else if (currentStyle.equals("fatal"))
                            window.fatal(str);
                        else if (currentStyle.equals("severe"))
                            window.severe(str);
                        else if (currentStyle.equals("error"))
                            window.error(str);
                        else
                            window.info(str);
                    }
                }
                TimeUnit.MILLISECONDS.sleep(500);
            }
        } catch (Exception ex) {
            //log.error(ex);
        } finally {

            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    //log.error(ex);
                }
            }
        }
    }
}
