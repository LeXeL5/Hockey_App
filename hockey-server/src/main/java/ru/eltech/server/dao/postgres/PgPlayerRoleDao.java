package ru.eltech.server.dao.postgres;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.eltech.server.dao.interfaces.AbstractDao;
import ru.eltech.server.dao.interfaces.IPlayerRoleDao;
import ru.eltech.api.entity.PlayerRole;

import java.util.List;

@Component
@Lazy
public class PgPlayerRoleDao extends AbstractDao<PlayerRole> implements IPlayerRoleDao {

    @Override
    public List<PlayerRole> findAll()  {
        return query("select * from player_role order by id");
    }
}
