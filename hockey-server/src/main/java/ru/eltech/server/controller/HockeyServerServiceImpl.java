package ru.eltech.server.controller;

import org.springframework.stereotype.Component;
import ru.eltech.api.editClasses.PlayerEdit;
import ru.eltech.api.editClasses.PlayerFilter;
import ru.eltech.api.editClasses.TeamEdit;
import ru.eltech.api.editClasses.TeamFilter;
import ru.eltech.api.entity.City;
import ru.eltech.api.entity.League;
import ru.eltech.api.entity.Player;
import ru.eltech.api.entity.PlayerRole;
import ru.eltech.api.entity.Team;
import ru.eltech.api.servcie.HockeyServerService;
import ru.eltech.server.dao.interfaces.ICityDao;
import ru.eltech.server.dao.interfaces.ILeagueDao;
import ru.eltech.server.dao.interfaces.IPlayerDao;
import ru.eltech.server.dao.interfaces.IPlayerRoleDao;
import ru.eltech.server.dao.interfaces.IPlayerTeamDao;
import ru.eltech.server.dao.interfaces.ITeamDao;
import ru.eltech.server.service.AuthManager;

import java.util.List;

@Component
public class HockeyServerServiceImpl implements HockeyServerService {

    private final IPlayerDao playerDao;
    private final ILeagueDao leagueDao;
    private final ITeamDao teamDao;
    private final IPlayerRoleDao playerRoleDao;
    private final ICityDao cityDao;
    private final IPlayerTeamDao playerTeamDao;
    private final AuthManager authManager;

    public HockeyServerServiceImpl(IPlayerDao playerDao, ILeagueDao leagueDao, ITeamDao teamDao, IPlayerRoleDao playerRoleDao,
                                   ICityDao cityDao, IPlayerTeamDao playerTeamDao, AuthManager authManager) {
        this.playerDao = playerDao;
        this.leagueDao = leagueDao;
        this.teamDao = teamDao;
        this.playerRoleDao = playerRoleDao;
        this.cityDao = cityDao;
        this.playerTeamDao = playerTeamDao;
        this.authManager = authManager;
    }

    @Override
    public boolean isLoggedIn() {
        return authManager.isLoggedIn();
    }

    @Override
    public boolean isContainAnyTeam(Integer leagueId) {
        return leagueDao.isContainAnyTeam(leagueId);
    }

    @Override
    public List<League> loadAllLeague(String filter) {
        return leagueDao.findAllLeague(filter);
    }

    @Override
    public List<League> loadAllLeague() {
        return leagueDao.findAll();
    }

    @Override
    public List<City> loadAllCity() {
        return cityDao.findAll();
    }

    @Override
    public List<Player> loadAllPlayers(PlayerFilter filter) {
        return playerDao.findAll(filter);
    }

    @Override
    public List<Player> loadAllPlayers() {
        return playerDao.findAllPlayers();
    }

    @Override
    public List<Team> loadAllTeam(TeamFilter filter) {
        return teamDao.findAll(filter);
    }

    @Override
    public List<PlayerRole> findAllPlayerRole() {
        return playerRoleDao.findAll();
    }

    @Override
    public void saveLeague(String leagueEdit) {
        leagueDao.save(leagueEdit);
    }

    @Override
    public void savePlayer(PlayerEdit playerEdit) {
        playerDao.save(playerEdit);
    }

    @Override
    public void saveTeam(TeamEdit teamEdit) {
        teamDao.save(teamEdit);
    }

    @Override
    public List<Player> loadTeamPlayers(Integer teamId) {
        return playerDao.findTeamPlayers(teamId);
    }

    @Override
    public List<Player> loadAllNotInTeam(Integer teamId) {
        return playerDao.findAllNotInTeam(teamId);
    }

    @Override
    public void updateLeague(Integer leagueId, String changedLeague) {
        leagueDao.update(leagueId, changedLeague);
    }

    @Override
    public void updatePlayer(Integer playerId, PlayerEdit changedPlayer) {
        playerDao.update(playerId, changedPlayer);
    }

    @Override
    public void updateTeam(Integer teamId, TeamEdit changedTeam) {
        teamDao.update(teamId, changedTeam);
    }

    @Override
    public void deletePlayerById(Integer playerId) {
        playerDao.deletePlayerById(playerId);
    }

    @Override
    public void deleteLeagueById(Integer leagueId) {
        leagueDao.deleteLeagueById(leagueId);
    }

    @Override
    public void deleteTeamById(Integer teamId) {
        teamDao.deleteTeamById(teamId);
    }

    @Override
    public boolean login(String login, String password) {
        return authManager.login(login, password);
    }

    @Override
    public void logout() {
        authManager.logout();
    }
}
