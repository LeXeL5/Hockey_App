package ru.eltech.api.editClasses;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class TeamFilter implements Serializable {

    private String name;
    private String league;
    private String city;
    public TeamFilter(String name, String league, String city) {
        this.name = name;
        this.league = league;
        this.city = city;
    }

}
