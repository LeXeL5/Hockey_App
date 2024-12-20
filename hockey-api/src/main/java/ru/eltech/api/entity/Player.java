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
public class Player implements IEntity, Serializable {
    private Integer id;
    private String name;
    private String surname;
    private Integer age;
    private Integer playerRoleId;
    private String playerRoleName;
    private Integer countGames;
    private Integer countPoints;
    private Integer effectiveness;
    private Integer playerNumber;
    private String playerTeams;
}
