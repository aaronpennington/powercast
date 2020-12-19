package com.penningtonb.powercast;

import java.util.ArrayList;
import java.util.List;

class Subscriptions {
    ArrayList<String> podcast_ids;

    public ArrayList<String> getPodcastList() {
        return podcast_ids;
    }
}

class SubscribeAction {
    int status;
    String message;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
