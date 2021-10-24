package com.penningtonb.powercast;

import java.util.ArrayList;

/*
 * All directory related classes.
 */
class Directory {

}

/***
Represents an array of podcast directories, with each element in the array containing up to 10
episodes. This class exists primarily as a workaround for the clunky API, which only allows you to
get 10 episodes of a podcast at a time, so we make several successive API calls until all episodes
are contained in this class.
 */
class DirectoryFull {
    private ArrayList<DirectoryResponse> response;
    public ArrayList<DirectoryResponse> getResponse() {return response;}
    public void setResponse(ArrayList<DirectoryResponse> response) {this.response = response;}
}

/**
 * Represents the response from a single call to the /podcast endpoint.
 */
class DirectoryResponse {
    private int statusCode;
    private DirectoryPodcast body;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public DirectoryPodcast getBody() {
        return body;
    }

    public void setBody(DirectoryPodcast body) {
        this.body = body;
    }
}


class DirectoryPodcast {
    private String id;
    private String title;
    private String publisher;
    private String image;
    private String thumbnail;
    private String listennotes_url;
    private int total_episodes;
    private boolean explicit_content;
    private String description;
    private long itunes_id;
    private long latest_pub_date_ms;
    private long earliest_pub_date_ms;
    private String language;
    private String country;
    private String website;
    private boolean is_claimed;
    private String type;
    private ArrayList<Integer> genre_ids;
    private ArrayList<DirectoryEpisode> episodes;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getImage() {
        return image;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getListennotes_url() {
        return listennotes_url;
    }

    public int getTotal_episodes() {
        return total_episodes;
    }

    public boolean isExplicit_content() {
        return explicit_content;
    }

    public String getDescription() {
        return description;
    }

    public long getItunes_id() {
        return itunes_id;
    }

    public long getLatest_pub_date_ms() {
        return latest_pub_date_ms;
    }

    public long getEarliest_pub_date_ms() {
        return earliest_pub_date_ms;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    public String getWebsite() {
        return website;
    }

    public boolean isIs_claimed() {
        return is_claimed;
    }

    public String getType() {
        return type;
    }

    public ArrayList<Integer> getGenre_ids() {
        return genre_ids;
    }

    public ArrayList<DirectoryEpisode> getEpisodes() {
        return episodes;
    }
}

class DirectoryEpisode {
    private String id;
    private String title;
    private String description;
    private long pub_date_ms;
    private String audio;
    private int audio_length_sec;
    private String listennotes_url;
    private String image;
    private String thumbnail;
    private boolean maybe_audio_invalid;
    private String listennotes_edit_url;
    private boolean explicit_content;
    private String link;
    private DirectoryPodcast podcast;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public long getPub_date_ms() {
        return pub_date_ms;
    }

    public String getAudio() {
        return audio;
    }

    public int getAudio_length_sec() {
        return audio_length_sec;
    }

    public String getListennotes_url() {
        return listennotes_url;
    }

    public String getImage() {
        return image;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public boolean isMaybe_audio_invalid() {
        return maybe_audio_invalid;
    }

    public String getListennotes_edit_url() {
        return listennotes_edit_url;
    }

    public boolean isExplicit_content() {
        return explicit_content;
    }

    public String getLink() {
        return link;
    }

    public DirectoryPodcast getPodcast() {
        return podcast;
    }
}