package com.dg;

import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.io.*;
import java.util.Arrays;
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
        System.out.print("Please Enter Topics (up to 5 topics):");
        Scanner sc = new Scanner(System.in);
        String[] topics = new String[5];
        for (lenTopic = 0; lenTopic < 5; lenTopic++) {
            String r = sc.nextLine();
            if (r.length() == 0) {
                break;
            }
            topics[lenTopic] = r;
        }

        Configuration cb = genConfiguration();
        TwtStream[] twtStream = new TwtStream[lenTopic];
        for (int i = 0; i < twtStream.length; i++) {
            twtStream[i] = new TwtStream(topics[i]);
            twtStream[i].genTwitterStream(cb).filter(new FilterQuery(topics[i]));
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

    //
    public static Configuration genConfiguration() throws TwitterException {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        Properties prop = getProperties();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(prop.getProperty("oauth.consumerKey"))
                .setOAuthConsumerSecret(prop.getProperty("oauth.consumerSecret"))
                .setOAuthAccessToken(prop.getProperty("oauth.accessToken"))
                .setOAuthAccessTokenSecret(prop.getProperty("oauth.accessTokenSecret"));
        cb.setJSONStoreEnabled(true);
        return cb.build();
    }

}
