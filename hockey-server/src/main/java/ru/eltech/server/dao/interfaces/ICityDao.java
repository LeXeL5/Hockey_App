package ru.eltech.server.dao.interfaces;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import ru.eltech.api.entity.City;

import java.util.List;

/**
 * Интерфейс DAO-класса, работающего с таблицей {@link City}.
 */
public interface ICityDao extends IDao<City> {

	@Override
	default RowMapper<City> rowMapper() {
		return (resultSet, i) -> {
			City city = new City();
			city.setId(resultSet.getInt("id"));
			city.setName(resultSet.getString("name"));
			return city;
		};
	}

	//================================================================================================================//

	@Transactional
	public List<City> findAll();
}
