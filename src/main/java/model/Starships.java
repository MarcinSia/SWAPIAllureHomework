package model;

import lombok.Data;

import java.util.List;

@Data
public class Starships {
    private String MGLT;
    private String cargo_capacity;
    private String consumables;
    private String cost_in_credits;
    private String created;
    private String crew;
    private String edited;
    private String hyperdrive_rating;
    private String length;
    private String manufacturer;
    private String max_atmosphering_speed;
    private String model;
    private String name;
    private String passengers;
    private List films;
    private List pilots;
    private String starship_class;
    private String url;
}
