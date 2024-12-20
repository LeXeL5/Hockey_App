package ru.eltech.server.dao.postgres;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.eltech.server.dao.interfaces.AbstractDao;
import ru.eltech.server.dao.interfaces.ITeamDao;
import ru.eltech.api.editClasses.TeamEdit;
import ru.eltech.api.editClasses.TeamFilter;
import ru.eltech.api.entity.Player;
import ru.eltech.api.entity.Team;

import java.util.List;
import java.util.stream.Collectors;

import static ru.eltech.server.utils.ServerUtils.isBlank;

@Component
@Lazy
public class PgTeamDao extends AbstractDao<Team> implements ITeamDao {

    @Override
    public List<Team> findAll(TeamFilter teamFilter) {
        return query("select " +
                "t.id as team_id, " +
                "t.name as team_name, " +
                "l.id as league_id, " +
                "l.name as league_name, " +
                "c.id as city_id, " +
                "c.name as city_name, " +
                "array_to_string(array(select concat(p.name, ' ', p.surname) from player p inner join player_team pt on p.id = pt.player_id where pt.team_id = t.id order by p.name), ', ') as team_players " +
                "from " +
                "team t " +
                "inner join league l on t.league_id = l.id " +
                "inner join city c on t.city_id = c.id " +
                "where 1=1 " +
                (isBlank(teamFilter.getName()) ? "" : "and t.name like '%" + teamFilter.getName() + "%' ") +
                (isBlank(teamFilter.getLeague()) ? "" : "and l.name like '%" + teamFilter.getLeague() + "%' ") +
                (isBlank(teamFilter.getCity()) ? "" : "and c.name like '%" + teamFilter.getCity() + "%' ") +
                "order by " +
                "t.id");
    }

    @Override
    public void deleteTeamById(Integer selectedTeamId) {
        update("delete from team where id = " + selectedTeamId);
    }

    @Override
    public void save(TeamEdit newTeam) {
        Integer newTeamId = queryForObject("insert into team(name, league_id, city_id) values " +
                "('" + newTeam.getName() + "', " + newTeam.getLeagueName().getId() + ", " + newTeam.getCity().getId() + ") " +
                "returning id", Integer.class);

        List<Player> players = newTeam.getListPlayer();
        if (!players.isEmpty()) {
            String valuesToInsert = players.stream()
                    .map(Player::getId)
                    .map(playerId -> String.format("(%s, %s)", playerId, newTeamId))
                    .collect(Collectors.joining(","));
            update("insert into player_team(player_id, team_id) values " + valuesToInsert);
        }
    }

    @Override
    public void update(Integer teamId, TeamEdit changedTeam) {
        update("update team set name = '" + changedTeam.getName() + "', " +
                "league_id = " + changedTeam.getLeagueName().getId() +
                ", city_id = " + changedTeam.getCity().getId() +
                " where id = " + teamId);

        update("delete from player_team where team_id = " + teamId);

        List<Player> players = changedTeam.getListPlayer();
        if (!players.isEmpty()) {
            String valuesToInsert = players.stream()
                    .map(Player::getId)
                    .map(playerId -> String.format("(%s, %s)", playerId, teamId))
                    .collect(Collectors.joining(","));
            update("insert into player_team(player_id, team_id) values " + valuesToInsert);
        }
    }

}
