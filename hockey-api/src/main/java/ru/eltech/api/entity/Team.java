package ru.eltech.api.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Team implements IEntity, Serializable {
    private Integer id;
    private String name;
    private Integer leagueId;
    private String leagueName;
    private Integer cityId;
    private String cityName;
    private String listPlayers;
}
