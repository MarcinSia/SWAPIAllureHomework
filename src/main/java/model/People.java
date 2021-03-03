package model;

import lombok.Data;

import java.util.List;

@Data
public class People {
    private String name;
    private String height;
    private String mass;
    private String hair_color;
    private String skin_color;
    private String eye_color;
    private String birth_year;
    private String gender;
    private String homeworld;
    private String created;
    private String edited;
    private String url;
    private List species;
    private List starships;
    private List vehicles;
    private List films;
}
