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
        System.out.print("Please enter topics:");
        Scanner sc = new Scanner(System.in);
        String topics = sc.nextLine();
        //System.out.println("java.class.path:\n" + System.getProperty("java.class.path"));

        String[] trackTopic = topics.split(",");
        System.out.println("track topic: " + Arrays.toString(trackTopic));

        Configuration cb = genConfiguration();
        TwtStream[] twtStream = new TwtStream[trackTopic.length];
        for (int i = 0; i < twtStream.length; i++) {
            twtStream[i] = new TwtStream(trackTopic[i]);
            twtStream[i].genTwitterStream(cb).filter(new FilterQuery(trackTopic[i]));
        }

        int ret = 0;
        while(ret == 0) {
            ret = 1;
            for (TwtStream a: twtStream) {
                ret &= a.isTerminal();
            }
        }
        System.out.println("twitter over");
    }

    public static Properties getProperties() {
        String path = "twitter4j.properties"; //
        Properties prop = new Properties();

        try {
            InputStream inputStream = new FileInputStream(new File(path));
            prop.load(inputStream);
        } catch (FileNotFoundException ex) {
            System.out.println("---cannot found file: twitter4j.properties");
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
