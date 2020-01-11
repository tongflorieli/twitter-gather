package com.dg;

import twitter4j.*;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;


/**
 * @Package: twt
 * @ClassName: Twt
 * @Description: main function: gather twitter by topic
 */
public class Twt {
    /**
     * main:
     */
    public static void main(String[] args) throws TwitterException {
        int lenTopic = 0;
        System.out.println("Please Enter Topics (up to 5 topics):");
        Scanner sc = new Scanner(System.in);
        String[] topics = new String[5];
        for (lenTopic = 0; lenTopic < 5; lenTopic++) {
            String r = sc.nextLine();
            if (r.length() == 0) {
                break;
            }
            topics[lenTopic] = r;
        }
        //System.out.println("java.class.path:\n" + System.getProperty("java.class.path"));

        Properties prop = getProperties();
        TwtStream[] twtStream = new TwtStream[lenTopic];
        for (int i = 0; i < twtStream.length; i++) {
            twtStream[i] = new TwtStream(prop);
            twtStream[i].genTwitterStream(topics[i]).filter(new FilterQuery(topics[i]));
        }

        boolean ret = false;
        while(!ret) {
            ret = true;
            for (TwtStream a: twtStream) {
                ret = ret && a.isTerminal();
            }
        }
        System.out.println("twt over");
    }

    public static Properties getProperties() {
        String path = "twitter4j.properties"; //
        Properties prop = new Properties();

        try {
            InputStream inputStream = new FileInputStream(new File(path));
            prop.load(inputStream);
        } catch (FileNotFoundException ex) {
            System.out.println("---cannot found file: " + path);
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return prop;
    }
}
