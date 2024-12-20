package ru.eltech.server.dao.interfaces;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import ru.eltech.api.entity.League;

import java.util.List;

/**
 * Интерфейс DAO-класса, работающего с таблицей {@link League}.
 */
public interface ILeagueDao extends IDao<League> {

    @Override
    default RowMapper<League> rowMapper() {
        return (resultSet, i) -> {
            League league = new League();
            league.setId(resultSet.getInt("league_id"));
            league.setName(resultSet.getString("league_name"));
            league.setTeams(resultSet.getString("league_teams"));
            return league;
        };
    }

    //================================================================================================================//

    @Transactional(readOnly = true)
    List<League> findAll();

    @Transactional
    List<League> findAllLeague(String filter);

    @Transactional(readOnly = true)
    boolean isContainAnyTeam(Integer selectedLeagueId);

    @Transactional
    void deleteLeagueById(Integer selectedLeagueId);

    @Transactional
    void save(String newLeagueName);

    @Transactional
    void update(Integer selectedLeagueId, String changedLeagueName);
}
