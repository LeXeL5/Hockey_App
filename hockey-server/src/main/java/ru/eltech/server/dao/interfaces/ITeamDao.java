package ru.eltech.server.dao.interfaces;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import ru.eltech.api.editClasses.TeamEdit;
import ru.eltech.api.editClasses.TeamFilter;
import ru.eltech.api.entity.Team;

import java.util.List;

/**
 * Интерфейс DAO-класса, работающего с таблицей {@link Team}.
 */
public interface ITeamDao extends IDao<Team> {

    @Override
    default RowMapper<Team> rowMapper() {
        return (resultSet, i) -> {
            Team team = new Team();
            team.setId(resultSet.getInt("team_id"));
            team.setName(resultSet.getString("team_name"));
            team.setLeagueId(resultSet.getInt("league_id"));
            team.setLeagueName(resultSet.getString("league_name"));
            team.setCityId(resultSet.getInt("city_id"));
            team.setCityName(resultSet.getString("city_name"));
            team.setListPlayers(resultSet.getString("team_players"));
            return team;
        };
    }

    //================================================================================================================//

    @Transactional(readOnly = true)
    List<Team> findAll(TeamFilter teamFilter);

    @Transactional
    void deleteTeamById(Integer selectedTeamId);

    @Transactional
    void save(TeamEdit newTeamName);

    @Transactional
    void update(Integer selectedTeamId, TeamEdit changedTeam);

}
