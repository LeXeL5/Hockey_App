package ru.eltech.server.dao.interfaces;

import org.springframework.jdbc.core.RowMapper;
import ru.eltech.api.entity.PlayerTeam;

/**
 * Интерфейс DAO-класса, работающего с таблицей {@link PlayerTeam}.
 */
public interface IPlayerTeamDao extends IDao<PlayerTeam> {

    @Override
    default RowMapper<PlayerTeam> rowMapper() {
        return (resultSet, i) -> {
            PlayerTeam playerTeam = new PlayerTeam();
            playerTeam.setPlayerId(resultSet.getInt("player_id"));
            playerTeam.setTeamId(resultSet.getInt("team_id"));
            return playerTeam;
        };
    }

    //================================================================================================================//

}
