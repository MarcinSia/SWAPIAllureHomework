package model;

import lombok.Data;

import java.util.List;

@Data
public class Films {

    private List characters;
    private String created;
    private String director;
    private String edited;
    private Integer episode_id;
    private String opening_crawl;
    private List planets;
    private String producer;
    private String release_date;
    private List species;
    private List starships;
    private String title;
    private String url;
    private List vehicles;

}
