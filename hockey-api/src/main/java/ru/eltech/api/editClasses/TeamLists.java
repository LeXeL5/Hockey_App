package ru.eltech.api.editClasses;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.eltech.api.entity.League;
import ru.eltech.api.entity.City;
import ru.eltech.api.entity.Player;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class TeamLists implements Serializable {
    private List<League> leagueList;
    private List<City> cityList;
    private List<Player> playerList;
}
