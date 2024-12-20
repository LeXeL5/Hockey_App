package ru.eltech.api.editClasses;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.eltech.api.entity.City;
import ru.eltech.api.entity.League;
import ru.eltech.api.entity.Player;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class TeamEdit implements Serializable {

    private String name;
    private League leagueName;
    private City city;
    private List<Player> listPlayer;

    public TeamEdit(String name, League leagueName, City city, List<Player> listPlayer) {
        this.name = name;
        this.leagueName = leagueName;
        this.city = city;
        this.listPlayer = listPlayer;
    }
}
