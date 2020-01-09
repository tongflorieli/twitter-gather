package com.dg;

public class SaveRunner implements Runnable {
    private String topic = "";
    private String text = "";
    private SaveTextFile file = new SaveTextFile();

    public SaveRunner(String topic, String text) {
        this.topic = topic;
        this.text = text;
    }

    @Override
    public void run() {
        file.open(topic + ".txt").saveText(text).close();
    }
}
