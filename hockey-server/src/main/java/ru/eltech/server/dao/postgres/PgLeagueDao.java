package ru.eltech.server.dao.postgres;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.eltech.server.dao.interfaces.AbstractDao;
import ru.eltech.server.dao.interfaces.ILeagueDao;
import ru.eltech.api.entity.League;

import java.util.List;

import static ru.eltech.server.utils.ServerUtils.isBlank;

@Component
@Lazy
public class PgLeagueDao extends AbstractDao<League> implements ILeagueDao {

    @Override
    public List<League> findAll() {
        return query(
                "select " +
                        "l.id as league_id, " +
                        "l.name as league_name, " +
                        "array_to_string(array(select t.name from team t where t.league_id = l.id order by l.id), ', ') as league_teams " +
                        "from " +
                        "league l " +
                        "order by " +
                        "l.id");
    }

    @Override
    public List<League> findAllLeague(String filter) {
        return query(
                "select " +
                "l.id as league_id, " +
                "l.name as league_name, " +
                "array_to_string(array(select t.name from team t where t.league_id = l.id order by l.id), ', ') as league_teams " +
                "from " +
                "league l " +
                "where 1=1 " +
                (isBlank(filter) ? "" : "and l.name like '%" + filter + "%' ") +
                "order by " +
                "l.id");
    }

    @Override
    public boolean isContainAnyTeam(Integer selectedLeagueId) {
        Integer count = queryForObject("select count(*) from team where league_id = " + selectedLeagueId, Integer.class);
        return count != 0;
    }

    @Override
    public void deleteLeagueById(Integer selectedLeagueId) {
        update("delete from league where id = " + selectedLeagueId);
    }

    @Override
    public void save(String newLeagueName) {
        update("insert into league(name) values ('" + newLeagueName + "')");
    }

    @Override
    public void update(Integer selectedLeagueId, String changedLeagueName) {
        update("update league set name = '" + changedLeagueName + "' where id = " + selectedLeagueId);
    }
}
