package com.dg;

import java.io.*;

/**
 * @ClassName: SaveTextFile
 * @Description: save twitter message to file
 */
public class SaveTextFile {
    private BufferedWriter buf = null;
    private int lockCounter = 0;

    public SaveTextFile open(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new IOException("---throw exception: cannot create file: " + filePath);
                }
            }

            FileOutputStream fos = new FileOutputStream(file, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            buf = new BufferedWriter(osw);
        } catch (IOException ex) {
            System.out.println("---exception: SaveTextFile()");
            ex.printStackTrace();
        }
        lockCounter = 0;
        return this;
    }

    public SaveTextFile saveText(String text) {
        try {
            if (!("".equals(text)) && null != buf) {
                synchronized(this) {
                    buf.append(text).append("\n\n");
                    buf.flush();
                }
                lockCounter--;
            }
        } catch (IOException ex) {
            System.out.println("---TWT error: Exception: SaveTextFile.saveText");
            ex.printStackTrace();
        }
        return this;
    }

    public void close() {
        if (null != buf) {
            try {
                buf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void increaseCounter() {
        lockCounter++;
    }

    public int getLockCounter() {
        return lockCounter;
    }
}
