package ru.eltech.server.dao.postgres;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.eltech.server.dao.interfaces.AbstractDao;
import ru.eltech.server.dao.interfaces.ICityDao;
import ru.eltech.api.entity.City;

import java.util.List;

@Component
@Lazy
public class PgCityDao extends AbstractDao<City> implements ICityDao {

    @Override
    public List<City> findAll() {
        return query("select * from city order by id");
    }
}
