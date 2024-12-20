package ru.eltech.server.dao.postgres;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.eltech.server.dao.interfaces.AbstractDao;
import ru.eltech.server.dao.interfaces.IPlayerDao;
import ru.eltech.api.editClasses.PlayerEdit;
import ru.eltech.api.editClasses.PlayerFilter;
import ru.eltech.api.entity.Player;

import java.util.List;

import static ru.eltech.server.utils.ServerUtils.isBlank;

@Component
@Lazy
public class PgPlayerDao extends AbstractDao<Player> implements IPlayerDao {

    @Override
    public List<Player> findAll(PlayerFilter playerFilter) {
        return query("select " +
                "p.id as id, " +
                "p.name as name, " +
                "p.surname as surname, " +
                "p.age as age, " +
                "pr.id as player_role_id, " +
                "pr.name as player_role_name, " +
                "p.count_games as count_games, " +
                "p.count_points as count_points, " +
                "p.effectiveness as effectiveness, " +
                "p.player_number as player_number, " +
                "array_to_string(array(select t.name from team t inner join player_team pt on t.id = pt.team_id where pt.player_id = p.id order by t.name), ', ')  as player_teams " +
                "from " +
                "player p " +
                "inner join player_role pr on p.player_role_id = pr.id " +
                "where 1=1 " +
                (isBlank(playerFilter.getName()) ? "" : "and p.name like '%" + playerFilter.getName() + "%' ") +
                (isBlank(playerFilter.getSurname()) ? "" : "and p.surname like '%" + playerFilter.getSurname() + "%' ") +
                (isBlank(playerFilter.getAge()) ? "" : "and p.age = " + playerFilter.getAge() + " ") +
                (isBlank(playerFilter.getPlayerRole()) ? "" : "and pr.name like '%" + playerFilter.getPlayerRole() + "%' ") +
                (isBlank(playerFilter.getCountGames()) ? "" : "and p.count_games = " + playerFilter.getCountGames() + " ") +
                (isBlank(playerFilter.getCountPoints()) ? "" : "and p.count_points = " + playerFilter.getCountPoints() + " ") +
                (isBlank(playerFilter.getEffectiveness()) ? "" : "and p.effectiveness = " + playerFilter.getEffectiveness() + " ") +
                (isBlank(playerFilter.getPlayerNumber()) ? "" : "and p.player_number = " + playerFilter.getPlayerNumber() + " ") +
                "order by " +
                "p.id");
    }

    @Override
    public List<Player> findAllPlayers() {
        return query("select " +
                "p.id as id, " +
                "p.name as name, " +
                "p.surname as surname, " +
                "p.age as age, " +
                "pr.id as player_role_id, " +
                "pr.name as player_role_name, " +
                "p.count_games as count_games, " +
                "p.count_points as count_points, " +
                "p.effectiveness as effectiveness, " +
                "p.player_number as player_number, " +
                "array_to_string(array(select t.name from team t inner join player_team pt on t.id = pt.team_id where pt.player_id = p.id order by t.name), ', ')  as player_teams " +
                "from " +
                "player p " +
                "inner join player_role pr on p.player_role_id = pr.id " +
                "order by " +
                "p.id");
    }

    @Override
    public List<Player> findAllNotInTeam(Integer teamId) {
        return query("select " +
                "p.id as id, " +
                "p.name as name, " +
                "p.surname as surname, " +
                "p.age as age, " +
                "p.player_role_id as player_role_id, " +
                "null as player_role_name, " +
                "p.count_games as count_games, " +
                "p.count_points as count_points, " +
                "p.effectiveness as effectiveness, " +
                "p.player_number as player_number, " +
                "null as player_teams " +
                "from player p " +
                "where id not in(select player_id from player_team where team_id = " + teamId + ")");
    }

    @Override
    public void deletePlayerById(Integer selectedPlayerId) {
        update("delete from player where id = " + selectedPlayerId);
    }

    @Override
    public void save(PlayerEdit newPlayer) {
        update("insert into player(name, surname, age, player_role_id, count_games, count_points," +
                " effectiveness, player_number) values ('" +
                newPlayer.getName() + "', '"
                + newPlayer.getSurname() + "', "
                + newPlayer.getAge() + ", "
                + newPlayer.getPlayerRole().getId() + ", "
                + newPlayer.getCountGames() + ", "
                + newPlayer.getCountPoints() + ", "
                + newPlayer.getEffectiveness() + ", "
                + newPlayer.getPlayerNumber() + ");");
    }

    @Override
    public void update(Integer selectedPlayerId, PlayerEdit changedPlayer) {
        update("update player set name = '" + changedPlayer.getName() + "', " +
                "surname = '" + changedPlayer.getSurname() +
                "', age = " + changedPlayer.getAge() +
                ", player_role_id = " + changedPlayer.getPlayerRole().getId() +
                ", count_games = " + changedPlayer.getCountGames() +
                ", count_points = " + changedPlayer.getCountPoints() +
                ", effectiveness = " + changedPlayer.getEffectiveness() +
                ", player_number = " + changedPlayer.getPlayerNumber() +
                " where id = " + selectedPlayerId);
    }

    @Override
    public List<Player> findTeamPlayers(Integer teamId) {
        return query("select " +
                "p.id as id, " +
                "p.name as name, " +
                "p.surname as surname, " +
                "p.age as age, " +
                "p.player_role_id as player_role_id, " +
                "null as player_role_name, " +
                "p.count_games as count_games, " +
                "p.count_points as count_points, " +
                "p.effectiveness as effectiveness, " +
                "p.player_number as player_number, " +
                "null as player_teams " +
                "from player p " +
                "where id in(select player_id from player_team where team_id = " + teamId + ")");
    }
}
