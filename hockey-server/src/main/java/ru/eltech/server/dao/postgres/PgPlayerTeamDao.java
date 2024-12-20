package ru.eltech.server.dao.postgres;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.eltech.server.dao.interfaces.AbstractDao;
import ru.eltech.server.dao.interfaces.IPlayerTeamDao;
import ru.eltech.api.entity.PlayerTeam;

@Component
@Lazy
public class PgPlayerTeamDao extends AbstractDao<PlayerTeam> implements IPlayerTeamDao {

}
