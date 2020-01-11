package com.dg;

import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Properties;

public class TwtStream {
    private boolean isTerminal = false;
    Configuration cfg;

    public TwtStream(Properties prop) throws TwitterException {
        this.cfg = TwtStream.genConfiguration(prop);
    }

    public TwitterStream genTwitterStream(String topic) throws TwitterException {
        TwitterStream twitterStream = new TwitterStreamFactory(cfg).getInstance();
        return twitterStream.addListener(new StatusListener() {
            @Override
            public void onStatus(Status status) {
                StringBuilder sb = new StringBuilder("@" + status.getUser().getScreenName() + ": ");
                sb.append(status.getCreatedAt().toString()).append("\n")
                        .append(status.getSource()).append("\n")
                        .append(status.getText());
                new Thread(new SaveRunner(topic, sb.toString())).start();

                System.out.println(status.getText());
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                System.out.println("---TWT: Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("---TWT: Got track limitation notice:" + numberOfLimitedStatuses);
                //saver.close();
                isTerminal = true;
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                System.out.println("---TWT: Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("---TWT: Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                System.out.println("---TWT error: onException:");
                ex.printStackTrace();
            }
        });
    }

    public static Configuration genConfiguration(Properties prop) throws TwitterException {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(prop.getProperty("oauth.consumerKey"))
                .setOAuthConsumerSecret(prop.getProperty("oauth.consumerSecret"))
                .setOAuthAccessToken(prop.getProperty("oauth.accessToken"))
                .setOAuthAccessTokenSecret(prop.getProperty("oauth.accessTokenSecret"));
        cb.setJSONStoreEnabled(true);
        return cb.build();
    }

    public boolean isTerminal() {
        return isTerminal;
    }
}
