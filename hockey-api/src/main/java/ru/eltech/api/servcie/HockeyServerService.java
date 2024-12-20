package ru.eltech.api.servcie;

import ru.eltech.api.editClasses.PlayerEdit;
import ru.eltech.api.editClasses.PlayerFilter;
import ru.eltech.api.editClasses.TeamEdit;
import ru.eltech.api.editClasses.TeamFilter;
import ru.eltech.api.entity.City;
import ru.eltech.api.entity.League;
import ru.eltech.api.entity.Player;
import ru.eltech.api.entity.PlayerRole;
import ru.eltech.api.entity.Team;

import java.util.List;

public interface HockeyServerService {

    boolean isLoggedIn();

    boolean isContainAnyTeam(Integer leagueId);

    List<League> loadAllLeague(String filter);

    List<League> loadAllLeague();

    List<City> loadAllCity();

    List<Player> loadAllPlayers(PlayerFilter filter);

    List<Player> loadAllPlayers();

    List<Team> loadAllTeam(TeamFilter filter);

    List<PlayerRole> findAllPlayerRole();

    void saveLeague(String leagueEdit);

    void savePlayer(PlayerEdit playerEdit);

    void saveTeam(TeamEdit teamEdit);

    List<Player> loadTeamPlayers(Integer teamId);

    List<Player> loadAllNotInTeam(Integer teamId);

    void updateLeague(Integer leagueId, String changedLeague);

    void updatePlayer(Integer playerId, PlayerEdit changedPlayer);

    void updateTeam(Integer teamId, TeamEdit changedTeam);

    void deletePlayerById(Integer playerId);

    void deleteLeagueById(Integer leagueId);

    void deleteTeamById(Integer teamId);

    boolean login(String login, String password);

    void logout();
}
