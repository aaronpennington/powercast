package com.penningtonb.powercast;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

class SearchResponse {
    private int statusCode;
    private Search body;
    private String type;

    public Search getBody() {
        return body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

class Search {
    private String searchType;
    private double took;
    private int count;
    private int total;
    private int next_offset;
    private ArrayList<Podcast> results;

    // my thoughts:
    /*
        create a "results interface" so that the above code can be
        private ArrayList<Results> results;
        and then have if-statements (if (type.equals("podcast")))
        in which the specific Results class is declared

        private ArrayList<Results> results;

        if (type.equals("podcast")) {
            results = new ArrayList<Podcast>;
        }

        if (type.equals("episode")) {
            results = new ArrayList<Episode>;
        }

        of course, for this to work, i will need an Episode class
        and a Curated class
        so make those, you big dummy. 
     */

    public int getCount() {
        return count;
    }

    public ArrayList<Podcast> getResults() {
        return results;
    }

    public double getTook() {
        return took;
    }

    public int getTotal() {
        return total;
    }

    public int getNext_offset() {
        return next_offset;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }


    public void setResults() {
        if (getSearchType().equals("podcast")) {
            results = new ArrayList<Podcast>();
        }
//        if (getSearchType().equals("episode")) {
//            results = new ArrayList<Episode>();
//        }
//        if (getSearchType().equals("curated")) {
//            results = new ArrayList<Curated>();
//        }
    }

    /*
    ALL OF THIS NEEDS TO BE COMPLETED
    SEE POSTMAN API RESULTS TO GET THE CLASS TEMPLATES
    APPARENTLY PODCAST, EPISODE, AND CURATED HAVE DIFFERENT VALUES
    ALSO SEVERAL FIELDS REQUIRE THE PRO API PLAN, WHICH I DO NOT HAVE
    AT THE MOMENT.
    THUS IT WOULD BE PRUDENT TO SAVE TIME BY ONLY HAVING VARIABLES FOR
    THOSE FIELDS WHICH WILL ACTUALLY BE RETURNED IN THE JSON
     */
}

interface Results {}

class Podcast implements Results {
    private String description_highlighted;
    private String description_original;
    private String title_highlighted;
    private String title_original;
    private String publisher_highlighted;
    private String publisher_original;
    private String image;
    private String thumbnail;
    private int itunes_id;
    private long latest_pub_date_ms;
    private long earliest_pub_date_ms;
    private String id;
    private ArrayList<Integer> genre_ids;
    private String listennotes_url;
    private int total_episodes;
    private boolean explicit_content;
    private String website;

    public String getTitle_original() {
        return title_original;
    }

    public String getDescription_highlighted() {
        return description_highlighted;
    }

    public String getDescription_original() {
        return description_original;
    }

    public String getTitle_highlighted() {
        return title_highlighted;
    }

    public String getPublisher_highlighted() {
        return publisher_highlighted;
    }

    public String getPublisher_original() {
        return publisher_original;
    }

    public String getImage() {
        return image;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public int getItunes_id() {
        return itunes_id;
    }

    public long getLatest_pub_date_ms() {
        return latest_pub_date_ms;
    }

    public long getEarliest_pub_date_ms() {
        return earliest_pub_date_ms;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Integer> getGenre_ids() {
        return genre_ids;
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

    public String getWebsite() {
        return website;
    }
}

class Episode implements Results {
    private String audio;
    private int audio_length_sec;
    private String description_highlighted;
    private String description_original;
    private String title_highlighted;
    private String title_original;
    private String image;
    private String thumbnail;
    private long itunes_id;
    private long pub_date_ms;
    private String id;
    private String listennotes_url;
    private boolean explicit_content;
    private String link;
    private Podcast podcast;
    private String podcast_listennotes_url;
    private String podcast_id;
    private String podcast_title_highlighted;
    private String podcast_title_original;
    private List<Integer> genre_ids;
}


